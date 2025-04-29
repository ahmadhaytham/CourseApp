package CourseApp.CourseApp.Service;

import CourseApp.CourseApp.CourseRecommender;
import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Entity.Course;
import CourseApp.CourseApp.Mapper.AuthorMapper;
import CourseApp.CourseApp.Mapper.CourseMapper;
import CourseApp.CourseApp.Repository.AuthorRepository;
import CourseApp.CourseApp.Repository.CourseRepository;
import CourseApp.CourseApp.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CourseService {
    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    CourseRecommender recommender;

    private final AtomicInteger idGenerator = new AtomicInteger();
    @Autowired //Here precedence is given using Primary
    public CourseService(AuthorMapper authorMapper, AuthorRepository authorRepository, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
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

    @Transactional
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courseMapper.coursePageToCourseDTOPage(courses, pageable);
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return courseMapper.toDto(course);
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

    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Optional<AuthorDto> getAuthorByEmail(String email) {
        return authorRepository.findByEmail(email)
                .map(authorMapper::toDto);
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
