package br.com.alura.school.section;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.school.course.Course;
import br.com.alura.school.video.Video;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Section {

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

    @ManyToMany
    @JoinTable(
    name = "course_section",
    joinColumns = @JoinColumn(name = "section_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id"))
    List<Course> courses;

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Video> videos;

    @Deprecated
    protected Section() { }

    Section(String code, String title, String authorUsername, List<Course> courses) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
        this.courses = courses;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorUsername(){
        return authorUsername;
    }

    List<Course> getCourses(){
        return courses;
    }

    public List<Video> getVideos(){
        return videos;
    }

    List<String> getCoursesCodesList(){
        List<String> codesList = new ArrayList<>();

        for(Course course : courses){
            codesList.add(course.getCode());
        }

        return codesList;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

}
