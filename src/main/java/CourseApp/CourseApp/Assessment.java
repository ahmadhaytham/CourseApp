package CourseApp.CourseApp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Assessment {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
}