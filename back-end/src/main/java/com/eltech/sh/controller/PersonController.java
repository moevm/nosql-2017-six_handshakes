package com.eltech.sh.controller;

import com.eltech.sh.model.Person;
import com.eltech.sh.service.FixedHandshakeService;
import com.eltech.sh.service.HandshakeService;
import com.eltech.sh.service.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class PersonController {

    private final VKService vkService;
    private final HandshakeService handshakeService;
    private final FixedHandshakeService fixedHandshakeService;

    @Autowired
    public PersonController(VKService vkService,
                            HandshakeService handshakeService,
                            FixedHandshakeService fixedHandshakeService) {
        this.vkService = vkService;
        this.handshakeService = handshakeService;
        this.fixedHandshakeService = fixedHandshakeService;
    }

    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        return vkService.getUserByStringId(id);
    }

    @GetMapping("/find")
    Iterable<Person> checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId) {
        return fixedHandshakeService.checkSixHandshakes(fromId,toId);
    }
}
