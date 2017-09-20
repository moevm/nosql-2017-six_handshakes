package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FixedHandshakeService {

    private final VKService vkService;
    private final PersonRepository personRepository;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public FixedHandshakeService(VKService vkService, PersonRepository personRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.vkService = vkService;
        this.personRepository = personRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);

        Integer origIdFrom = vkService.getUserByStringId(from).getVkId();
        Integer origIdTo = vkService.getUserByStringId(to).getVkId();

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        return createGraph(origIdFrom, origIdTo);
    }

    private Iterable<Person> createGraph(int from, int to) {
        Queue<Integer> nextLevel = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            notify("STARTED ITERATION# " + i);
            while (!toVisit.isEmpty()) {
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

            notify("FINDING PATH");
            Iterable<Person> friends = personRepository.findPathByQuery(from, to);
            if (friends.iterator().hasNext()) {
                notify("PATH IS FOUND");
                return friends;
            } else {
                notify("THERE IS NO PATH YET");
            }
        }
        return personRepository.findPathByQuery(from, to);
    }

    private List<Person> findAndSavePersonFriends(String id) {
        Person user = vkService.getUserByStringId(id);

        notify("REQUESTING FRIENDS OF " + user.getFirstName() + " " + user.getLastName());
        List<Person> friends = vkService.findPersonFriends(user.getVkId());
        notify("RESPONSE: " + friends.size() + " FRIENDS");

        friends.forEach(user::friendOf);

        notify("SAVING FRIENDS TO NEO4J");
        personRepository.save(user);

        return friends;
    }

    private void notify(String msg){
        simpMessagingTemplate.convertAndSend("/topic/status", msg);
    }
}