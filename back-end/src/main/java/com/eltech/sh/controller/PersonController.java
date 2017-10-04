package com.eltech.sh.controller;

import com.eltech.sh.beans.ResponseBean;
import com.eltech.sh.beans.TimeBean;
import com.eltech.sh.model.Person;
import com.eltech.sh.service.HandshakeService;
import com.eltech.sh.service.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class PersonController {

    private final VKService vkService;
    private final HandshakeService handshakeService;

    @Autowired
    public PersonController(VKService vkService,
                            HandshakeService handshakeService) {
        this.vkService = vkService;
        this.handshakeService = handshakeService;
    }

    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        return vkService.getPersonByStringId(id);
    }

    @GetMapping("/find")
    ResponseBean checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId) {
        List<Person> list = handshakeService.checkSixHandshakes(fromId, toId);
        TimeBean timeBean = handshakeService.getTimerValues();
        Integer peopleCount = handshakeService.getPeopleCount();
        Integer web  = handshakeService.getCurrentWeb();
        return new ResponseBean(list, handshakeService.findAllPaths(fromId,toId),timeBean, peopleCount, web);
    }
}
