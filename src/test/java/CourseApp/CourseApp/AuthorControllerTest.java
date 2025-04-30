package CourseApp.CourseApp;

import CourseApp.CourseApp.Controller.AuthorController;
import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.Service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate; // Import LocalDate
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Assuming your AuthorController is mapped to "/api/authors"
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Use @MockBean to create a mock of the concrete AuthorService class
    // and register it as a bean in the Spring test context.
    // This mock will be injected into the AuthorController.
    @MockBean // <-- Ensure this is present and correctly references the concrete class
    private AuthorService authorService; // <-- The type must match the bean you want to mock

    // --- Test Methods ---

    @Test
    void testCreateAuthor_ReturnsCreated() throws Exception {
        // Arrange: Create an input DTO and an expected output DTO (with ID)
        AuthorDto inputDto = new AuthorDto();
        inputDto.setName("New Test Author");
        inputDto.setEmail("test.author@example.com");
        inputDto.setBirthdate(LocalDate.of(1990, 1, 1));

        AuthorDto createdDto = new AuthorDto();
        createdDto.setId(1L); // Simulate service returned ID
        createdDto.setName("New Test Author");
        createdDto.setEmail("test.author@example.com");
        createdDto.setBirthdate(LocalDate.of(1990, 1, 1));

        // Mock the service behavior: when createAuthor is called with any DTO, return the created DTO
        when(authorService.createAuthor(any(AuthorDto.class))).thenReturn(createdDto);

        // Act: Perform the POST request to the specific endpoint
        mockMvc.perform(post("/api/authors/createAuthor") // Use your specific endpoint path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto))) // Convert DTO to JSON string
                // Assert: Verify the response
                .andExpect(status().isCreated()) // Check status is 201 Created (due to @ResponseStatus on controller method)
                .andExpect(jsonPath("$.id").value(1L)) // Check ID in response body
                .andExpect(jsonPath("$.name").value("New Test Author")) // Check name
                .andExpect(jsonPath("$.email").value("test.author@example.com")); // Check email

        // Verify the service method was called by the controller
        verify(authorService).createAuthor(any(AuthorDto.class));
    }

    @Test
    void testGetAuthorByEmail_Found_ReturnsOkAndDto() throws Exception {
        // Arrange
        String email = "test@example.com";
        AuthorDto authorDto = new AuthorDto(); // Simulate DTO returned by service
        authorDto.setId(1L);
        authorDto.setEmail(email);
        authorDto.setName("Test Author");

        // Mock service finding the author
        when(authorService.getAuthorByEmail(email)).thenReturn(authorDto);

        // Act & Assert
        mockMvc.perform(get("/api/authors/search") // Use your search endpoint path
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.id").value(1L)) // Check ID
                .andExpect(jsonPath("$.email").value(email)); // Check email

        // Verify the service method was called
        verify(authorService).getAuthorByEmail(email);
    }

    @Test
    void testGetAuthorByEmail_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        String email = "nonexistent@example.com";

        // Mock service throwing ResourceNotFoundException
        doThrow(new ResourceNotFoundException("author not found")).when(authorService).getAuthorByEmail(email);

        // Act & Assert
        mockMvc.perform(get("/api/authors/search")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect 404 Not Found (handled by GlobalExceptionHandler)

        // Verify the service method was called
        verify(authorService).getAuthorByEmail(email);
    }

    @Test
    void testGetAllAuthors_ReturnsListOfAuthors() throws Exception {
        // Arrange: Create a list of expected AuthorDto objects
        AuthorDto author1 = new AuthorDto();
        author1.setId(1L);
        author1.setName("Author One");
        author1.setEmail("one@example.com");

        AuthorDto author2 = new AuthorDto();
        author2.setId(2L);
        author2.setName("Author Two");
        author2.setEmail("two@example.com");

        List<AuthorDto> expectedAuthors = Arrays.asList(author1, author2);

        // Mock the service behavior: when getAllAuthors is called, return the list
        when(authorService.getAllAuthors()).thenReturn(expectedAuthors);

        // Act: Perform the GET request to the base endpoint
        mockMvc.perform(get("/api/authors/authors") // Map GET requests to /api/authors
                        .contentType(MediaType.APPLICATION_JSON))
                // Assert: Verify the response
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.size()").value(expectedAuthors.size())) // Check the size of the returned list
                .andExpect(jsonPath("$[0].id").value(1L)) // Check ID of the first author
                .andExpect(jsonPath("$[0].name").value("Author One")) // Check name of the first author
                .andExpect(jsonPath("$[1].id").value(2L)) // Check ID of the second author
                .andExpect(jsonPath("$[1].name").value("Author Two")); // Check name of the second author

        // Verify the service method was called
        verify(authorService).getAllAuthors();
    }

    }