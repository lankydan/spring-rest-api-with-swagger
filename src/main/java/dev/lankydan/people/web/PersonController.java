package dev.lankydan.people.web;

import dev.lankydan.people.data.Person;
import dev.lankydan.people.data.PersonRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/people")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController {

    private final PersonRepository personRepository;

    PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    @Operation(
        summary = "Finds all people",
        description = "Finds all people.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = Person.class))
                    )
                }
            ),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    // Can use @ResponseStatus to have an enum version of the response code?
    public ResponseEntity<Iterable<Person>> all() {
        return ResponseEntity.ok(personRepository.findAll());
    }

    // Creates the example response without any annotations
    // Still figures it out even without defining the content property of @ApiResponse
    // But without the annotation I can't set the media type?
    @GetMapping("/{id}")
    @Operation(
        summary = "Finds a person",
        description = "Finds a person by their Id.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Success",
                responseCode = "200",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            // Need the empty content otherwise it fills it with the example person schema
            // Setting empty content also hides the box in the swagger ui
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<Person> get(@PathVariable("id") @Parameter(description = "The Id of the person to find.") UUID id) {
        return personRepository.findById(id).map(ResponseEntity::ok)
            .orElseThrow(() -> new NoSuchElementException("Person with id: " + id + " does not exist"));
    }

    //    @PostMapping
//    @Operation(
//        summary = "Adds a new person",
//        description = "Adds a new person by passing in a JSON representation of the person.",
//        tags = { "People" }
//    )
//    @ApiResponses({
//        @ApiResponse(
//            description = "Created",
//            responseCode = "201",
//            links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id")),
//            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
//        ),
//        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
//    })
    // Seems that @ApiResponses doesn't work when trying to create the response links
    // however there is a `responses` property on the `@Operation` annotation
    @PostMapping
    @Operation(
        summary = "Adds a new person",
        description = "Adds a new person by passing in a JSON representation of the person.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Created",
                responseCode = "201",
                links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<Person> post(@RequestBody Person person) {
        Person saved = personRepository.save(
            new Person(
                UUID.randomUUID(),
                person.firstName(),
                person.lastName()
            )
        );
        URI uri =
            MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Updates a person's information",
        description = "Updates a person's information by passing in their Id and a JSON representation of the updated person.",
        tags = { "People" },
        responses = {
            @ApiResponse(
                description = "Updated",
                responseCode = "200",
                links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class))
            ),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<Person> put(
        @PathVariable("id") @Parameter(description = "The Id of the person to update.") UUID id,
        @RequestBody Person person
    ) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            Person saved = personRepository.save(new Person(id, person.firstName(), person.lastName()));
            return ResponseEntity.ok(saved);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletes a person",
        description = "Deletes a person by their Id.",
        tags = { "People" },
        responses = {
            @ApiResponse(description = "Deleted", responseCode = "204", content = @Content),
            @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
        }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") @Parameter(description = "The Id of the person to delete.") UUID id) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        } else {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
