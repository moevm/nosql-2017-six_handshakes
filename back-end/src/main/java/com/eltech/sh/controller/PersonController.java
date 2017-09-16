package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.repository.PersonRepository;
import com.eltech.sh.service.HandshakeService;
import com.eltech.sh.service.VKService;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    private final PersonRepository personRepository;
    private final HandshakeService handshakeService;

    @Autowired
    public PersonController(VKService vkService, PersonRepository personRepository, HandshakeService handshakeService) {
        this.handshakeService = handshakeService;
        this.personRepository = personRepository;
    }

    @PostMapping("/find")
    List<Person> checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId) {
        return handshakeService.checkSixHandshakes(fromId,toId);
    }
}
