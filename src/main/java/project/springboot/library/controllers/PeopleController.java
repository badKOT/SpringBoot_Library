package project.springboot.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.springboot.library.dto.AddPersonRqDto;
import project.springboot.library.dto.UpdatePersonRqDto;
import project.springboot.library.models.Person;
import project.springboot.library.security.PersonDetails;
import project.springboot.library.services.PeopleService;

import java.security.Principal;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/account")
    public String accountPage(Model model, Principal principal) {

        PersonDetails details = (PersonDetails) peopleService.loadUserByUsername(principal.getName());
        Person person = details.getPerson();
        model.addAttribute("person", person);
        model.addAttribute("books", peopleService.getBooksByPersonId(person.getId()));

        return "people/account";
    }

    @PostMapping("/add")
    @ResponseBody
    public int addPerson(@RequestBody AddPersonRqDto dto) {
        return peopleService.save(new Person(dto.name(), 0));
    }

    @PostMapping("/update")
    @ResponseBody
    public int updatePerson(@RequestBody UpdatePersonRqDto dto) {
        return peopleService.update(dto);
    }
}
