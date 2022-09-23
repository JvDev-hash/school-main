package br.com.alura.school.enrollment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.CONFLICT;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.SectionRepository;

@RestController
public class EnrollmentController {

    private final SectionRepository sectionRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    EnrollmentController(SectionRepository sectionRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository){
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> newEnrollment(@PathVariable String courseCode, @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {
        Course actualCourse = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Course not Found"));
        List<Course> finalList = new ArrayList<>();

        if(actualCourse != null){
            finalList.add(actualCourse);
        }

        enrollmentRepository.save(newEnrollmentRequest.toEntity(finalList, new Date()));
        return ResponseEntity.status(CREATED).build();

    }
}
