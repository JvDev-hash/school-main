package br.com.alura.school.section;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.school.video.Video;

public class SectionResponse {
    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String authorUsername;

    @JsonProperty
    private final String courseCode;

    @JsonProperty
    private final List<Video> videoList;

    SectionResponse(Section section){
        this.code = section.getCode();
        this.title = section.getTitle();
        this.authorUsername = section.getAuthorUsername();
        this.courseCode = section.getCourseCode();
        this.videoList = section.getVideos();

    }
}
