package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HandshakeService {

    private final VKService vkService;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;
    private Map<Integer,List<Integer>> data;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CSVService csvService;
    private final DBService dbService;

    @Autowired
    public HandshakeService(VKService vkService, SimpMessagingTemplate simpMessagingTemplate, CSVService csvService, DBService dbService) {
        this.vkService = vkService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.csvService = csvService;
        this.dbService = dbService;
    }

    public List<Person> checkSixHandshakes(String from, String to) {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);
        data = new HashMap<>();

        Integer origIdFrom = vkService.getPersonIntegerIdByStringId(from);
        Integer origIdTo = vkService.getPersonIntegerIdByStringId(to);

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        List<Integer> nodeIds = findPath(origIdFrom, origIdTo);
        return vkService.getPersonsByIds(nodeIds);
    }

    private List<Integer> findPath(int from, int to) {
        Queue<Integer> nextLevel = new LinkedList<>();
        Date startTime = new Date();

        for (int i = 0; i < 3; i++) {
            notify("STARTED ITERATION# " + i);
            while (!toVisit.isEmpty()) {
                notify("PEOPLE TO CHECK " + toVisit.size());
                Integer cur = toVisit.poll();
                if (!visited.contains(cur)) {

                    List<Integer> friendIds = findAndSavePersonFriends(cur.toString());
                    visited.add(cur);

                    for (Integer id : friendIds) {
                        if (!visited.contains(id)) {
                            nextLevel.add(id);
                        }
                    }
                }
            }
            toVisit.addAll(nextLevel);
            nextLevel.clear();
            notify("SAVING FRIENDS TO NEO4J");
            csvService.save(data);
            notify("MIGRATION TO DB");
            dbService.migrateToDB();
            notify("MIGRATION TO DB IS OVER");
            data.clear();

            notify("FINDING PATH");
            List<Integer> nodeIds = dbService.findPathByQuery(from, to);
            if(!nodeIds.isEmpty()){
                notify("PATH IS FOUND: " + new Date(new Date().getTime() - startTime.getTime()));
                csvService.deleteCSV();
                return nodeIds;
            } else {
                notify("THERE IS NO PATH YET");
                csvService.deleteCSV();
            }
        }
        return dbService.findPathByQuery(from, to);
    }

    private List<Integer> findAndSavePersonFriends(String userId) {
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
        simpMessagingTemplate.convertAndSend("/topic/status", msg);
    }
}