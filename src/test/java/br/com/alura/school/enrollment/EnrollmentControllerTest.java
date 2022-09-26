package br.com.alura.school.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_add_new_enroll() throws Exception {
        courseRepository.save(new Course("java-9", "Java OO-new", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        userRepository.save(new User("victor", "victor@email.com"));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("victor");

        mockMvc.perform(post("/courses/java-9/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_allow_duplication_of_enroll() throws Exception {
        courseRepository.save(new Course("spring-7", "Spring Basics-new", "Spring Core and Spring MVC."));
        userRepository.save(new User("joao", "joao@email.com"));

        Course actualCourse = courseRepository.findByCode("spring-7").orElseThrow();
        List<Course> finalList = new ArrayList<>();
        finalList.add(actualCourse);
        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("joao");
        enrollmentRepository.save(newEnroll.toEntity(finalList, new Date()));

        NewEnrollmentRequest newEnrollTest = new NewEnrollmentRequest("joao");

        mockMvc.perform(post("/courses/spring-7/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_empty_username() throws Exception {

        courseRepository.save(new Course("spring-5", "Spring Boot-new", "Spring Boot"));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("");

        mockMvc.perform(post("/courses/spring-5/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_inexistent_username() throws Exception {
        courseRepository.save(new Course("java-8", "Java Collections-new", "Java Collections: Lists, Sets, Maps and more."));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("juliana");

        mockMvc.perform(post("/courses/java-8/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isNotFound());
    }

}
