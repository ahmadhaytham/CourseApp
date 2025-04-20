package CourseApp.CourseApp;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    CourseRecommender recommender;
    private HashMap<Integer,Course>courses;
    private final AtomicInteger idGenerator = new AtomicInteger();
    @Autowired //Here precedence is given using Primary
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, CourseRecommender recommender) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.recommender = recommender;
        courses = new HashMap<>();

    }

//    @Autowired Here precedence is given using Qualifier
    //@Qualifier("DescriptionQualifier")
//    public CourseService(CourseRecommender recommender) {
//        this.recommender = recommender;
//    }


    public List<Course> recommendCourses() {
        return recommender.recommendCourses();
    }

    public CourseDto getCourseById(Long id) {
        return courseMapper.toDto(courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found")));
    }

    public CourseDto createCourse(CourseDto dto) {
        Course course = courseMapper.toEntity(dto);
        return courseMapper.toDto(courseRepository.save(course));
    }

    public CourseDto updateCourse(Long id, CourseDto dto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCredit(dto.getCredit());
        return courseMapper.toDto(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

//    public Course addCourse(Course course){
//        int id=idGenerator.incrementAndGet();
//        course.setId(id);
//        courses.put(id,course);
//        return course;
//    }
//
//    public Course updateCourse(Course course){
//        int id=course.getId();
//        if(!courses.containsKey(id)){
//            return null;
//        }
//        else{
//            return courses.put(id,course);
//        }
//    }
//
//    public Course getCourse(int id){
//        return courses.get(id);
//    }
//
//    public boolean deleteCourse(int id){
//        if(courses.containsKey(id)){
//            courses.remove(id);
//            return true;
//        }
//        return false;
//    }



}
