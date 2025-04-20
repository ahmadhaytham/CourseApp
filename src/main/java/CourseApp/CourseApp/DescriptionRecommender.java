package CourseApp.CourseApp;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class DescriptionRecommender implements CourseRecommender{

    @Override
    public List<Course> recommendCourses() {
        return List.of(new Course(1,"Spring","A spring Course",new Rating()));
    }
}
