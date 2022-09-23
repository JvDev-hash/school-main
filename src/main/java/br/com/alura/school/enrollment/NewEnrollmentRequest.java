package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class NewEnrollmentRequest {

    @JsonProperty
    @NotBlank
    private final String studentUsername;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public NewEnrollmentRequest (String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    Enrollment toEntity(List<Course> courseCode, Date enrollDate) {
        return new Enrollment(studentUsername, enrollDate, courseCode);
    }
}
