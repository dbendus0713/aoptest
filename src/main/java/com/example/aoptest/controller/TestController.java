package com.example.aoptest.controller;

import com.example.aoptest.config.aop.usersession.IUserSession;
import com.example.aoptest.etc.JwtAuthentication;
import com.example.aoptest.etc.UserSession;
import com.example.aoptest.service.TestService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/aop", produces = MediaTypes.HAL_JSON_VALUE)
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }


    @GetMapping
    public ResponseEntity selectEvents(@ModelAttribute JwtAuthentication authentication, @IUserSession UserSession userSession) {
        testService.doSomething(userSession, null, "test string value");
        return ResponseEntity.ok(userSession);
    }

}
