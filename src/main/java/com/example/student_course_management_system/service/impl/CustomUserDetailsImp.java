package com.example.student_course_management_system.service.impl;

import com.example.student_course_management_system.model.User;
import com.example.student_course_management_system.repository.UserRepository;
import com.example.student_course_management_system.security.UserPrincipal;
import com.example.student_course_management_system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsImp implements UserDetailsService, CustomUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("Tài khoản không được tìm thấy với id = %s", id)));

        return UserPrincipal.create(user);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException(String.format("Tài khoản không được tìm thấy: %s", usernameOrEmail)));
        return UserPrincipal.create(user);
    }
}
