package project.springboot.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springboot.library.models.Book;
import project.springboot.library.models.Person;
import project.springboot.library.repositories.BooksRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository rep;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository rep, PeopleService peopleService) {
        this.rep = rep;
        this.peopleService = peopleService;
    }

    public Page<Book> findAll(Pageable a) {
        return rep.findAll(a);
    }

    public Book findOne(int id) {
        return rep.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String title) {
        return rep.findByTitleStartingWith(title);
    }

    @Transactional
    public void save(Book book) {
        rep.save(book);
    }

    @Transactional
    public void update(int id, Book newBook) {
        newBook.setId(id);
        newBook.setOwner(rep.findById(id).get().getOwner());
        rep.save(newBook);
    }

    @Transactional
    public void delete(int id) {
        rep.deleteById(id);
    }

    @Transactional
    public void takeTheBook(int id, int personId) {
        Book book = findOne(id);
        Person person = peopleService.findOne(personId);
        book.setOwner(person);
        book.setTakenAt(new Date());
        rep.save(book);
    }

    @Transactional
    public void returnBook(int id) {
        Book book = findOne(id);
        book.setOwner(null);
        book.setTakenAt(null);
        rep.save(book);
    }
}


