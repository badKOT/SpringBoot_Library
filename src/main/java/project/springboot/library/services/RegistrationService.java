package project.springboot.library.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.springboot.library.models.Person;
import project.springboot.library.repositories.PeopleRepository;

//@Service
public class RegistrationService {

    private final PeopleRepository rep;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PeopleRepository rep, PasswordEncoder passwordEncoder) {
        this.rep = rep;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        rep.save(person);
    }
}
