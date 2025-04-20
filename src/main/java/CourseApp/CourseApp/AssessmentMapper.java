package CourseApp.CourseApp;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    AssessmentDto toDto(Assessment assessment);
    Assessment toEntity(AssessmentDto dto);
}
