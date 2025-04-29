package CourseApp.CourseApp.Mapper;

import CourseApp.CourseApp.DTO.AssessmentDto;
import CourseApp.CourseApp.Entity.Assessment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    AssessmentDto toDto(Assessment assessment);
    Assessment toEntity(AssessmentDto dto);
}
