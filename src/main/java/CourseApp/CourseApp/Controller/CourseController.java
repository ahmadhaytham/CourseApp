package CourseApp.CourseApp.Controller;

import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Service.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course API", description = "Manage courses and recommendations")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CourseDto> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/course-id/{id}")
    public CourseDto getCourse(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("/createCourse")
    public CourseDto createCourse(@RequestBody CourseDto dto) {
        return courseService.createCourse(dto);
    }

    @PutMapping("/updateCourse/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @RequestBody CourseDto dto) {
        return courseService.updateCourse(id, dto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

}
