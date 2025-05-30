package com.micro.authservice.security;


import com.micro.authservice.model.Role;
import com.micro.authservice.model.User;
import com.micro.authservice.repository.RoleRepository;
import com.micro.authservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyUserDetailsService implements UserDetailsService {

    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return new MyUserDetails(user, role);
    }
}
