package CourseApp.CourseApp.Mapper;

import CourseApp.CourseApp.DTO.AuthorDto;
import CourseApp.CourseApp.Entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);
    Author toEntity(AuthorDto dto);
}
