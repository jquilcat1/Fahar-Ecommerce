package pe.edu.utp.streetwear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/catalogo", "/producto/**", "/nosotros", "/contacto", "/contacto/enviar",
                                "/error")
                        .permitAll()
                        .requestMatchers("/login", "/registro").permitAll()
                        .requestMatchers("/perfil", "/historial", "/carrito/**").hasAnyRole("CLIENTE", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                            if (isAdmin) {
                                response.sendRedirect("/admin/productos"); 
                            } else {
                                response.sendRedirect("/"); 
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        return http.build();
    }
}