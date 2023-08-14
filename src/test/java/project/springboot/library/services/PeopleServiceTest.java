package project.springboot.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.springboot.library.models.Person;
import project.springboot.library.repositories.PeopleRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    private PeopleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PeopleService(peopleRepository);
    }

    @Test
    void findAll() {
        underTest.findAll();

        verify(peopleRepository).findAll();
    }

    @Test
    void findOne() {
        int id = 9;
        underTest.findOne(id);

        ArgumentCaptor<Integer> argumentCaptor =
                ArgumentCaptor.forClass(Integer.class);
        verify(peopleRepository)
                .findById(argumentCaptor.capture());
        Integer capturedNum = argumentCaptor.getValue();
        assertThat(capturedNum.equals(id));
    }

    @Test
    void save() {
        Person person = new Person("Томас", 1998);
        underTest.save(person);

        ArgumentCaptor<Person> personArgumentCaptor =
                ArgumentCaptor.forClass(Person.class);

        verify(peopleRepository)
                .save(personArgumentCaptor.capture());

        Person capturedPerson = personArgumentCaptor.getValue();
        assertThat(capturedPerson.equals(person));
    }

    @Test
    void updateBeingDoneInDatabase() {
        int id = 5;
        Person person = new Person("Томас", 1998);
        person.setId(id);
        peopleRepository.save(person);
        person.setYear(2001);
        underTest.update(id, person);
        Optional<Person> captured = peopleRepository.findByName("Томас");
        assertThat(captured.isPresent() && captured.get().getYear() == 2001);
    }

    @Test
    void updateArgumentsAreCorrect() {
        int id = 5;
        Person person = new Person("Томас", 1998);
        person.setId(id);
        underTest.update(id, person);

        ArgumentCaptor<Integer> intArgCaptor =
                ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Person> personArgCaptor =
                ArgumentCaptor.forClass(Person.class);

        verify(peopleRepository)
                .save(personArgCaptor.capture());

        Person capturedPerson = personArgCaptor.getValue();
        int capturedId = capturedPerson.getId();
        assertThat(capturedPerson.equals(person) && capturedId == id);
    }

    @Test
    void delete() {
        int id = 8;
        underTest.delete(id);

        ArgumentCaptor<Integer> numberArgumentCaptor =
                ArgumentCaptor.forClass(Integer.class);

        verify(peopleRepository)
                .deleteById(numberArgumentCaptor.capture());

        int capturedId = numberArgumentCaptor.getValue();
        assertThat(capturedId == id);
    }
}