package CourseApp.CourseApp;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "rating.id", target = "ratingId")
    @Mapping(source = "assessment.id", target = "assessmentId")
    CourseDto toDto(Course course);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "ratingId", target = "rating.id")
    @Mapping(source = "assessmentId", target = "assessment.id")
    Course toEntity(CourseDto dto);
}
