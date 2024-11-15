package com.boostmytool.beststore.security;


import com.boostmytool.beststore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;


@Configuration
public class SecurityConfig {
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Tạm thời vô hiệu hóa CSRF để kiểm tra --> Nếu không có không dùng POST được
                .authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers("/", "/login/**", "/register/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/employee/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/employee/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/userHome/**").hasAuthority("ROLE_USER")
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form.loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(authenticationSuccessHandler())
                                .permitAll()
                )
                .logout(
                        logout -> logout.permitAll()


                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                String targetUrl = determineTargetUrl(authentication);
                if (response.isCommitted()) {
                    return;
                }
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
            }

            protected String determineTargetUrl(Authentication authentication) {
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
                if (isAdmin) {
                    return "/employee"; // Chuyển hướng đến trang employee cho RULE_ADMIN
                } else {
                    return "/userHome"; // Chuyển hướng đến trang userHome cho ROLE_USER
                }
            }
        };
    }


}

