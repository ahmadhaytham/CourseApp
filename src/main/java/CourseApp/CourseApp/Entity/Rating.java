package CourseApp.CourseApp.Entity;

import jakarta.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue
    private Long id;
    private int number;

    @ManyToOne
    @JoinColumn(name = "course_id") // Foreign key column in Rating table
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
