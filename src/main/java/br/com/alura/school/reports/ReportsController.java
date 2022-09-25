package br.com.alura.school.reports;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class ReportsController {

    private final CourseRepository courseRepository;

    ReportsController(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    @GetMapping("/sectionByVideosReport")
    ResponseEntity<List<SectionByVideosReport>> sectionByVideos() {

        List<Course> coursesList = courseRepository.findAll();
        List<SectionByVideosReport> finalReport = new ArrayList<>();
        boolean hasEnrollments = false;

        if(coursesList != null){
            for(Course course : coursesList){
                if(!course.getEnrollments().isEmpty()){
                    hasEnrollments = true;

                    for(Section section : course.getSections()){

                        finalReport.add(new SectionByVideosReport(course.getName(), section.getCode(), section.getAuthorUsername(), section.getVideos().size()));
                    }
                }

            }

            if(hasEnrollments == false){ throw new ResponseStatusException(NO_CONTENT, "Does not have enrollments yet"); }

        } else {

            throw new ResponseStatusException(NOT_FOUND, "Does not have courses yet");
        }

        Collections.sort(finalReport, Collections.reverseOrder(new Comparator<SectionByVideosReport>() {
            @Override
            public int compare(SectionByVideosReport o1, SectionByVideosReport o2) {
                return o1.getTotalVideos().compareTo(o2.getTotalVideos());
            }
        }));

        return ResponseEntity.ok(finalReport);
    }
}
