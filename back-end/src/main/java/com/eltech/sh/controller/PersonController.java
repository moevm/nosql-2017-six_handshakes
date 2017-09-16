package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.model.RequestStatus;
import com.eltech.sh.repository.PersonRepository;
import com.eltech.sh.service.VKService;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PersonController {

    private final VKService vkService;
    private final PersonRepository personRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public PersonController(VKService vkService, PersonRepository personRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.vkService = vkService;
        this.personRepository = personRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        UserXtrCounters userById = vkService.getUserById(id);
        return new Person(userById.getFirstName(), userById.getLastName());
    }

    //TODO move getUserMethod in VkService
    @GetMapping("/persons/{personId}/friends")
    List<Person> getPersonFriends(@PathVariable("personId") String id){
        simpMessagingTemplate.convertAndSend("/topic/status", RequestStatus.FETCHING_DATA_FROM_VK);
        List<Person> friends = vkService.findPersonFriends(id);
        UserXtrCounters userById = vkService.getUserById(id);
        Person user = new Person(userById.getFirstName(), userById.getLastName());
        friends.forEach(user::friendOf);

        simpMessagingTemplate.convertAndSend("/topic/status", RequestStatus.SAVING_DATE_TO_DB);

        personRepository.save(user);
        return friends;
    }

    //TODO Now this method just redirects to getPersonFriend. We need to add some logic instead of it
    @GetMapping("/find")
    List<Person> checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId) {
        return getPersonFriends(fromId);
    }
}
