package project.springboot.library.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.springboot.library.models.Person;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PeopleRepositoryTest {

    @Autowired
    private PeopleRepository underTest;
    @Autowired
    private BooksRepository booksRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        booksRepository.deleteAll();
    }

    @Test
    void itShouldFindByName() {
        Person person = new Person("Томас", 1999);
        underTest.save(person);

        Optional<Person> foundPerson = underTest.findByName("Томас");

        assertThat(foundPerson.isPresent());
    }

    @Test
    void itShouldLetUserWithNewNameRegister() {
        String name = "Томас";

        Optional<Person> foundPerson = underTest.findByName(name);

        assertThat(foundPerson.isEmpty());
    }
}