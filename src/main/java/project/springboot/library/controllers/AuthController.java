package project.springboot.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.springboot.library.models.Person;
import project.springboot.library.security.PersonDetails;
import project.springboot.library.services.PeopleService;
import project.springboot.library.services.RegistrationService;
import project.springboot.library.util.PeopleValidator;

import java.security.Principal;
import java.util.Objects;

//@Controller
//@RequestMapping("/auth")
public class AuthController {

    private final PeopleValidator peopleValidator;
    private final RegistrationService registrationService;
    private final PeopleService peopleService;

    @Autowired
    public AuthController(PeopleValidator peopleValidator, RegistrationService registrationService, PeopleService peopleService) {
        this.peopleValidator = peopleValidator;
        this.registrationService = registrationService;
        this.peopleService = peopleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/error")
    public String error() {
        return "redirect:/login?error";
    }

    @GetMapping("/new")
    public String newUserPage(@ModelAttribute("person") Person person) {
        return "/auth/new";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("person") @Valid Person person,
                          BindingResult br, Principal principal) {
        peopleValidator.validate(person, br);
        if (br.hasErrors())
            return "/auth/new";

        registrationService.register(person);

        PersonDetails personDetails = (PersonDetails) peopleService.loadUserByUsername(principal.getName());
        Person currentUser = personDetails.getPerson();
        if (Objects.equals(currentUser.getRole(), "ROLE_ADMIN"))
            return "redirect:/admin/people";

        return "redirect:/auth/hello";
    }

    @GetMapping("/hello")
    public String helloPage() {
        return "auth/hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "auth/adminStart";
    }
}
