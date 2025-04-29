package CourseApp.CourseApp.Repository;

import CourseApp.CourseApp.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CourseRepository extends JpaRepository<Course, Long>{
    Page<Course> findAll(Pageable pageable);
}