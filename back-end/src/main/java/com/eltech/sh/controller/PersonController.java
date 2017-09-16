package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.service.HandshakeService;
import com.eltech.sh.service.VKService;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PersonController {

    private final VKService vkService;
    private final HandshakeService handshakeService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public PersonController(VKService vkService, SimpMessagingTemplate simpMessagingTemplate, HandshakeService handshakeService) {
        this.vkService = vkService;
        this.handshakeService = handshakeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        UserXtrCounters userById = vkService.getUserById(id);
        return new Person(vkService.getOriginalId(id), userById.getFirstName(), userById.getLastName());
    }

    @PostMapping("/find")
    Iterable<Person> checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId) {
        return handshakeService.checkSixHandshakes(fromId,toId);
    }
}
