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
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

@RestController
public class EnrollmentController {

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final UserRepository userRepository;

    EnrollmentController(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository){
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> newEnrollment(@PathVariable String courseCode, @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {
        Course actualCourse = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Course not Found"));
        User actualUser = userRepository.findByUsername(newEnrollmentRequest.getStudentUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not Found"));
        List<Course> finalList = new ArrayList<>();

        if(actualCourse != null){
            for(Enrollment enroll : actualCourse.getEnrollments()){
                if(enroll.getUsername().equals(newEnrollmentRequest.getStudentUsername())){
                    throw new ResponseStatusException(BAD_REQUEST, "User already enrolled on this course");
                }
            }

            finalList.add(actualCourse);
        }

        enrollmentRepository.save(newEnrollmentRequest.toEntity(finalList, new Date()));
        return ResponseEntity.status(CREATED).build();

    }
}
