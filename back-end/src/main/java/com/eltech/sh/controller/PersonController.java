package com.eltech.sh.controller;

import com.eltech.sh.beans.ResponseBean;
import com.eltech.sh.model.Person;
import com.eltech.sh.service.CoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class PersonController {

    private final CoreService coreService;

    @Autowired
    public PersonController(CoreService coreService) {
        this.coreService = coreService;
    }

    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        return coreService.getPersonByStringId(id);
    }

    @GetMapping("/find")
    public ResponseBean checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId, HttpServletRequest request) {
        String currentUserId = (String) request.getSession().getAttribute("current_user_id");
        return coreService.run(fromId, toId, currentUserId);
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleValidationException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
