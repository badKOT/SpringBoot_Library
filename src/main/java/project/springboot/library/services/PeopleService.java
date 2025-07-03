package project.springboot.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springboot.library.dto.PersonRecord;
import project.springboot.library.dto.UpdatePersonRqDto;
import project.springboot.library.models.Book;
import project.springboot.library.models.Person;
import project.springboot.library.repositories.PeopleRepository;
import project.springboot.library.security.PersonDetails;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {

    private final PeopleRepository rep;

    @Autowired
    public PeopleService(PeopleRepository rep) {
        this.rep = rep;
    }

    public List<Person> findAll() {
        return rep.findAll();
    }

    public Person findOne(int id) {
        return rep.findById(id).orElse(null);
    }

    @Transactional
    public int save(Person person) {
        return rep.save(person).getId();
    }

    @Transactional
    public int update(UpdatePersonRqDto dto) {
        var person = rep.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Person with id " + dto.id() + " not found"));
        Optional.ofNullable(dto.name()).ifPresent(name -> person.setName(name));
        Optional.ofNullable(dto.password()).ifPresent(pw -> person.setPassword(pw));
        Optional.ofNullable(dto.role()).ifPresent(role -> person.setRole(role));
        Optional.ofNullable(dto.year()).ifPresent(year -> person.setYear(year));
        rep.save(person);
        return person.getId();
    }

    @Transactional
    public void update(int id, Person newPerson) {
        newPerson.setId(id);
        Optional<Person> oldPerson = rep.findById(id);
        newPerson.setPassword(oldPerson.get().getPassword());
        newPerson.setRole(oldPerson.get().getRole());
        rep.save(newPerson);
    }

    @Transactional
    public void delete(int id) {
        rep.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = rep.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long timeTaken = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (timeTaken > 864000000) {
                    book.setExpired(true);
                }
            });
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public PersonRecord getRecordById(Integer id) {
        return rep.findPersonRecordById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = rep.findByName(username);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new PersonDetails(person.get());
    }
}
