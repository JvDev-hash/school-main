package br.com.alura.school.enrollment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import br.com.alura.school.course.Course;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.List;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max=20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enrollDate;

    @ManyToMany
    @JoinTable(
    name = "course_enrollment",
    joinColumns = @JoinColumn(name = "enrollment_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> coursesEnrolled;

    @Deprecated
    protected Enrollment() { }

    Enrollment(String username, Date enrollDate, List<Course> courses) {
        this.username = username;
        this.enrollDate = enrollDate;
        this.coursesEnrolled = courses;
    }

    public String getUsername() {
        return username;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public List<Course> getCourses() {
        return coursesEnrolled;
    }
}
