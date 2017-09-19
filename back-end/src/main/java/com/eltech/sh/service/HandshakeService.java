package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HandshakeService {

    private final VKService vkService;
    private final PersonRepository personRepository;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;

    @Autowired
    public HandshakeService(VKService vkService, PersonRepository personRepository) {
        this.vkService = vkService;
        this.personRepository = personRepository;

    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);

        Integer origIdFrom = vkService.getOriginalId(from),
                origIdTo = vkService.getOriginalId(to);

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        //findFriends(origIdFrom);
        //findFriends(origIdTo);

        //if targets already friends
        //Iterable<Person> path = personRepository.findPathByQuery(origIdFrom, origIdTo);

        //if (path.iterator().hasNext()) {
        //  return path;
        //} else {
        // findNextLevelFriends(origIdFrom, origIdTo, 1);
        //findNextLevelFriends(origIdTo, origIdFrom, 1);
        Iterable<Person> path = createGraph(origIdFrom, origIdTo);
        return path;
        // return personRepository.findPathByQuery(origIdFrom, origIdTo);
        //}
    }

    protected Iterable<Person> createGraph(int from, int to) {
        // while (!toVisit.isEmpty() && level < 3) {
        Queue<Integer> nextLevel = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            while (!toVisit.isEmpty()) {
                Integer cur = toVisit.poll();
                if (!visited.contains(cur)) {
                    List<Person> friends = savePersonFriends(cur.toString());
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

            Iterable<Person> friends = personRepository.findPathByQuery(from, to);
            if (friends.iterator().hasNext()) return friends;
        }
        return personRepository.findPathByQuery(from, to);
    }

    protected List<Person> savePersonFriends(String id) {
        List<Person> friends = vkService.findPersonFriends(id);
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getId(), userById.getFirstName(), userById.getLastName());
        friends.forEach(user::friendOf);
        System.out.println(user.getVkId());
        personRepository.save(user);


        return friends;
    }
}