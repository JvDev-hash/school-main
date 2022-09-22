package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewSectionRequest {
    @Size(max=30)
    @NotBlank
    @JsonProperty
    private final String code;

    @Size(min=5)
    @NotBlank
    @JsonProperty
    private final String title;

    @JsonProperty
    @NotBlank
    private final String authorUsername;

    public NewSectionRequest(String code, String title, String authorUsername) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    public String getCode() {
        return code;
    }

    Section toEntity(List<Course> courseCode) {
        return new Section(code, title, authorUsername, courseCode);
    }
}
