package project.springboot.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springboot.library.models.Book;
import project.springboot.library.models.Person;
import project.springboot.library.repositories.PeopleRepository;
import project.springboot.library.security.PersonDetails;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public void save(Person person) {
        rep.save(person);
    }

    @Transactional
    public void update(int id, Person newPerson) {
        newPerson.setId(id);
        rep.save(newPerson);
    }

    @Transactional
    public void delete(int id) { rep.deleteById(id); }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = rep.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long timeTaken = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (timeTaken > 864000000)
                    book.setExpired(true);
            });
            return person.get().getBooks();
        } else
            return Collections.emptyList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = rep.findByName(username);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new PersonDetails(person.get());
    }
}