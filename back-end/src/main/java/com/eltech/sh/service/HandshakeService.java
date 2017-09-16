package com.eltech.sh.service;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandshakeService {

    private final VKService vkService;
    private final PersonRepository personRepository;

    @Autowired
    public HandshakeService(VKService vkService, PersonRepository personRepository) {
        this.vkService = vkService;
        this.personRepository = personRepository;
    }

    public Iterable<Person> checkSixHandshakes(String from, String to) {
        savePersonFriends(from);
        savePersonFriends(to);
        //return (List<Person>) personRepository.findAll();
        return personRepository.findPathByQuery(43787759L,211604452L);
    }

    protected void savePersonFriends(String id) {
        List<Person> friends = vkService.findPersonFriends(id);
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getId().longValue(), userById.getFirstName(), userById.getLastName());
        friends.forEach(user::friendOf);
        personRepository.save(user);
    }
}
