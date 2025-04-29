package CourseApp.CourseApp.Mapper;

import CourseApp.CourseApp.Entity.Rating;
import CourseApp.CourseApp.DTO.RatingsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    RatingsDto toDto(Rating rating);
    Rating toEntity(RatingsDto dto);
}
