package CourseApp.CourseApp;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    RatingsDto toDto(Rating rating);
    Rating toEntity(RatingsDto dto);
}
