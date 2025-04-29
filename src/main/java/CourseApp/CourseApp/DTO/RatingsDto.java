package CourseApp.CourseApp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingsDto {
    private Long id;
    private int number;
    private Long courseId;
}
