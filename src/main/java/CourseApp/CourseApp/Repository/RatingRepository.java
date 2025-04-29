package CourseApp.CourseApp.Repository;

import CourseApp.CourseApp.Entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {}