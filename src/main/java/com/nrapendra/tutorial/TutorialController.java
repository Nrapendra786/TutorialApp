package com.nrapendra.tutorial;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/tutorials")
@Slf4j
@AllArgsConstructor
public class TutorialController {

    private TutorialRepository repository;

    @GetMapping("/all/")
    ResponseEntity<List<Tutorial>> all() {
        return new ResponseEntity<>(repository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Tutorial> one(@PathVariable Long id) {

        Tutorial tutorial = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(HttpStatus.NOT_FOUND,
                                    "Not found Tutorial with id = " + id));
        return new ResponseEntity<>(tutorial, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Optional<Tutorial>> update(@PathVariable Long id, @RequestBody Tutorial tutorial) {
        try {
            Optional.ofNullable(repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(HttpStatus.NOT_FOUND,"tutorial is not found")));
            repository.saveAndFlush(tutorial);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    ResponseEntity<Tutorial> create(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = repository.save(tutorial);
            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Tutorial> delete(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/")
    ResponseEntity<Tutorial> deleteAll() {
        try {
            repository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/published/")
    ResponseEntity<List<Tutorial>> published(@PathVariable Long id) {
        try {
            List<Tutorial> tutorials = repository.findByPublished("Physics");
            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
