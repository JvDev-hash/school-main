package br.com.alura.school.video;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class VideoController {

    private final VideoRepository videoRepository;

    private final SectionRepository sectionRepository;

    VideoController(VideoRepository videoRepository, SectionRepository sectionRepository){
        this.videoRepository = videoRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/videos/{title}")
    ResponseEntity<VideoResponse> courseByCode(@PathVariable("title") String title) {
        Video video = videoRepository.findByTitle(title).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Video with title %s not found", title)));
        return ResponseEntity.ok(new VideoResponse(video));
    }

    @PostMapping("/courses/{courseCode}/sections/{sectionCode}")
    ResponseEntity<Void> newVideo(@PathVariable String sectionCode, @PathVariable String courseCode, @RequestBody @Valid NewVideoRequest newVideoRequest) {
        Section section = sectionRepository.findByCourseCodeAndCode(courseCode, sectionCode);

        videoRepository.save(newVideoRequest.toEntity(section));
        URI location = URI.create(format("/videos/%s", newVideoRequest.getTitle()));
        return ResponseEntity.created(location).build();
    }
}
