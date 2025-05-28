package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN
@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @Qualifier("getTimeOfDay")
    @Autowired
    private Daytime nameTime;

    @GetMapping
    public String index() {
        return "It is " + nameTime.getName() + " now! Welcome to Spring!";
    }

}