package br.com.alura.school.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        userRepository.save(new User("victor", "victor@email.com"));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("victor");

        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_not_allow_duplication_of_enroll() throws Exception {
        courseRepository.save(new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC."));
        userRepository.save(new User("joao", "joao@email.com"));

        Course actualCourse = courseRepository.findByCode("spring-1").orElseThrow();
        List<Course> finalList = new ArrayList<>();
        finalList.add(actualCourse);
        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("joao");
        enrollmentRepository.save(newEnroll.toEntity(finalList, new Date()));

        NewEnrollmentRequest newEnrollTest = new NewEnrollmentRequest("joao");

        mockMvc.perform(post("/courses/spring-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnrollTest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_empty_username() throws Exception {

        courseRepository.save(new Course("spring-2", "Spring Boot", "Spring Boot"));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("");

        mockMvc.perform(post("/courses/spring-2/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_inexistent_username() throws Exception {
        courseRepository.save(new Course("java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more."));

        NewEnrollmentRequest newEnroll = new NewEnrollmentRequest("juliana");

        mockMvc.perform(post("/courses/java-2/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newEnroll)))
                .andExpect(status().isNotFound());
    }

}
