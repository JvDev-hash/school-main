package br.com.alura.school.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import  br.com.alura.school.user.*;
import  br.com.alura.school.course.*;

@RestController
public class SectionController {
    
    private final SectionRepository sectionRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    SectionController(SectionRepository sectionRepository, UserRepository userRepository, CourseRepository courseRepository){
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
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
        Section tempSection = newSectionRequest.toEntity(code);

        Course tempCourse = courseRepository.findByCode(tempSection.getCourseCode()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", tempSection.getCourseCode())));
        User tempUser = userRepository.findByUsername(tempSection.getAuthorUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", tempSection.getAuthorUsername())));

        if(!tempUser.getUserRole().equals("INSTRUCTOR")){
            throw new ResponseStatusException(UNAUTHORIZED, format("User with code %s is not a Instructor", tempUser.getUsername()));
        } else {
            sectionRepository.save(newSectionRequest.toEntity(code));
            URI location = URI.create(format("/sections/%s", newSectionRequest.getCode()));
            return ResponseEntity.created(location).build();
        }
    }
}
