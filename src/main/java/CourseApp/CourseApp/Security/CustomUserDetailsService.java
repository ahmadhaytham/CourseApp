package CourseApp.CourseApp.Security;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, String> users = new HashMap<>();

    public CustomUserDetailsService() {
        // In-memory users for demonstration
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        users.put("user", encoder.encode("password"));
        users.put("admin", encoder.encode("admin"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = users.get(username);
        if (password == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(username, password, new ArrayList<>());
    }
}
