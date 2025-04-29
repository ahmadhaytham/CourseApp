package CourseApp.CourseApp;

import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Entity.Author;
import CourseApp.CourseApp.Entity.Course;
import CourseApp.CourseApp.Mapper.AuthorMapper;
import CourseApp.CourseApp.Mapper.CourseMapper;
import CourseApp.CourseApp.Repository.AssessmentRepository;
import CourseApp.CourseApp.Repository.AuthorRepository;
import CourseApp.CourseApp.Repository.CourseRepository;
import CourseApp.CourseApp.Service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private AuthorMapper authorMapper;

    @InjectMocks
    private CourseService courseService;

    private Author author;
    private AuthorDto authorDto; // Renamed DTO to Dto
    private Course course;
    private CourseDto courseDto; // Renamed DTO to Dto
    private Page<Course> coursePage;
    private Page<CourseDto> courseDtoPage; // Renamed DTO to Dto
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Sample Data
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");
        author.setEmail("john.doe@example.com");
        author.setBirthdate(LocalDate.of(1990, 5, 15));

        authorDto = new AuthorDto(); // Renamed DTO to Dto
        authorDto.setId(1L);
        authorDto.setName("John Doe");
        authorDto.setEmail("john.doe@example.com");
        authorDto.setBirthdate(LocalDate.of(1990, 5, 15));


        course = new Course();
        course.setId(1L);
        course.setName("Test Course");
        course.setDescription("Description");
        course.setCredit(3);
        course.setAuthor(author);

        courseDto = new CourseDto(); // Renamed DTO to Dto
        courseDto.setId(1L);
        courseDto.setName("Test Course");
        courseDto.setDescription("Description");
        courseDto.setCredit(3);
        courseDto.setAuthor(authorDto); // Renamed DTO to Dto

        pageable = PageRequest.of(0, 10);
        coursePage = new PageImpl<>(Collections.singletonList(course), pageable, 1);
        courseDtoPage = new PageImpl<>(Collections.singletonList(courseDto), pageable, 1); // Renamed DTO to Dto

        // Mock Mapper behavior for common cases
        when(courseMapper.toDto(any(Course.class))).thenReturn(courseDto); // Renamed DTO to Dto
        when(courseMapper.toEntity(any(CourseDto.class))).thenReturn(course); // Renamed DTO to Dto
        when(authorMapper.toDto(any(Author.class))).thenReturn(authorDto); // Renamed DTO to Dto
        when(authorMapper.toEntity(any(AuthorDto.class))).thenReturn(author); // Renamed DTO to Dto
        when(courseMapper.coursePageToCourseDTOPage(any(Page.class), any(Pageable.class))).thenReturn(courseDtoPage); // Renamed DTO to Dto
    }

    @Test
    void testGetAllCourses_ReturnsPagedDto() { // Renamed
        when(courseRepository.findAll(pageable)).thenReturn(coursePage);

        Page<CourseDto> result = courseService.getAllCourses(pageable); // Renamed DTO to Dto

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(courseDto.getId(), result.getContent().get(0).getId()); // Renamed DTO to Dto

        verify(courseRepository).findAll(pageable);
        verify(courseMapper).coursePageToCourseDTOPage(coursePage, pageable); // Renamed DTO to Dto
    }

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
    void testGetCourseById_Found_ReturnsDto() { // Renamed
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourseById(1L); // Renamed DTO to Dto

        assertNotNull(result);
        assertEquals(courseDto.getId(), result.getId()); // Renamed DTO to Dto

        verify(courseRepository).findById(1L);
        verify(courseMapper).toDto(course); // Renamed DTO to Dto
    }

    @Test
    void testGetCourseById_NotFound_ThrowsException() { // Renamed
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(2L));

        verify(courseRepository).findById(2L);
        verify(courseMapper, never()).toDto(any(Course.class)); // Renamed DTO to Dto
    }

    @Test
    void testUpdateCourse_NotFound_ThrowsException() { // Renamed
        CourseDto updateCourseDto = new CourseDto(); // Doesn't matter what's in the DTO for this test // Renamed DTO to Dto

        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(2L, updateCourseDto)); // Renamed DTO to Dto

        verify(courseRepository).findById(2L);
        verify(authorRepository, never()).findById(anyLong()); // Ensure author lookup is not called
        verify(courseRepository, never()).save(any(Course.class)); // Ensure save is not called
        verify(courseMapper, never()).toDto(any(Course.class)); // Ensure mapper is not called // Renamed DTO to Dto
    }

    @Test
    void testUpdateCourse_AuthorNotFound_ThrowsException() { // Renamed
        CourseDto updateCourseDto = new CourseDto(); // Renamed DTO to Dto
        updateCourseDto.setName("Updated Name");
        updateCourseDto.setDescription("Updated Description");
        updateCourseDto.setCredit(5);
        AuthorDto nonExistentAuthorDto = new AuthorDto(); // Renamed DTO to Dto
        nonExistentAuthorDto.setId(99L); // Non-existent author ID
        updateCourseDto.setAuthor(nonExistentAuthorDto); // Renamed DTO to Dto

        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setName("Original Name");
        existingCourse.setDescription("Original Description");
        existingCourse.setCredit(3);
        existingCourse.setAuthor(author); // Assume original author exists

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(authorRepository.findById(99L)).thenReturn(Optional.empty()); // Mock author not found


        assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(1L, updateCourseDto)); // Renamed DTO to Dto


        verify(courseRepository).findById(1L);
        verify(authorRepository).findById(99L);
        verify(courseRepository, never()).save(any(Course.class)); // Ensure save is not called
        verify(courseMapper, never()).toDto(any(Course.class)); // Ensure mapper is not called // Renamed DTO to Dto
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
        savedCourse.setId(1L);
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
