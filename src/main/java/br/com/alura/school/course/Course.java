package br.com.alura.school.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.section.Section;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max=10)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    @Size(max=20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "courses")
    List<Section> sections;

    @ManyToMany(mappedBy = "coursesEnrolled")
    List<Enrollment> enrollments;

    @Deprecated
    protected Course() { }

    public Course(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    

}
