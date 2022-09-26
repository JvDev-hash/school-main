package br.com.alura.school.video;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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

        if(!actualCourse.getSections().isEmpty()){
            for(Section tempSection : actualCourse.getSections()){
                if(tempSection.getCode().equals(sectionCode)){
                    section = tempSection;
                } else {
                    throw new ResponseStatusException(BAD_REQUEST, "Section not Found");
                }
            }

        } else {
            throw new ResponseStatusException(BAD_REQUEST, "Course does not have sections yet");
        }

        for(Video video : section.getVideos()){
            if(video.getTitle().equals(newVideoRequest.getTitle())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicated video title");
            }
        }

        videoRepository.save(newVideoRequest.toEntity(section));
        URI location = URI.create(format("/videos/%s", newVideoRequest.getTitle()));
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/courses/{courseCode}/sections/{sectionCode}/{videoTitle}")
    ResponseEntity<Void> deleteVideo(@PathVariable String sectionCode, @PathVariable String courseCode, @PathVariable String videoTitle){
        Video targetVideo = videoRepository.findByTitle(videoTitle).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Video not Found"));

        videoRepository.delete(targetVideo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/courses/{courseCode}/sections/{sectionCode}/{videoTitle}")
    ResponseEntity<Void> updateVideo(@PathVariable String videoTitle, @PathVariable String sectionCode, @PathVariable String courseCode, @RequestBody @Valid NewVideoRequest newVideoRequest){
        Course actualCourse = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Course not found"));
        List<Video> tempVideoList = new ArrayList<>();
        Video targetVideo = null;

        for(Section section : actualCourse.getSections()){
            if(section.getCode().equals(sectionCode)){
                tempVideoList = section.getVideos();
            }
        }

        for(Video video : tempVideoList){
            if(video.getTitle().equals(videoTitle)){
                targetVideo = video;
                targetVideo.setTitle(newVideoRequest.getTitle());
                targetVideo.setVideo(newVideoRequest.getVideo());
            }
        }

        videoRepository.save(targetVideo);
        URI location = URI.create(format("/videos/%s", newVideoRequest.getTitle()));
        return ResponseEntity.created(location).build();
    }
}
