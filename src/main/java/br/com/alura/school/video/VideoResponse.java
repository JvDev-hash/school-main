package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoResponse {
    @JsonProperty
    private final String video;

    @JsonProperty
    private final String title;

    VideoResponse(Video video){
        this.video = video.getVideo();
        this.title = video.getTitle();
    }
}
