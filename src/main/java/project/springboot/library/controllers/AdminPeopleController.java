package project.springboot.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.springboot.library.models.Person;
import project.springboot.library.services.PeopleService;

@Controller
@RequestMapping("/admin/people")
class AdminPeopleController {

    private final PeopleService peopleService;

    @Autowired
    public AdminPeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("")
    public String allUsers(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "admin/people/people";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getBooksByPersonId(id));
        return "admin/people/info";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "admin/people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult,
    @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/people/edit";

        peopleService.update(id, person);
        return "redirect:/admin/people";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/admin/people";
    }
}
