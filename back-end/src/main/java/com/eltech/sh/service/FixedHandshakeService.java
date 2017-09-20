package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FixedHandshakeService {

    private final VKService vkService;
    private final PersonRepository personRepository;
    private Set<Integer> visited;
    private Queue<Integer> toVisit;
    private static final Logger logger = LoggerFactory.getLogger(FixedHandshakeService.class);

    @Autowired
    public FixedHandshakeService(VKService vkService, PersonRepository personRepository) {
        this.vkService = vkService;
        this.personRepository = personRepository;

    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {
        toVisit = new LinkedList<>();
        visited = new HashSet<>(10000);

        Integer origIdFrom = vkService.getOriginalId(from);
        Integer origIdTo = vkService.getOriginalId(to);

        toVisit.add(origIdFrom);
        toVisit.add(origIdTo);

        return createGraph(origIdFrom, origIdTo);
    }

    protected Iterable<Person> createGraph(int from, int to) {
        Queue<Integer> nextLevel = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            logger.debug("ITERATION: " + i);
            while (!toVisit.isEmpty()) {
                Integer cur = toVisit.poll();
                logger.debug("POLLED ID: " + cur);
                if (!visited.contains(cur)) {
                    logger.debug("ISN'T VISITED ID: " + cur);
                    logger.debug("REQUESTING FRIENDS");
                    List<Person> friends = savePersonFriends(cur.toString());
                    logger.debug("NUMBER OF FRIENDS " + friends.size());
                    visited.add(cur);
                    logger.debug("CHECKING FRIENDS");
                    for (Person p : friends) {
                        if (!visited.contains(p.getVkId())) {
                            logger.debug("ADDED TO VISITED " + p.getVkId());
                            nextLevel.add(p.getVkId());
                        }
                    }
                }
            }
            logger.debug("ADDED ALL INTO TO VISIT, SIZE " + nextLevel.size());
            toVisit.addAll(nextLevel);
            nextLevel.clear();

            logger.debug("SEARCH FOR PATH");
            Iterable<Person> friends = personRepository.findPathByQuery(from, to);
            if (friends.iterator().hasNext()) {
                logger.debug("SUCCESS");
                return friends;
            } else {
                logger.debug("FAILURE");
            }
        }
        logger.debug("CYCLE IS OVER");
        return personRepository.findPathByQuery(from, to);
    }

    protected List<Person> savePersonFriends(String id) {
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getId(), userById.getFirstName(), userById.getLastName());

        logger.debug("REQUESTING FRIENDS");

        List<Person> friends = vkService.findPersonFriends(id);
        logger.debug("REQUESTING IS OVER");

        if(friends != null){
            friends.forEach(user::friendOf);
            System.out.println(user.getVkId());
            logger.debug("SAVING FRIENDS");

            personRepository.save(user);
            logger.debug("SAVING IS OVER");

            return friends;
        } else {
            return new ArrayList<>();
        }
    }
}