package CourseApp.CourseApp.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class HeaderValidationFilter extends OncePerRequestFilter {

    private static final String VALIDATION_HEADER_NAME = "x-validation-report";
    private static final String VALIDATION_HEADER_VALUE = "true";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String validationHeader = request.getHeader(VALIDATION_HEADER_NAME);

        if (validationHeader == null || !validationHeader.equals(VALIDATION_HEADER_VALUE)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid " + VALIDATION_HEADER_NAME + " header");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
