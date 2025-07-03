package project.springboot.library.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.springboot.library.dto.PersonRecord;
import project.springboot.library.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByName(String name);

    @Query("""
            SELECT new project.springboot.library.dto.PersonRecord(
                p.id, p.name, p.year, p.password, p.role, p.version,
                COUNT(b), COALESCE(SUM(b.pages), 0), COALESCE(SUM(b.price), 0)
            ) FROM Person p
            LEFT JOIN p.books b
            WHERE p.id = :id
            GROUP BY p
            """)
    Optional<PersonRecord> findPersonRecordById(Integer id);
}
