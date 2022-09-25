package br.com.alura.school.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SectionByVideosReport {

    @JsonProperty
    private final String courseName;

    @JsonProperty
    private final String sectionTitle;

    @JsonProperty
    private final String authorName;

    @JsonProperty
    private final Integer totalVideos;

    SectionByVideosReport(String courseName, String sectionTitle, String authorName, Integer totalVideos){
        this.authorName = authorName;
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.totalVideos = totalVideos;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }

}
