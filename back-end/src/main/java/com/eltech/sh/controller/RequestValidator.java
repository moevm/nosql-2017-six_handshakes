package com.eltech.sh.controller;

import com.eltech.sh.service.VKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {
    @Autowired
    private VKService vkService;

    public void ifIdValid(String id) {
        if (id == null) {
            throw new IllegalArgumentException("User id must not be empty");
        }
        if (vkService.getPersonByStringId(id) == null) {
            throw new IllegalArgumentException("User with this id is not exist");
        }
    }
}
