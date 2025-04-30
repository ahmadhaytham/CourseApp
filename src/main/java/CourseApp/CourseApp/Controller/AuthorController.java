package CourseApp.CourseApp.Controller;

import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.Entity.Author;
import CourseApp.CourseApp.Mapper.AuthorMapper;
import CourseApp.CourseApp.Repository.AuthorRepository;
import CourseApp.CourseApp.Service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService; // Inject the concrete Service

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/createAuthor")
    @ResponseStatus(HttpStatus.CREATED) // Use @ResponseStatus for 201 Created
    public AuthorDto createAuthor(@RequestBody AuthorDto dto) {

        return authorService.createAuthor(dto);
    }

    @GetMapping("/authors")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/search")
    public ResponseEntity<AuthorDto> getAuthorByEmail(@RequestParam String email) {

        AuthorDto authorDto = authorService.getAuthorByEmail(email);
        return ResponseEntity.ok(authorDto);
    }
}
