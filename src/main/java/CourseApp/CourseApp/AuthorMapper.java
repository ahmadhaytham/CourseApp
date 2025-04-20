package CourseApp.CourseApp;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);
    Author toEntity(AuthorDto dto);
}
