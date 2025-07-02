package project.springboot.library.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.springboot.library.dto.AddBookRqDto;
import project.springboot.library.models.Book;
import project.springboot.library.services.BooksService;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BooksController {

    private final BooksService booksService;

    @GetMapping()
    public String index(Model model, @ModelAttribute("books") Book book,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "booksPerPage", required = false) Integer booksPerPage) {

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

    @ResponseBody
    @PostMapping("/add")
    public Boolean addBook(@RequestBody AddBookRqDto dto) {
        log.info("got request to save book:" + dto);
        booksService.save(dto.personId(), dto.title(), dto.author(), dto.year());
        log.info("request processing finished");
        return true;
    }
}
