package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import com.eltech.sh.beans.GraphBean;
import com.eltech.sh.beans.Node;
import com.eltech.sh.beans.ResponseBean;
import com.eltech.sh.enums.Timers;
import com.eltech.sh.model.Person;
import com.eltech.sh.utils.NodeUtils;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoreServiceImpl implements CoreService {

    private final VKService vkService;
    private final MessageService messageService;
    private final TimerService timerService;
    private final CSVService csvService;
    private final DBService dbService;

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
    public ResponseBean run(String fromId, String toId, String currentUserId) {
        timerService.init();

        Integer origFromId = vkService.getPersonIntegerIdByStringId(fromId);
        Integer origToId = vkService.getPersonIntegerIdByStringId(toId);
        Integer origCurrentUserId = vkService.getPersonIntegerIdByStringId(currentUserId);

        //TODO remove peopleChecked
        ResponseBean info = new ResponseBean(
                checkSixHandshakes(origFromId, origToId, origCurrentUserId),
                findAllPaths(origFromId, origToId, origCurrentUserId),
                timerService.getTimers(),
                0,
                dbService.countPeople(origCurrentUserId)
        );

        dbService.deleteCluster(origCurrentUserId);
        return info;
    }

    @Override
    public Person getPersonByStringId(String id) {
        return vkService.getPersonByStringId(id);
    }

    private List<Person> checkSixHandshakes(Integer from, Integer to, Integer currentUserID) {
        List<Integer> nodeIds = findPath(from, to, currentUserID);
        return vkService.getPersonsByIds(nodeIds);
    }

    private GraphBean findAllPaths(Integer fromID, Integer toID, Integer currentUserID) {
        Pair<List<Edge>, List<Integer>> graphData = dbService.findWebByQuery(fromID, toID, currentUserID);
        List<Person> people = vkService.getPersonsByIds(graphData.getValue());

        List<Node> nodes = NodeUtils.getNodesByPeople(people, fromID, toID);
        return new GraphBean(nodes, graphData.getKey());
    }

    private List<Integer> findPath(Integer from, Integer to, Integer currentUserID) {
        Map<Integer, List<Integer>> userFriendsMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>(1000);
        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(from);
        toVisit.add(to);

        List<Integer> currentLevelIds = new ArrayList<>();
        Queue<Integer> nextLevelIds = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            messageService.notify("Started iteration # " + (i + 1));
            doSearch(visited, currentLevelIds, nextLevelIds, toVisit, userFriendsMap);
            saveFriends(currentUserID, userFriendsMap);

            messageService.notify("Searching for path");
            timerService.startTimer(Timers.PATH_TIMER);
            List<Integer> nodeIds = dbService.findPathByQuery(from, to, currentUserID);
            timerService.suspendTimer(Timers.PATH_TIMER);

            if (!nodeIds.isEmpty()) {
                messageService.notify("Path is found");
                timerService.startTimer(Timers.CSV_TIMER);
                csvService.deleteCSV(String.valueOf(currentUserID));
                timerService.suspendTimer(Timers.CSV_TIMER);
                return nodeIds;
            } else {
                messageService.notify("There is no path yet");
                timerService.startTimer(Timers.CSV_TIMER);
                csvService.deleteCSV(String.valueOf(currentUserID));
                timerService.suspendTimer(Timers.CSV_TIMER);
            }
        }
        return dbService.findPathByQuery(from, to, currentUserID);
    }

    private void saveFriends(Integer currentUserID, Map<Integer, List<Integer>> userFriendsMap) {
        timerService.startTimer(Timers.CSV_TIMER);
        messageService.notify("Saving friends");
        csvService.saveToSeparateFile(userFriendsMap, String.valueOf(currentUserID));
        timerService.suspendTimer(Timers.CSV_TIMER);
        timerService.startTimer(Timers.DB_TIMER);
        messageService.notify("Migrate friends to database");
        dbService.migrateToDB(currentUserID);
        timerService.suspendTimer(Timers.DB_TIMER);
        timerService.startTimer(Timers.CSV_TIMER);
        userFriendsMap.clear();
        timerService.suspendTimer(Timers.CSV_TIMER);
    }

    private void doSearch(Set<Integer> visited,
                          List<Integer> currentLevelIds,
                          Queue<Integer> nextLevelIds,
                          Queue<Integer> toVisit,
                          Map<Integer, List<Integer>> userFriendsMap) {

        currentLevelIds.clear();

        while (!toVisit.isEmpty()) {
            messageService.notify("People to check: " + toVisit.size());

            Integer currentID = toVisit.poll();
            currentLevelIds.add(currentID);
            visited.add(currentID);

            if (currentLevelIds.size() == 24) {
                timerService.startTimer(Timers.VK_TIMER);
                Map<Integer, List<Integer>> map = findFriendsForGivenPeople(currentLevelIds);
                timerService.suspendTimer(Timers.VK_TIMER);

                userFriendsMap.putAll(map);
                for (Integer id : getFriendsList(map)) {
                    if (!visited.contains(id)) {
                        nextLevelIds.add(id);
                    }
                }
                currentLevelIds.clear();
            }
        }
        if (currentLevelIds.size() != 0) {
            timerService.startTimer(Timers.VK_TIMER);
            Map<Integer, List<Integer>> map = findFriendsForGivenPeople(currentLevelIds);
            timerService.suspendTimer(Timers.VK_TIMER);

            userFriendsMap.putAll(map);
            for (Integer id : getFriendsList(map)) {
                if (!visited.contains(id)) {
                    nextLevelIds.add(id);
                }
            }
            currentLevelIds.clear();
        }

        toVisit.addAll(nextLevelIds);
        nextLevelIds.clear();
    }

    private Map<Integer, List<Integer>> findFriendsForGivenPeople(List<Integer> userIds) {
        messageService.notify("Requesting friends");
        return vkService.findFriendsForGivenPeople(userIds);
    }

    private List<Integer> getFriendsList(Map<Integer, List<Integer>> map) {
        Collection<List<Integer>> values = map.values();
        return values.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
