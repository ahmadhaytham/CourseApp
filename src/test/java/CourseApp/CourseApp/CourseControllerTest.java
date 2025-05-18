package CourseApp.CourseApp;

import CourseApp.CourseApp.Controller.CourseController;
import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
// Import Spring Security test annotations and matchers
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // <--- Added csrf import
import org.springframework.security.test.context.support.WithMockUser; // <--- Added WithMockUser import

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    // Define constants for the custom header
    private final String VALIDATION_HEADER_NAME = "x-validation-report";
    private final String VALIDATION_HEADER_VALUE = "true";

    // --- Test Method for Create Course (Secured Endpoint) ---
    @Test
    @WithMockUser // This endpoint requires authentication, so simulate a logged-in user
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

        // Act: Perform the POST request with the required header and include CSRF token for POST
        mockMvc.perform(post("/api/courses/createCourse") // Use the standard post method
                        .contentType(MediaType.APPLICATION_JSON) // Set content type to JSON
                        .content(objectMapper.writeValueAsString(inputDto)) // Convert DTO to JSON string
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Add CSRF token for POST requests
                // Assert: Verify the response
                .andExpect(status().isOk()) // Expect 200 OK, as the controller returns DTO directly
                .andExpect(jsonPath("$.id").value(10L)) // Check ID in response body
                .andExpect(jsonPath("$.name").value("New Test Course")); // Check name

        // Verify the service method was called
        verify(courseService).createCourse(any(CourseDto.class));
    }

    // Add test for Create Course without header (should be 401 Unauthorized from the filter)
