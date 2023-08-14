package project.springboot.library.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.springboot.library.models.Book;
import project.springboot.library.models.Person;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BooksRepositoryTest {

    @Autowired
    private BooksRepository underTest;
    @Autowired
    private PeopleRepository peopleRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        peopleRepository.deleteAll();
    }

    @Test
    void itShouldFindBooksByTitleStartingWith() {
        Book book = new Book("aaabc", "she", 1900);
        underTest.save(book);

        List<Book> capturedBooks = underTest.findByTitleStartingWith("aaa");
        assertThat(capturedBooks.get(0).equals(book));
    }
}