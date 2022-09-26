package br.com.alura.school.reports;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.video.VideoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportsControllerTest {

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
    void should_return_no_content() throws Exception {
        courseRepository.save(new Course("spring-10", "Spring Basics-novo", "Spring Core and Spring MVC."));
        courseRepository.save(new Course("spring-11", "Spring Boot-att", "Spring Boot"));

        mockMvc.perform(get("/sectionByVideosReport")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

}
