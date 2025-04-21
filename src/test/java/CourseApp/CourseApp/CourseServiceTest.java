package CourseApp.CourseApp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void getCourseById() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        CourseDto courseDto = new CourseDto();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.getCourseById(courseId);


        assertEquals(courseDto, result);
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, times(1)).toDto(course);
    }

    void getCourseById_nonExistingId() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(courseId));
        verify(courseRepository, times(1)).findById(courseId);
        verify(courseMapper, never()).toDto(any(Course.class));
    }

    @Test
    void createCourse() {
        // Arrange
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Test Course");
        courseDto.setCredit(3);

        Course courseToSave = new Course();
        courseToSave.setName("Test Course");
        courseToSave.setCredit(3);

        Course savedCourse = new Course();
        savedCourse.setId(1);
        savedCourse.setName("Test Course");
        savedCourse.setCredit(3);

        CourseDto savedCourseDto = new CourseDto();
        savedCourseDto.setId(1L);
        savedCourseDto.setName("Test Course");
        savedCourseDto.setCredit(3);

        when(courseMapper.toEntity(courseDto)).thenReturn(courseToSave);
        when(courseRepository.save(courseToSave)).thenReturn(savedCourse);
        when(courseMapper.toDto(savedCourse)).thenReturn(savedCourseDto);

        // Act
        CourseDto result = courseService.createCourse(courseDto);

        // Assert
        assertEquals(savedCourseDto, result);
        verify(courseMapper, times(1)).toEntity(courseDto);
        verify(courseRepository, times(1)).save(courseToSave);
        verify(courseMapper, times(1)).toDto(savedCourse);
        verify(assessmentRepository, never()).findById(any());
        verify(authorRepository, never()).findById(any());
    }

    void deleteCourse() {
        // Arrange
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(courseId);

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        verify(courseRepository, times(1)).existsById(courseId);
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    }
