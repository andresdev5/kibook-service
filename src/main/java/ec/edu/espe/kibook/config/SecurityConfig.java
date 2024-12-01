package ec.edu.espe.kibook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                    .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/books/search").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/genres").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/genres/search").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/genres").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/genres/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/genres/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/authors").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/authors/search").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/authors").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/users/me").hasAnyRole("USER", "ADMIN")

                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/auth/**").permitAll()

                    .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider);
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(e -> e.authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(401);
        }).authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(false)
                .ignoring()
                .requestMatchers(
                    "/error",
                    "/public/**",
                    "/auth/**",
                    "/actuator/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**"
                );
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://localhost"
        ));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
