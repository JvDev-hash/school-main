package br.com.alura.school.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.CONFLICT;

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
        List<Section> allSectionsList = sectionRepository.findAll();
        List<SectionResponse> allSectionsReturn = new ArrayList<>();

        if(allSectionsList.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, "No Scetions have been registered yet");
         } else {
            for(Section section : allSectionsList){
                allSectionsReturn.add(new SectionResponse(section));
            }
        }
        return ResponseEntity.ok(allSectionsReturn);
    }

    @GetMapping("/sections/{code}")
    ResponseEntity<List<SectionResponse>> sectionByCode(@PathVariable("code") String code) {
        List<Section> sectionsList = sectionRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code)));
        List<SectionResponse> sectionsReturn = new ArrayList<>();

        if(sectionsList.isEmpty()){
           throw new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code));
        } else {
            for(Section section : sectionsList){
                sectionsReturn.add(new SectionResponse(section));
            }
        }
        return ResponseEntity.ok(sectionsReturn);
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newSection(@PathVariable String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {
        Course actualCourse = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Courses not found"));
        List<Course> tempCourseList = new ArrayList<>();
        tempCourseList.add(actualCourse);

        Section tempSection = newSectionRequest.toEntity(tempCourseList);
        boolean testSection = false;

        if(tempCourseList.isEmpty()){
            throw new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code));
         } else {
        for(Section section: actualCourse.getSections()){
            if(section.getCode().equals(tempSection.getCode())){
                testSection = true;
                break;
            }
        }
    }

        User tempUser = userRepository.findByUsername(tempSection.getAuthorUsername())
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", tempSection.getAuthorUsername())));

        if(!tempUser.getUserRole().equals("INSTRUCTOR")){
            throw new ResponseStatusException(UNAUTHORIZED, format("User with code %s is not a Instructor", tempUser.getUsername()));
        } else if(testSection == true) {
            throw new ResponseStatusException(CONFLICT, "Duplicated section Code");
        } else {
            sectionRepository.save(newSectionRequest.toEntity(tempCourseList));
            URI location = URI.create(format("/sections/%s", newSectionRequest.getCode()));
            return ResponseEntity.created(location).build();
        }
    }

    @DeleteMapping("/courses/{code}/sections/{sectionCode}")
    ResponseEntity<Void> deleteSection(@PathVariable String code, @PathVariable String sectionCode){
        Course actualCourse = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Course not found"));

        if(!actualCourse.getSections().isEmpty()){
            for(Section section: actualCourse.getSections()){
                if(section.getCode().equals(sectionCode)){
                    sectionRepository.delete(section);
                }
            }
        } else {
            throw new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", sectionCode));
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{code}/sections/{sectionCode}")
    ResponseEntity<Void> updateSection(@PathVariable String code, @PathVariable String sectionCode, @RequestBody @Valid NewSectionRequest newSectionRequest) {
        Course actualCourse = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Courses not found"));
        Section targetSection = null;

        if(!actualCourse.getSections().isEmpty()){
            for(Section section: actualCourse.getSections()){
                if(section.getCode().equals(sectionCode)){
                    targetSection = section;
                    targetSection.setAuthorUsername(newSectionRequest.getAuthorUsername());
                    targetSection.setCode(newSectionRequest.getCode());
                    targetSection.setTitle(newSectionRequest.getTitle());
                }
            }
        } else {
            throw new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", sectionCode));
        }

        User tempUser = userRepository.findByUsername(newSectionRequest.getAuthorUsername())
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", newSectionRequest.getAuthorUsername())));

        if(!tempUser.getUserRole().equals("INSTRUCTOR")){
            throw new ResponseStatusException(UNAUTHORIZED, format("User with code %s is not a Instructor", tempUser.getUsername()));
        } else{
            sectionRepository.save(targetSection);
            URI location = URI.create(format("/sections/%s", newSectionRequest.getCode()));
            return ResponseEntity.created(location).build();
        }
    }
}
