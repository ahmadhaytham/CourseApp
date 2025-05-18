package CourseApp.CourseApp.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final HeaderValidationFilter headerValidationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(HeaderValidationFilter headerValidationFilter, CustomUserDetailsService customUserDetailsService) {
        this.headerValidationFilter = headerValidationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity, configure as needed
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/courses/courses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses/course-id/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/courses/createCourse").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/courses/updateCourse/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/delete/{id}").authenticated()
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .httpBasic(withDefaults -> {}) // Enable HTTP Basic authentication
                .addFilterBefore(headerValidationFilter, UsernamePasswordAuthenticationFilter.class); // Add custom filter before standard filters


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}