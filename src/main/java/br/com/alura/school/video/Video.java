package br.com.alura.school.video;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.alura.school.section.Section;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = false)
    private String video;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String title;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Section section;

    @Deprecated
    protected Video() { }

    Video(String video, Section section, String title){
        this.video = video;
        this.section = section;
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public Section getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

}
