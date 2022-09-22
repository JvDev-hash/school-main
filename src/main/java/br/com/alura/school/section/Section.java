package br.com.alura.school.section;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max=30)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    @Size(min=5)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String authorUsername;

    @NotBlank
    @Column(nullable = false)
    private String courseCode;

    @Deprecated
    protected Section() { }

    Section(String code, String title, String authorUsername, String courseCode) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
        this.courseCode = courseCode;
    }

    String getCode() {
        return code;
    }

    String getTitle() {
        return title;
    }

    String getAuthorUsername(){
        return authorUsername;
    }

    String getCourseCode(){
        return courseCode;
    }

}
