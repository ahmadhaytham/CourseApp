package CourseApp.CourseApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    CourseRecommender recommender;

    @Autowired //Here precedence is given using Primary
    public CourseService(CourseRecommender recommender) {
        this.recommender = recommender;
    }

//    @Autowired Here precedence is given using Qualifier
    //@Qualifier("DescriptionQualifier")
//    public CourseService(CourseRecommender recommender) {
//        this.recommender = recommender;
//    }





    public List<Course> recommendCourses() {
        return recommender.recommendCourses();
    }

}
