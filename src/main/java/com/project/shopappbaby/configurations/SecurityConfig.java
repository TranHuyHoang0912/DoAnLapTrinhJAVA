package com.project.shopappbaby.configurations;

import com.project.shopappbaby.models.User;
import com.project.shopappbaby.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor

public class SecurityConfig {
    // khởi tạo đối tượng user's detail

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailService() {
        return phoneNunber ->  userRepository.findByPhoneNumber(phoneNunber)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Can not find user with phone number = " + phoneNunber));

    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // đối tượng mã hóa mật khẩu
        return new BCryptPasswordEncoder();

    }
    @Bean
    public AuthenticationProvider authenticationProvider(){//chịu trách nhiệm xác thực người dùng dựa trên thông tin cung cấp.
        DaoAuthenticationProvider authProvider  = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    // giống hàm PasswordEncoder, AuthenticationProvider cung cấp vào 1 đối tượng
    @Bean
    public AuthenticationManager authenticationManager( //chịu trách nhiệm quản lý toàn bộ quá trình xác thực trong hệ thống.
            AuthenticationConfiguration config
    ) throws Exception{
        return config.getAuthenticationManager();
    }
}

