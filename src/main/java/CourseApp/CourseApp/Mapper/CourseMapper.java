package CourseApp.CourseApp.Mapper;

import CourseApp.CourseApp.DTO.CourseDto;
import CourseApp.CourseApp.Entity.Course;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AuthorMapper.class})
public interface CourseMapper {

    CourseDto toDto(Course course);
    Course toEntity(CourseDto dto);

    default Page<CourseDto> coursePageToCourseDTOPage(Page<Course> coursePage, Pageable pageable) {
        if (coursePage == null) {
            return null;
        }
        List<CourseDto> content = coursePage.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(content, pageable, coursePage.getTotalElements());
    }
}
