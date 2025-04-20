package CourseApp.CourseApp;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    AssessmentDto toDto(Assessment assessment);
    Assessment toEntity(AssessmentDto dto);
}
