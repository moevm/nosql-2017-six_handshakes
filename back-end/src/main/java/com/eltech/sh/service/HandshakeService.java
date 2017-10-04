package com.eltech.sh.service;

import com.eltech.sh.beans.Edge;
import com.eltech.sh.beans.GraphBean;
import com.eltech.sh.beans.Node;
import com.eltech.sh.beans.TimeBean;
import com.eltech.sh.model.Person;
import javafx.util.Pair;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HandshakeService {

    private final VKService vkService;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;
    private Map<Integer, List<Integer>> data;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CSVService csvService;
    private final DBService dbService;
    private StopWatch vkTimer;
    private StopWatch dbTimer;
    private StopWatch pathTimer;
    private StopWatch csvTimer;
    private Integer peopleCount;

    @Autowired
    public HandshakeService(VKService vkService, SimpMessagingTemplate simpMessagingTemplate, CSVService csvService, DBService dbService /*,
                           /* StopWatch vkTimer, StopWatch dbTimer, StopWatch pathTimer */) {
        this.vkService = vkService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.csvService = csvService;
        this.dbService = dbService;
    }

    private void init() {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);
        data = new HashMap<>();
        this.vkTimer = new StopWatch();
        this.dbTimer = new StopWatch();
        this.pathTimer = new StopWatch();
        this.csvTimer = new StopWatch();
        peopleCount = 0;
    }

    public List<Person> checkSixHandshakes(String from, String to) {
        init();

        Integer origIdFrom = vkService.getPersonIntegerIdByStringId(from);
        Integer origIdTo = vkService.getPersonIntegerIdByStringId(to);

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        List<Integer> nodeIds = findPath(origIdFrom, origIdTo);
        return vkService.getPersonsByIds(nodeIds);
    }

    public TimeBean getTimerValues() {
        return new TimeBean(dbTimer.getTime(), vkTimer.getTime(), pathTimer.getTime(), csvTimer.getTime());
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public Integer getCurrentWeb() {
        return dbService.countPeople(1);
    }

    public GraphBean findAllPaths(String from, String to) {
        Pair<List<Edge>, List<Integer>> graphData = dbService.findWebByQuery(vkService.getPersonIntegerIdByStringId(from),
                vkService.getPersonIntegerIdByStringId(to));

        List<Person> people = vkService.getPersonsByIds(graphData.getValue());

        List<Node> nodes = people.stream().map(vk -> new Node(vk.getVkId(),
                String.valueOf(vk.getFirstName() + " " + vk.getLastName()),
                vk.getPhotoUrl()))
                .collect(Collectors.toList());
        return new GraphBean(nodes, graphData.getKey());
    }

    private List<Integer> findPath(int from, int to) {
        Queue<Integer> nextLevel = new LinkedList<>();
        Date startTime = new Date();

        for (int i = 0; i < 3; i++) {
            notify("STARTED ITERATION# " + i);

            List<Integer> currFriendIds = new ArrayList<>();
            while (!toVisit.isEmpty()) {
                notify("PEOPLE TO CHECK " + toVisit.size());
                Integer cur = toVisit.poll();
                currFriendIds.add(cur);
                visited.add(cur);
                if (currFriendIds.size() == 24) {
                    startTimer(vkTimer);
                    List<Integer> friendIds = vkService.getFriendsByIds(currFriendIds);
                    vkTimer.suspend();
                    currFriendIds.clear();
                    for (Integer id : friendIds) {
                        if (!visited.contains(id)) {
                            nextLevel.add(id);
                        }
                    }
                }
            }
            if (currFriendIds.size() != 0) {
                startTimer(vkTimer);
                List<Integer> friendIds = vkService.getFriendsByIds(currFriendIds);
                vkTimer.suspend();
                currFriendIds.clear();
                for (Integer id : friendIds) {
                    if (!visited.contains(id)) {
                        nextLevel.add(id);
                    }
                }
            }

            toVisit.addAll(nextLevel);
            nextLevel.clear();

            startTimer(csvTimer);
            notify("SAVING FRIENDS TO NEO4J");
            csvService.save(data);
            csvTimer.suspend();

            startTimer(dbTimer);
            notify("MIGRATION TO DB");
            dbService.migrateToDB();
            notify("MIGRATION TO DB IS OVER");
            dbTimer.suspend();
            startTimer(csvTimer);
            data.clear();
            csvTimer.suspend();

            notify("FINDING PATH");
            startTimer(pathTimer);
            List<Integer> nodeIds = dbService.findPathByQuery(from, to);
            pathTimer.suspend();

            if (!nodeIds.isEmpty()) {
                notify("PATH IS FOUND: " + new Date(new Date().getTime() - startTime.getTime()));
                startTimer(csvTimer);
                csvService.deleteCSV();
                csvTimer.suspend();
                return nodeIds;
            } else {
                notify("THERE IS NO PATH YET");
                startTimer(csvTimer);
                csvService.deleteCSV();
                csvTimer.suspend();
            }
        }
        return dbService.findPathByQuery(from, to);
    }

    private void startTimer(StopWatch timer) {
        if (timer.isSuspended()) {
            timer.resume();
        } else {
            timer.start();
        }
    }

    private List<Integer> findAndSavePersonFriends(String userId) {
        peopleCount++;
        Integer id = vkService.getPersonIntegerIdByStringId(userId);

        notify("REQUESTING FRIENDS OF USER #" + id);
        List<Integer> friendIds = vkService.findIdsOfPersonFriends(id);

        if (friendIds != null) {
            data.put(id, friendIds);
            notify("RESPONSE: " + friendIds.size() + " FRIENDS");
            return friendIds;
        } else {
            notify("RESPONSE: " + 0 + " FRIENDS (USER IS BANNED OR SMTH ELSE)");
            return new ArrayList<>();
        }

    }

    private void notify(String msg) {
//        System.out.println(msg);
        simpMessagingTemplate.convertAndSend("/topic/status", msg);
    }
}