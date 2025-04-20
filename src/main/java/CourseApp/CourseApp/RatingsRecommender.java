package CourseApp.CourseApp;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
@Primary
@Component
public class RatingsRecommender implements CourseRecommender{
    @Override
    public List<Course> recommendCourses() {
        return List.of(new Course(1,"Angular","An Angular Course",4.7,350));
    }
}
