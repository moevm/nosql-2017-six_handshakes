package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.eltech.sh.service.VKService;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private final VKService vkService;
    private final PersonRepository personRepository;

    @Autowired
    public PersonController(VKService vkService, PersonRepository personRepository) {
        this.vkService = vkService;
        this.personRepository = personRepository;
    }

    //TODO move getUserMethod in VkService
    @GetMapping("/persons/{personId}/friends")
    List<Person> getPersonFriends(@PathVariable("personId") String id) {
        List<Person> friends = vkService.findPersonFriends(id);
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getFirstName(), userById.getLastName());
        friends.forEach(user::friendOf);
        personRepository.save(user);
        return friends;
    }


}
