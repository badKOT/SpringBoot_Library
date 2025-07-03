package project.springboot.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@ToString
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private Integer year;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Book> books;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Person() {
    }
}
