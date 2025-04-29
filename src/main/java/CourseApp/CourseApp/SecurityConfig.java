package CourseApp.CourseApp;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import CourseApp.CourseApp.security.ValidationReportHeaderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ValidationReportHeaderFilter validationReportHeaderFilter;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, ValidationReportHeaderFilter validationReportHeaderFilter, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.validationReportHeaderFilter = validationReportHeaderFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // For simplicity, disable CSRF
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions for basic auth
                .addFilterBefore(validationReportHeaderFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/courses", "/courses/**").permitAll() // Permit all for view course endpoints
                        .requestMatchers("/add", "/update/**", "/delete/**").authenticated() // Secure these endpoints
                        .anyRequest().denyAll() // Deny any other requests not explicitly matched
                )
                .httpBasic(withDefaults()); // Enable HTTP Basic authentication
        return http.build();
    }

    public static org.springframework.security.config.Customizer<org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer<HttpSecurity>> withDefaults() {
        return httpBasicConfigurer -> {};
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
