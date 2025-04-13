package CourseApp.CourseApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceVarName {
    @Autowired
    private CourseRecommender RatingsRecommender;

    public List<Course> recommendCourses() {
        return RatingsRecommender.recommendCourses();
    }
}
