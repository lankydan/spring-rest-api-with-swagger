package dev.lankydan.people.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "people")
public record Person(
    @Id
    UUID id,
    @Column(name = "first_name")
    String firstName,
    @Column(name = "last_name")
    String lastName
) {
}

//@Entity(name = "people")
//public class Person {
//
//    @Id
//    private UUID id;
//    @Column(name = "first_name")
//    private String firstName;
//    @Column(name = "last_name")
//    private String lastName;
//
//    public Person(UUID id, String firstName, String lastName) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//
//    public Person() {
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    @Override
//    public String toString() {
//        return "Person{" +
//            "id=" + id +
//            ", firstName='" + firstName + '\'' +
//            ", lastName='" + lastName + '\'' +
//            '}';
//    }
//}
