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
import javax.validation.constraints.Null;
import javax.xml.bind.ValidationException;


@RestController
public class PersonController {

    private final RequestValidator requestValidator;
    private final CoreService coreService;

    @Autowired
    public PersonController(CoreService coreService, RequestValidator requestValidator) {
        this.coreService = coreService;
        this.requestValidator = requestValidator;
    }

    @GetMapping("/me")
    public Person me(HttpServletRequest request) {
        String id = (String) request.getSession().getAttribute("current_user_id");
        return coreService.getPersonByStringId(id);
    }

    @GetMapping("/find")
    public ResponseBean checkSixHandshakes(@RequestParam("from") String fromId, @RequestParam("to") String toId, HttpServletRequest request) {
        String currentUserId = (String) request.getSession().getAttribute("current_user_id");
        requestValidator.ifIdValid(fromId);
        requestValidator.ifIdValid(toId);
        return coreService.run(fromId, toId, currentUserId);
    }

    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleValidationException(Exception e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
