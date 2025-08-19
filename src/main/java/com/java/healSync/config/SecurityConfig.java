package com.java.healSync.config;

import com.java.healSync.config.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Allow preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Allow static resources (React build)
                        .requestMatchers(
                                "/",                // root
                                "/index.html",      // main entry point
                                "/static/**",       // static files
                                "/*.ico",
                                "/*.png",
                                "/*.jpg",
                                "/*.svg",
                                "/*.css",
                                "/*.js"
                        ).permitAll()

                        // Public API endpoints
                        .requestMatchers("/auth/login", "/auth/registration").permitAll()

                        // Admin + Patient access
                        .requestMatchers(
                                "/auth/appointments/delete/**",
                                "/appointments/patient/**",
                                "/appointments/create",
                                "/appointments/update/**",
                                "/appointments/check-availability"
                        ).hasAnyRole("ADMIN", "PATIENT")

                        // Admin-only access
                        .requestMatchers("/auth/all?role=ADMIN").hasRole("ADMIN")
                        .requestMatchers("/auth/delete/**", "/auth/update/**", "/auth/appointments/**", "/appointments/doctors-availability-summary").hasRole("ADMIN")

                        // Allow PATIENT role to access /doctors/doctors
                        .requestMatchers("/doctors/doctors").hasRole("PATIENT")

                        // Admin + Doctor access
                        .requestMatchers(
                                "/doctors/**",
                                "/appointments/doctor/**",
                                "/hospitals/all",
                                "/appointments/doctor-load/**"
                        ).hasAnyRole("ADMIN", "DOCTOR")

                        // Doctor only
                        .requestMatchers("/doctors/update/**").hasRole("DOCTOR")

                        // Any API request needs authentication
                        .requestMatchers("/api/**").authenticated()

                        // Any other request (frontend routes) → allow and serve index.html
                        .anyRequest().permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
