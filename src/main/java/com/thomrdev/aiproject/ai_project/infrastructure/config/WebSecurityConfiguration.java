package com.thomrdev.aiproject.ai_project.infrastructure.config;

import com.thomrdev.aiproject.ai_project.infrastructure.persistence.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            System.out.println("Buscando por: " + username);
            var user =  userRepository.findByUsernameOrEmail(username, username)
                    .orElseThrow(() -> {
                        System.out.println("No se encontró al usuario con username o email: " + username);
                        return new UsernameNotFoundException("User not found");
                    });

            return User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/new-account").permitAll()
                                        .requestMatchers("/admin").hasRole("ADMINISTRATOR")
                                        .requestMatchers("/js/**", "/css/**", "/images/**").permitAll()
                                        .requestMatchers("/h2-console/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("text/html;charset=UTF-8");
                            request.getRequestDispatcher("/login").forward(request, response);
                        })
                )
//                ambos necesitan de un form con action
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
        ;
        return http.build();
    }
}
