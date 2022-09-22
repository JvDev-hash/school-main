package br.com.alura.school.video;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class VideoController {

    private final VideoRepository videoRepository;


    private final CourseRepository courseRepository;

    VideoController(VideoRepository videoRepository, CourseRepository courseRepository){
        this.videoRepository = videoRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/videos/{title}")
    ResponseEntity<VideoResponse> courseByCode(@PathVariable("title") String title) {
        Video video = videoRepository.findByTitle(title).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Video with title %s not found", title)));
        return ResponseEntity.ok(new VideoResponse(video));
    }

    @PostMapping("/courses/{courseCode}/sections/{sectionCode}")
    ResponseEntity<Void> newVideo(@PathVariable String sectionCode, @PathVariable String courseCode, @RequestBody @Valid NewVideoRequest newVideoRequest) {
        Course actualCourse = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Course not Found"));
        Section section = null;

        for(Section tempSection : actualCourse.getSections()){
            if(tempSection.getCode().equals(sectionCode)){
                section = tempSection;
            }
        }

        videoRepository.save(newVideoRequest.toEntity(section));
        URI location = URI.create(format("/videos/%s", newVideoRequest.getTitle()));
        return ResponseEntity.created(location).build();
    }
}
