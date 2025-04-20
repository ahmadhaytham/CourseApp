package CourseApp.CourseApp;

import jakarta.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue
    private Long id;
    private int number;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
