package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.eltech.sh.service.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    //FIXME fix hardcode
    @GetMapping("/persons/{personId}/friends")
    List<Person> getPersonFriends(@PathVariable("personId") String id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("access_token");
        List<Person> friends = vkService.findFriend(id, token);

        Person user = new Person("Serezha", "Karpunin");
        friends.forEach(user::friendOf);
        personRepository.save(user);
        return friends;
    }


}
