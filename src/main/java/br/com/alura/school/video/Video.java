package br.com.alura.school.video;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

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

    @ManyToOne
    @JoinColumn(name="section_id", nullable=false)
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

}
