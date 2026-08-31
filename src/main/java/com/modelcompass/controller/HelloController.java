package com.modelcompass.controller;

import com.modelcompass.service.HelloService;               //importing because it is asking the service to perform the work.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;    // creating reference

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return helloService.getHelloMessage();
    }
}
