package CourseApp.CourseApp;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping
    public CourseDto createCourse(@RequestBody CourseDto dto) {
        return courseService.createCourse(dto);
    }

    @PutMapping("/{id}")
    public CourseDto updateCourse(@PathVariable Long id, @RequestBody CourseDto dto) {
        return courseService.updateCourse(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

//    @Operation(summary = "View a course by ID")
//    @GetMapping("/{id}")
//    public ResponseEntity<Course> viewCourse(@PathVariable int id) {
//        Course course = courseService.getCourse(id);
//        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
//    }
//
//    @Operation(summary = "Add a new course")
//    @PostMapping
//    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
//        if(course==null){
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//        Course added = courseService.addCourse(course);
//        return ResponseEntity.status(HttpStatus.CREATED).body(added);
//    }
//
//    @Operation(summary = "Update a course by ID")
//    @PutMapping("/{id}")
//    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
//        Course updated = courseService.updateCourse(course);
//        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
//    }
//
//    @Operation(summary = "Delete a course by ID")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
//        return courseService.deleteCourse(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
//    }
}
