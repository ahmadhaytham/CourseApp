package CourseApp.CourseApp.Entity;

import jakarta.persistence.*;

@Entity
public class Assessment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}