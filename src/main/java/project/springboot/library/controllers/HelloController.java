package project.springboot.library.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.springboot.library.security.PersonDetails;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String helloPage() {
        return "hello/hello";
    }

    @GetMapping("/showUserInfo")
    public String userInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) auth.getPrincipal();
        System.out.println(personDetails.getPerson());
        return "hello/hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "hello/admin";
    }
}
