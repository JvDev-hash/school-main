package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionResponse {
    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String authorUsername;

    @JsonProperty
    private final String courseCode;

    SectionResponse(Section section){
        this.code = section.getCode();
        this.title = section.getTitle();
        this.authorUsername = section.getAuthorUsername();
        this.courseCode = section.getCourseCode();

    }
}
