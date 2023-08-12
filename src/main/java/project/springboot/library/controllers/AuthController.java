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
import project.springboot.library.services.PeopleService;
import project.springboot.library.services.RegistrationService;
import project.springboot.library.util.PeopleValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PeopleValidator peopleValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(PeopleValidator peopleValidator, RegistrationService registrationService) {
        this.peopleValidator = peopleValidator;
        this.registrationService = registrationService;
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
                          BindingResult br) {
        peopleValidator.validate(person, br);
        if (br.hasErrors()) {
            return "/auth/new";
        }
        registrationService.register(person);
        return "redirect:/hello";
    }
}