//    @Test
//    void testCreateCourse_WithoutHeader_ReturnsUnauthorized() throws Exception {
//        AuthorDto authorDto = new AuthorDto();
//        authorDto.setId(1L);
//        CourseDto inputDto = new CourseDto();
//        inputDto.setName("New Test Course");
//        inputDto.setDescription("Description");
//        inputDto.setCredit(3);
//        inputDto.setAuthor(authorDto);
//
//        // Perform POST request without header, disable CSRF to prevent 403 from CSRF filter
//        mockMvc.perform(post("/api/courses/createCourse")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(inputDto))
//                        .with(csrf().disable())) // <--- Disable CSRF to test header filter
//                .andExpect(status().isUnauthorized()); // Expect 401 due to missing header
//    }

    // Add test for Create Course with header but no authentication (should be 401 Unauthorized from Spring Security)
    @Test
    void testCreateCourse_WithHeaderWithoutAuthentication_ReturnsUnauthorized() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        CourseDto inputDto = new CourseDto();
        inputDto.setName("New Test Course");
        inputDto.setDescription("Description");
        inputDto.setCredit(3);
        inputDto.setAuthor(authorDto);

        // Perform POST request with header but no authentication, include CSRF as header filter passes
        mockMvc.perform(post("/api/courses/createCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Include CSRF as header filter passes
                .andExpect(status().isUnauthorized()); // Expect 401 because authentication is required
    }


    // --- Test Method for Get Course by ID (Permitted Endpoint) ---


    // Add test for Get Course by ID without header (should be 401 Unauthorized from the filter)
    @Test
    void testGetCourseById_WithoutHeader_ReturnsUnauthorized() throws Exception {
        Long courseId = 1L;

        // Perform GET request without header
        mockMvc.perform(get("/api/courses/course-id/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // Expect 401 due to missing header
    }


//    @Test
//    void testGetCourseById_NotFound_ReturnsNotFound() throws Exception {
//        // Simple test for GET /api/courses/course-id/{id} when not found
//        // Arrange
//        Long courseId = 99L; // Non-existent ID
//
//        // Mock service throwing ResourceNotFoundException
//        doThrow(new ResourceNotFoundException("Course not found")).when(courseService).getCourseById(courseId);
//
//        // Act & Assert: Perform GET request with the required header and explicitly set as anonymous
//        mockMvc.perform(get("/api/courses/course-id/{id}", courseId) // Perform GET request using the correct path
//                        .contentType(MediaType.APPLICATION_JSON) // Set content type
//                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
//                        .with(anonymous())) // <--- Explicitly set as anonymous principal
//                .andExpect(status().isNotFound()); // Expect 404 Not Found (caught by GlobalExceptionHandler)
//
//        // Verify the service method was called
//        verify(courseService).getCourseById(courseId);
//    }

    // --- Test Method for Get All Courses (Permitted Endpoint) ---


    // Add test for Get All Courses without header (should be 401 Unauthorized from the filter)
    @Test
    void testGetAllCourses_WithoutHeader_ReturnsUnauthorized() throws Exception {
        // Perform GET request without header
        mockMvc.perform(get("/api/courses/courses")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()); // Expect 401 due to missing header
    }


    // --- Test Method for Update Course (Secured Endpoint) ---
    @Test
    @WithMockUser // This endpoint requires authentication, so simulate a logged-in user
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

        // Act & Assert: Perform PUT request with header, simulated authentication, and CSRF token
        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId) // Perform PUT request with ID
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(objectMapper.writeValueAsString(inputDto)) // Convert DTO to JSON string
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Add CSRF token for PUT requests
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.id").value(courseId)) // Check ID
                .andExpect(jsonPath("$.name").value("Updated Course Name")); // Check updated name

        // Verify the service method was called
        verify(courseService).updateCourse(anyLong(), any(CourseDto.class));
    }

    // Add test for Update Course without header (should be 401 Unauthorized from the filter)
//    @Test
//    void testUpdateCourse_WithoutHeader_ReturnsUnauthorized() throws Exception {
//        Long courseId = 1L;
//        AuthorDto authorDto = new AuthorDto();
//        authorDto.setId(1L);
//        CourseDto inputDto = new CourseDto();
//        inputDto.setName("Update Attempt");
//        inputDto.setAuthor(authorDto);
//
//        // Perform PUT request without header, disable CSRF to prevent 403 from CSRF filter
//        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(inputDto))
//                        .with(csrf().disable())) // <--- Disable CSRF to test header filter
//                .andExpect(status().isUnauthorized()); // Expect 401 due to missing header
//    }

    // Add test for Update Course with header but no authentication (should be 401 Unauthorized from Spring Security)
    @Test
    void testUpdateCourse_WithHeaderWithoutAuthentication_ReturnsUnauthorized() throws Exception {
        Long courseId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        CourseDto inputDto = new CourseDto();
        inputDto.setName("Update Attempt");
        inputDto.setAuthor(authorDto);

        // Perform PUT request with header but no authentication, include CSRF as header filter passes
        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Include CSRF as header filter passes
                .andExpect(status().isUnauthorized()); // Expect 401 because authentication is required
    }


    @Test
    @WithMockUser // This endpoint requires authentication, so simulate a logged-in user
    void testUpdateCourse_NotFound_ThrowsException() throws Exception {
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

        // Act & Assert: Perform PUT request with header, simulated authentication, and CSRF token
        mockMvc.perform(put("/api/courses/updateCourse/{id}", courseId) // Perform PUT request
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(objectMapper.writeValueAsString(inputDto)) // Convert DTO to JSON string
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Add CSRF token for PUT requests
                .andExpect(status().isNotFound()); // Expect 404 Not Found (caught by GlobalExceptionHandler)

        // Verify the service method was called
        verify(courseService).updateCourse(anyLong(), any(CourseDto.class));
    }

    // --- Test Method for Delete Course (Secured Endpoint) ---
    @Test
    @WithMockUser // This endpoint requires authentication, so simulate a logged-in user
    void testDeleteCourse_Found_ReturnsOk() throws Exception {
        // Simple test for DELETE /api/courses/delete/{id} when found
        // Arrange
        Long courseId = 1L;

        // Mock service deleting the course (does not return a value)
        doNothing().when(courseService).deleteCourse(courseId);

        // Act & Assert: Perform DELETE request with header, simulated authentication, and CSRF token
        mockMvc.perform(delete("/api/courses/delete/{id}", courseId) // Perform DELETE request with ID
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Add CSRF token for DELETE requests
                .andExpect(status().isOk()); // Expect 200 OK

        // Verify the service method was called
        verify(courseService).deleteCourse(courseId);
    }

    // Add test for Delete Course without header (should be 401 Unauthorized from the filter)
//    @Test
//    void testDeleteCourse_WithoutHeader_ReturnsUnauthorized() throws Exception {
//        Long courseId = 1L;
//        // Perform DELETE request without header, disable CSRF to prevent 403 from CSRF filter
//        mockMvc.perform(delete("/api/courses/delete/{id}", courseId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf().disable())) // <--- Disable CSRF to test header filter
//                .andExpect(status().isUnauthorized()); // Expect 401 due to missing header
//    }

    // Add test for Delete Course with header but no authentication (should be 401 Unauthorized from Spring Security)
    @Test
    void testDeleteCourse_WithHeaderWithoutAuthentication_ReturnsUnauthorized() throws Exception {
        Long courseId = 1L;
        // Perform DELETE request with header but no authentication, include CSRF as header filter passes
        mockMvc.perform(delete("/api/courses/delete/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Include CSRF as header filter passes
                .andExpect(status().isUnauthorized()); // Expect 401 because authentication is required
    }


    @Test
    @WithMockUser // This endpoint requires authentication, so simulate a logged-in user
    void testDeleteCourse_NotFound_ReturnsNotFound() throws Exception {
        // Simple test for DELETE /api/courses/delete/{id} when not found
        // Arrange
        Long courseId = 99L; // Non-existent ID

        // Mock service throwing ResourceNotFoundException when not found
        doThrow(new ResourceNotFoundException("course not found")).when(courseService).deleteCourse(courseId);

        // Act & Assert: Perform DELETE request with header, simulated authentication, and CSRF token
        mockMvc.perform(delete("/api/courses/delete/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(VALIDATION_HEADER_NAME, VALIDATION_HEADER_VALUE) // <--- Add the custom header
                        .with(csrf())) // <--- Add CSRF token for DELETE requests
                .andExpect(status().isNotFound()); // Expect 404 Not Found (caught by GlobalExceptionHandler)

        // Verify the service method was called
        verify(courseService).deleteCourse(courseId);
    }
}