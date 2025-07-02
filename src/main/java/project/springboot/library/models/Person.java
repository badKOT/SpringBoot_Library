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
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Book> books;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Version
    private int version;

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public Person() {
    }
}
