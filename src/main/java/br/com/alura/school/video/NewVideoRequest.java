package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

import br.com.alura.school.section.Section;
import br.com.alura.school.support.validation.Unique;

public class NewVideoRequest {
        @NotBlank
        @JsonProperty
        private final String video;

        @Unique(entity = Video.class, field = "title")
        @NotBlank
        @JsonProperty
        private final String title;

        public NewVideoRequest(String video, String title) {
            this.video = video;
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public String getTitle() {
            return title;
        }

        Video toEntity(Section section) {
            return new Video(video, section, title);
        }
}
