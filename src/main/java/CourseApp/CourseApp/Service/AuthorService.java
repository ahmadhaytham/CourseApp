package CourseApp.CourseApp.Service;

import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.Entity.Author;
import CourseApp.CourseApp.Mapper.AuthorMapper;
import CourseApp.CourseApp.Repository.AuthorRepository;
import CourseApp.CourseApp.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Keep the Service annotation
public class AuthorService { // Now a concrete class, no interface

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired // Inject Repository and Mapper
    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    // Keep your methods
    @Transactional // Add transactional annotation for write operations
    public AuthorDto createAuthor(AuthorDto dto) {
        Author author = authorMapper.toEntity(dto); // Map DTO to Entity
        Author savedAuthor = authorRepository.save(author); // Save Entity using Repository
        return authorMapper.toDto(savedAuthor); // Map saved Entity back to DTO
    }

    @Transactional // Add transactional annotation for read operations
    public AuthorDto getAuthorByEmail(String email) {
        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("author not found")); // Use your custom exception
        return authorMapper.toDto(author); // Map Entity to DTO
    }
    @Transactional
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll(); // Fetch all authors from repository
        // Map the list of Author entities to a list of AuthorDto
        return authors.stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    }
