package com.eltech.sh.service;

import com.eltech.sh.beans.TimeBean;
import com.eltech.sh.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
                if(currFriendIds.size() == 24)
                {
                    startTimer(vkTimer);
                    List<Integer>  friendIds = findAndSavePersonFriends(currFriendIds);
                    vkTimer.suspend();
                    for (Integer id : friendIds) {
                        if (!visited.contains(id)) {
                            nextLevel.add(id);
                        }
                    }
                    currFriendIds.clear();
                }
            }
            if(currFriendIds.size() != 0)
            {
                startTimer(vkTimer);
                List<Integer>  friendIds = findAndSavePersonFriends(currFriendIds);
                vkTimer.suspend();
                for (Integer id : friendIds) {
                    if (!visited.contains(id)) {
                        nextLevel.add(id);
                    }
                }
                currFriendIds.clear();
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

    private List<Integer> findAndSavePersonFriends(List<Integer> userIds) {
        peopleCount+=userIds.size();
        notify("REQUESTING FRIENDS OF USERS #" + userIds);
        JsonNode friendIds = vkService.getFriendsByIds(userIds);
        List<Integer> save = new ArrayList<>();
        int i=0;
        for (JsonNode curr: friendIds) {
            if(curr.size() > 0) {
                List<Integer> saveNode = vkService.objectMapper.convertValue(curr.findValue("items"), new TypeReference<List<Integer>>() {
                });
                data.put(userIds.get(i), saveNode);
                notify("RESPONSE: " + saveNode.size() + " FRIENDS");
                i++;
                save.addAll(saveNode);
            } else {
                notify("RESPONSE: " + 0 + " FRIENDS (USER IS BANNED OR SMTH ELSE)");
                return new ArrayList<>();
            }
        }
        return save;
    }

    private void notify(String msg) {
//        System.out.println(msg);
        simpMessagingTemplate.convertAndSend("/topic/status", msg);
    }
}