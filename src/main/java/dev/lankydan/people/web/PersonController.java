package dev.lankydan.people.web;

import dev.lankydan.people.data.Person;
import dev.lankydan.people.data.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<Person>> all() {
        return ResponseEntity.ok(personRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> get(@PathVariable("id") UUID id) {
        return personRepository.findById(id).map(ResponseEntity::ok)
            .orElseThrow(() -> new NoSuchElementException("Person with id: " + id + " does not exist"));
    }

    @PostMapping
    public ResponseEntity<Person> post(@RequestBody Person person) {
        Person saved = personRepository.save(
            new Person(
                UUID.randomUUID(),
                person.getFirstName(),
                person.getLastName()
            )
        );
        URI uri =
            MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> put(@PathVariable("id") UUID id, @RequestBody Person person) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            Person saved = personRepository.save(new Person(id, person.getFirstName(), person.getLastName()));
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            return ResponseEntity.created(uri).body(saved);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        if(!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
