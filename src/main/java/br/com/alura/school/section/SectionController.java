package br.com.alura.school.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SectionController {
    
    private final SectionRepository sectionRepository;

    SectionController(SectionRepository sectionRepository){
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/sections")
    ResponseEntity<List<SectionResponse>> allSections() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sections/{code}")
    ResponseEntity<SectionResponse> sectionByCode(@PathVariable("code") String code) {
        Section section = sectionRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code)));
        return ResponseEntity.ok(new SectionResponse(section));
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newCourse(@PathVariable String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {
        sectionRepository.save(newSectionRequest.toEntity(code));
        URI location = URI.create(format("/sections/%s", newSectionRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
}
