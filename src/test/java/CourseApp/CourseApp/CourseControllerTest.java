package CourseApp.CourseApp;

import CourseApp.CourseApp.Controller.CourseController;
import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper; // Import ObjectMapper
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Import AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean; // Keep using MockBean for @WebMvcTest
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType; // Import MediaType
import org.springframework.http.RequestEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc; // Import MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder; // <-- Added explicit import for the builder type

// Removed unused imports for other test methods
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any; // Import any
// Removed unused imports for other test methods
// import static org.mockito.ArgumentMatchers.anyLong;
// Removed unused imports for other test methods
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.doThrow;
// Removed unused imports for other test methods
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
// Removed unused imports for other test methods
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Import jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CourseController.class) // Specify the CourseController
@AutoConfigureMockMvc // Explicitly configure MockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc; // Autowire MockMvc

    @Autowired
    private ObjectMapper objectMapper; // Autowire ObjectMapper for JSON conversion

    // Use @MockBean to create a mock of the concrete CourseService class
    // and register it as a bean in the Spring test context.
    // This mock will be injected into the CourseController.
    @MockBean // <-- Ensure this is present and correctly references the concrete class
    private CourseService courseService; // <-- The type must match the bean you want to mock

    // --- Test Method for Create Course ---

    @Test
    void testCreateCourse_ReturnsCreated() throws Exception {
        // Simple test for POST /api/courses/createCourse
        // Arrange: Create an input DTO and an expected output DTO (with ID)
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L); // Assuming author exists

        CourseDto inputDto = new CourseDto();
        inputDto.setName("New Test Course");
        inputDto.setDescription("Description");
        inputDto.setCredit(3);
        inputDto.setAuthor(authorDto);

        CourseDto createdDto = new CourseDto();
        createdDto.setId(10L); // Simulate database generated ID
        createdDto.setName("New Test Course");
        createdDto.setDescription("Description");
        createdDto.setCredit(3);
        createdDto.setAuthor(authorDto);

        // Mock the service behavior: when createCourse is called with any DTO, return the created DTO
        when(courseService.createCourse(any(CourseDto.class))).thenReturn(createdDto);

        // Act: Perform the POST request using the standard fluent chain
        mockMvc.perform(post("/api/courses/createCourse") // Use the standard post method
                        .contentType(MediaType.APPLICATION_JSON) // Set content type to JSON
                        .content(objectMapper.writeValueAsString(inputDto))) // Convert DTO to JSON string (Requires Getters in DTOs)
                // Assert: Verify the response
                .andExpect(status().isOk()) // Expect 200 OK, as the controller returns DTO directly
                .andExpect(jsonPath("$.id").value(10L)) // Check ID in response body
                .andExpect(jsonPath("$.name").value("New Test Course")); // Check name

        // Verify the service method was called
        verify(courseService).createCourse(any(CourseDto.class));
    }

    @Test
    void testGetCourseById_Found_ReturnsOkAndDto() throws Exception {
        // Simple test for GET /api/courses/course-id/{id} when found
        // Arrange: Create an expected output DTO
        Long courseId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);

        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseId);
        courseDto.setName("Course By ID");
        courseDto.setAuthor(authorDto);

        // Mock service finding the course by ID
        when(courseService.getCourseById(courseId)).thenReturn(courseDto);

        // Act & Assert
        mockMvc.perform(get("/api/courses/course-id/{id}", courseId) // Perform GET request with ID using the correct path
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.id").value(courseId)) // Check ID in response body
                .andExpect(jsonPath("$.name").value("Course By ID")); // Check name

        // Verify the service method was called
        verify(courseService).getCourseById(courseId);
    }

    @Test
    void testGetCourseById_NotFound_ReturnsNotFound() throws Exception {
        // Simple test for GET /api/courses/course-id/{id} when not found
        // Arrange
        Long courseId = 99L; // Non-existent ID

        // Mock service throwing ResourceNotFoundException
        doThrow(new ResourceNotFoundException("Course not found")).when(courseService).getCourseById(courseId);

        // Act & Assert
        mockMvc.perform(get("/api/courses/course-id/{id}", courseId) // Perform GET request using the correct path
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type
                .andExpect(status().isNotFound()); // Expect 404 Not Found

        // Verify the service method was called
        verify(courseService).getCourseById(courseId);
    }

    @Test
    void testGetAllCourses_ReturnsListOfCourses() throws Exception {
        // Simple test for GET /api/courses/courses (paginated)
        // Arrange: Create a list of expected CourseDto objects
        AuthorDto authorDto1 = new AuthorDto();
        authorDto1.setId(1L);
        AuthorDto authorDto2 = new AuthorDto();
        authorDto2.setId(2L);

        CourseDto course1 = new CourseDto();
        course1.setId(1L);
        course1.setName("Course One");
        course1.setAuthor(authorDto1);

        CourseDto course2 = new CourseDto();
        course2.setId(2L);
        course2.setName("Course Two");
        course2.setAuthor(authorDto2);

        List<CourseDto> expectedCourses = Arrays.asList(course1, course2);
        Page<CourseDto> expectedPage = new PageImpl<>(expectedCourses, PageRequest.of(0, 10), expectedCourses.size()); // Create a Page object

        // Mock the service behavior: when getAllCourses is called with any Pageable, return the Page
        when(courseService.getAllCourses(any(Pageable.class))).thenReturn(expectedPage);

        // Act: Perform the GET request to the correct endpoint, potentially with pagination params
        mockMvc.perform(get("/api/courses/courses") // Perform GET request using the correct path
                        .param("page", "0") // Add pagination parameters
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type
                // Assert: Verify the response
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.content.size()").value(expectedCourses.size())) // Check the size of the content list in the Page response
                .andExpect(jsonPath("$.content[0].id").value(1L)) // Check ID of the first course
                .andExpect(jsonPath("$.content[0].name").value("Course One")) // Check name of the first course
                .andExpect(jsonPath("$.content[1].id").value(2L)) // Check ID of the second course
                .andExpect(jsonPath("$.content[1].name").value("Course Two")); // Check name of the second course

        // Verify the service method was called (with any Pageable argument)
        verify(courseService).getAllCourses(any(Pageable.class));
    }

    @Test
    void testUpdateCourse_Found_ReturnsOkAndUpdatedDto() throws Exception {
        // Simple test for PUT /api/courses/updateCourse/{id} when found
        // Arrange: Create an input DTO and an expected output DTO
        Long courseId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);

        CourseDto inputDto = new CourseDto();
        inputDto.setName("Updated Course Name");
        inputDto.setDescription("Updated Description");
        inputDto.setCredit(4);
        inputDto.setAuthor(authorDto);

        CourseDto updatedDto = new CourseDto();
        updatedDto.setId(courseId);
        updatedDto.setName("Updated Course Name");
        updatedDto.setDescription("Updated Description");
        updatedDto.setCredit(4);
        updatedDto.setAuthor(authorDto);

        // Mock service updating the course
        when(courseService.updateCourse(anyLong(), any(CourseDto.class))).thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId) // Perform PUT request with ID using the correct path
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(objectMapper.writeValueAsString(inputDto))) // Convert DTO to JSON string (Requires Getters in DTOs)
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.id").value(courseId)) // Check ID
                .andExpect(jsonPath("$.name").value("Updated Course Name")); // Check updated name

        // Verify the service method was called
        verify(courseService).updateCourse(anyLong(), any(CourseDto.class));
    }

    @Test
    void testUpdateCourse_NotFound_ReturnsNotFound() throws Exception {
        // Simple test for PUT /api/courses/updateCourse/{id} when not found
        // Arrange
        Long courseId = 99L; // Non-existent ID
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        CourseDto inputDto = new CourseDto(); // DTO content doesn't matter for this test
        inputDto.setName("Update Attempt");
        inputDto.setAuthor(authorDto);

        // Mock service throwing ResourceNotFoundException
        doThrow(new ResourceNotFoundException("course not found")).when(courseService).updateCourse(anyLong(), any(CourseDto.class));

        // Act & Assert
        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId) // Perform PUT request using the correct path
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(objectMapper.writeValueAsString(inputDto))) // Convert DTO to JSON string (Requires Getters in DTOs)
                .andExpect(status().isNotFound()); // Expect 404 Not Found

        // Verify the service method was called
        verify(courseService).updateCourse(anyLong(), any(CourseDto.class));
    }

    @Test
    void testDeleteCourse_Found_ReturnsOk() throws Exception { // Changed expected status to 200 OK
        // Simple test for DELETE /api/courses/delete/{id} when found
        // Arrange
        Long courseId = 1L;

        // Mock service deleting the course (does not return a value)
        doNothing().when(courseService).deleteCourse(courseId);

        // Act & Assert
        mockMvc.perform(delete("/api/courses/delete/{id}", courseId) // Perform DELETE request with ID using the correct path
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type
                .andExpect(status().isOk()); // Expect 200 OK (as controller returns void, Spring defaults to 200)

        // Verify the service method was called
        verify(courseService).deleteCourse(courseId);
    }

    @Test
    void testDeleteCourse_NotFound_ReturnsNotFound() throws Exception {
        // Simple test for DELETE /api/courses/delete/{id} when not found
        // Arrange
        Long courseId = 99L; // Non-existent ID

        // Mock service throwing ResourceNotFoundException when not found
        doThrow(new ResourceNotFoundException("course not found")).when(courseService).deleteCourse(courseId); // Use doThrow for exception

        // Act & Assert
        mockMvc.perform(delete("/api/courses/delete/{id}", courseId) // Perform DELETE request using the correct path
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type
                .andExpect(status().isNotFound()); // Expect 404 Not Found (caught by GlobalExceptionHandler)

        // Verify the service method was called
        verify(courseService).deleteCourse(courseId);
    }
}
