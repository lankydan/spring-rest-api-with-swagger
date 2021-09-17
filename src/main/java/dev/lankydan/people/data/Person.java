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
