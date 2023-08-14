package project.springboot.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.springboot.library.models.Book;
import project.springboot.library.services.BooksService;
import project.springboot.library.services.PeopleService;

@Controller
@RequestMapping("/admin/books")
public class AdminBooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public AdminBooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model, @ModelAttribute("books") Book book,
                        @RequestParam(value = "page", required = false)
                        Integer page,
                        @RequestParam(value = "booksPerPage", required = false)
                        Integer booksPerPage) {

        Pageable pagination = PageRequest.of(page == null ? 0 : page,
                booksPerPage == null ? 25 : booksPerPage,
                Sort.by("year"));
        model.addAttribute("books", booksService.findAll(pagination));
        return "admin/books/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = booksService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("owner", book.getOwner());
        return "admin/books/info";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "admin/books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "admin/books/new";

        booksService.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "admin/books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/books/edit";

        booksService.update(id, book);
        return "redirect:/admin/books";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/admin/books";
    }

    @PatchMapping("/{id}/take")
    public String takeBook(@PathVariable("id") int id,
                           @RequestParam("personId") int person_id) {
        booksService.takeTheBook(id, person_id);
        return "redirect:/admin/books/" + id;
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable("id") int id) {
        booksService.returnBook(id);
        return "redirect:/admin/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("title") String title) {
        model.addAttribute("books", booksService.searchByTitle(title));
        return "books/search";
    }
}
