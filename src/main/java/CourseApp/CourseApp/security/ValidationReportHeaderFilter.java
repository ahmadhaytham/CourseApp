package CourseApp.CourseApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ValidationReportHeaderFilter extends OncePerRequestFilter {

    private static final String VALIDATION_HEADER = "x-validation-report";
    private static final String VALIDATION_VALUE = "true";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String validationHeader = request.getHeader(VALIDATION_HEADER);

        if (VALIDATION_VALUE.equalsIgnoreCase(validationHeader)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Missing or invalid '" + VALIDATION_HEADER + "' header.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // We want this filter to apply to all requests initially.
        // Later, Spring Security will handle authorization for specific paths.
        return false;
    }
}