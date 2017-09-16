package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HandshakeService {

    private final VKService vkService;
    private final PersonRepository personRepository;
    private Set<Integer> visited;

    @Autowired
    public HandshakeService(VKService vkService, PersonRepository personRepository) {
        this.vkService = vkService;
        this.personRepository = personRepository;
        visited = new HashSet<>(10000);
    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {

        findFriends(vkService.getOriginalId(from));
        findFriends(vkService.getOriginalId(to));

        Integer origIdFrom = vkService.getOriginalId(from),
                origIdTo = vkService.getOriginalId(to);

        //if targets already friends
        Iterable<Person> path = personRepository.findPathByQuery(origIdFrom, origIdTo);

        if (path.iterator().hasNext()) {
            return path;
        } else {
            findNextLevelFriends(origIdFrom, origIdTo, 1);
            findNextLevelFriends(origIdFrom, origIdTo, 2);
            return personRepository.findPathByQuery(origIdFrom, origIdTo);
        }
    }

    protected void findFriends(Integer id) {
        savePersonFriends(id.toString());
        visited.add(id);
    }

    protected void findNextLevelFriends(Integer current, Integer target, int curLevel) {

        Iterable<Person> friends = personRepository.findFriendsById(current);

        for (Person p : friends) {
            if (p.getVkId().equals(target)) {
                return;
            }

            if (!visited.contains(p.getVkId())) {
                visited.add(p.getVkId());
                savePersonFriends(current.toString());
                if (curLevel < 3) {
                    findNextLevelFriends(p.getVkId(), target, curLevel++);
                }
            }
        }
    }


    protected void savePersonFriends(String id) {
        List<Person> friends = vkService.findPersonFriends(id);
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getId(), userById.getFirstName(), userById.getLastName());
        friends.forEach(user::friendOf);
        personRepository.save(user);
    }
}
