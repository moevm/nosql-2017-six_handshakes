package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import com.eltech.sh.beans.Graph;
import com.eltech.sh.beans.Node;
import com.eltech.sh.beans.ResponseBean;
import com.eltech.sh.enums.Timers;
import com.eltech.sh.model.Person;
import com.eltech.sh.utils.NodeUtils;
import javafx.util.Pair;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoreServiceImpl implements CoreService {

    private final VKService vkService;
    private final MessageService messageService;
    private final TimerService timerService;
    private final CSVService csvService;
    private final DBService dbService;

    @Value("${core.partitionSize}")
    private Integer partitionSize;

    @Autowired
    public CoreServiceImpl(
            VKService vkService,
            MessageService messageService,
            TimerService timerService,
            CSVService csvService,
            DBService dbService) {
        this.vkService = vkService;
        this.messageService = messageService;
        this.timerService = timerService;
        this.csvService = csvService;
        this.dbService = dbService;
    }

    @Override
    public Person getPersonByStringId(String id) {
        return vkService.getPersonByStringId(id);
    }

    @Override
    public ResponseBean run(String fromId, String toId, String currentUserId) {
        validateIds(fromId, toId);

        //FIXME check how it works with multiple users
        timerService.init();

        Integer origFromId = vkService.getPersonIntegerIdByStringId(fromId);
        Integer origToId = vkService.getPersonIntegerIdByStringId(toId);
        Integer origCurrentUserId = vkService.getPersonIntegerIdByStringId(currentUserId);

        ResponseBean response = runBidirectionalSearch(origFromId, origToId, origCurrentUserId);

        dbService.deleteCluster(origCurrentUserId);
        return response;
    }

    private ResponseBean runBidirectionalSearch(Integer from, Integer to, Integer currentUserID) {
        Set<Integer> visited = new HashSet<>(1000);
        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(from);
        toVisit.add(to);

        for (int i = 0; i < 3; i++) {
            messageService.notify("Started iteration # " + (i + 1));
            Boolean isPathExist = singleIteration(visited, toVisit, from, to, currentUserID);

            if (isPathExist) {
                messageService.notify("Path is found");

                Pair<List<Edge>, List<Integer>> graphData = dbService.findWebByQuery(from, to, currentUserID);
                List<Person> people = vkService.getPersonsByIds(graphData.getValue());
                List<Node> nodes = NodeUtils.getNodesByPeople(people, from, to);
                Graph graph = new Graph(nodes, graphData.getKey());
//FIXME wrong path length calculation - people.size() = total amount of result graph's nodes instead of path length
                return new ResponseBean(
                        graph,
                        timerService.getTimers(),
                        people.size(),
                        dbService.countPeople(currentUserID)
                );
            } else {
                messageService.notify("There is no path yet");
            }
        }
        messageService.notify("No path found");
        return null;
    }

    private Boolean singleIteration(Set<Integer> visited,
                                    Queue<Integer> toVisit,
                                    Integer from,
                                    Integer to,
                                    Integer currentUserID) {
        List<List<Integer>> batches = ListUtils.partition(new ArrayList<>(toVisit), partitionSize);
        visited.addAll(toVisit);
        toVisit.clear();

        Map<Integer, List<Integer>> userFriendsMap = new HashMap<>();

        for (List<Integer> batch : batches) {
            messageService.notify("Requesting friends");
            timerService.startTimer(Timers.VK_TIMER);
            Map<Integer, List<Integer>> map = vkService.findFriendsForGivenPeople(batch);
            timerService.suspendTimer(Timers.VK_TIMER);

            userFriendsMap.putAll(map);
            List<Integer> friendsIDs = map.values().stream().flatMap(List::stream).collect(Collectors.toList());

            for (Integer id : friendsIDs) {
                if (!visited.contains(id)) {
                    toVisit.add(id);
                }
            }
        }

        messageService.notify("Saving friends");
        timerService.startTimer(Timers.CSV_TIMER);
        csvService.saveToSeparateFile(userFriendsMap, String.valueOf(currentUserID));
        timerService.suspendTimer(Timers.CSV_TIMER);

        messageService.notify("Migrate friends to database");
        timerService.startTimer(Timers.DB_TIMER);
        dbService.migrateToDB(currentUserID);
        timerService.suspendTimer(Timers.DB_TIMER);

        messageService.notify("Searching for path");
        timerService.startTimer(Timers.PATH_TIMER);
        Boolean isPathExist = dbService.isPathExist(from, to, currentUserID);
        timerService.suspendTimer(Timers.PATH_TIMER);

        timerService.startTimer(Timers.CSV_TIMER);
        csvService.deleteCSV(String.valueOf(currentUserID));
        timerService.suspendTimer(Timers.CSV_TIMER);

        return isPathExist;
    }

    private void validateIds(String fromId, String toId) {
        messageService.notify("Validating IDs");
        Assert.isTrue(!fromId.equals(toId), "Ids shouldn't be the same");
        Assert.notNull(vkService.getPersonByStringId(fromId), String.format("User with ID %s doesn't exist", fromId));
        Assert.notNull(vkService.getPersonByStringId(toId), String.format("User with ID %s doesn't exist", toId));
    }
}
