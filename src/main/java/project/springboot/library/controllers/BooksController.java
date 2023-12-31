package project.springboot.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.springboot.library.models.Book;
import project.springboot.library.services.BooksService;
import project.springboot.library.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
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
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = booksService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("owner", book.getOwner());
        return "books/show";
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
