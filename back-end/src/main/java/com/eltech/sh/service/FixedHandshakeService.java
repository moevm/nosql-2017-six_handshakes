package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FixedHandshakeService {

    private final VKService vkService;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;
    private List<Person> data;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CSVService csvService;
    private final DBService dbService;

    @Autowired
    public FixedHandshakeService(VKService vkService, SimpMessagingTemplate simpMessagingTemplate, CSVService csvService, DBService dbService) {
        this.vkService = vkService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.csvService = csvService;
        this.dbService = dbService;
    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);
        data = new ArrayList<>();

        Integer origIdFrom = vkService.getUserByStringId(from).getVkId();
        Integer origIdTo = vkService.getUserByStringId(to).getVkId();

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        List<Person> path = createGraph(origIdFrom, origIdTo);
        List<Person> result = new ArrayList<>();

        path.forEach(p -> {
            p.setPhotoUrl(vkService.getUserImgUrl(p.getVkId()));
            result.add(p);
        });

        return result;
    }

    private List<Person> createGraph(int from, int to) {
        Queue<Integer> nextLevel = new LinkedList<>();
        Date startTime = new Date();

        for (int i = 0; i < 3; i++) {
            notify("STARTED ITERATION# " + i);
            while (!toVisit.isEmpty()) {
                notify("PEOPLE TO CHECK " + toVisit.size());
                Integer cur = toVisit.poll();
                if (!visited.contains(cur)) {

                    List<Person> friends = findAndSavePersonFriends(cur.toString());
                    visited.add(cur);

                    for (Person p : friends) {
                        if (!visited.contains(p.getVkId())) {
                            nextLevel.add(p.getVkId());
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
            List<Person> friends = dbService.findPathByQuery(from, to);
            if (friends.iterator().hasNext()) {
                notify("PATH IS FOUND: " + new Date(new Date().getTime() - startTime.getTime()));
                csvService.deleteCSV();
                return friends;
            } else {
                notify("THERE IS NO PATH YET");
                csvService.deleteCSV();
            }
        }
        return dbService.findPathByQuery(from, to);
    }

    private List<Person> findAndSavePersonFriends(String id) {
        Person user = vkService.getUserByStringId(id);

        notify("REQUESTING FRIENDS OF " + user.getFirstName() + " " + user.getLastName());
        List<Person> friends = vkService.findPersonFriends(user.getVkId());

        if (friends != null) {
            friends.forEach(user::friendOf);
            data.add(user);
            notify("RESPONSE: " + friends.size() + " FRIENDS");
            return friends;
        } else {
            notify("RESPONSE: " + 0 + " FRIENDS (USER IS BANNED OR SMTH ELSE)");
            return new ArrayList<>();
        }

    }

    private void notify(String msg) {
        simpMessagingTemplate.convertAndSend("/topic/status", msg);
    }
}