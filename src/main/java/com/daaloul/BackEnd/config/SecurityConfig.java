package com.daaloul.BackEnd.config;


import com.daaloul.BackEnd.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                
                .authorizeHttpRequests(request -> request
                        // Public endpoints
                        .requestMatchers("/login","register/student/by/himself","api/ocr/extract" ,"/questions","/api/gemini/generate" ,"/save/test").permitAll()

                        // ADMIN-only routes
                        .requestMatchers(
                                "/register/admin", "/register/student", "/register/psupervisor", "/register/esupervisor",
                                "/getAllUsers", "/getUserById/**", "/updateUser/**", "/deleteUser/**",
                                "/getAllStudents", "/deleteStudent/**", "/getAllESupervisors", "/deleteESupervisor/**",
                                "/getAllPSupervisors", "/deletePSupervisor/**", "/getAllCVs", "/getAllCover-letters",
                                "/getAllSpecifications", "/admin-profile/**", "/admins", "/updateAdmin/**", "/deleteAdmin/**",
                                "/register/UM", "/UMs", "/deleteUM/**", "/CreateCompanyProject", "/getAllCompanyProjects",
                                "/getCompanyProject/**", "/updateCompanyProject/**", "/deleteCompanyProject/**",
                                "/createInternRoom", "/internRooms", "/internRoom/**", "/internRoomByStudent/**",
                                "/internRoomByPS/**", "/internRoomByES/**", "/internRoomByUM/**", "/updateInternRoom/**"
                        ).hasRole("ADMIN")
                        
                        // PS (Professional Supervisor) routes
                        .requestMatchers(
                                "/getPSupervisor/**", "/rate-student-ps", "/createTask", "/update-task/**",
                                "/delete-task/**", "/update-score/**", "/update-task-status-to-approved/**"
                        ).hasRole("PS")

                        // ES (Educational Supervisor) routes
                        .requestMatchers(
                                "/getESupervisor/**", "/rate-student-es", "/company-project-by-student-and-educational-supervisor",
                                "/company-projects-by-educational-supervisor-id"
                        ).hasRole("ES")

                        // STUDENT routes
                        .requestMatchers(
                                "/getStudent/**", "/{projectId}/upload-code", "/{projectId}/upload-report",
                                "/submit-solution/**"
                        ).hasRole("STUDENT")

                        // UM (User Manager) routes
                        .requestMatchers("/UM/**").hasRole("UM")

                        // Shared routes (ADMIN and PS)
                        .requestMatchers(
                                "/{studentId}/cv", "/{studentId}/cover-letter", "/updatePSupervisor/**"
                        ).hasAnyAuthority("ADMIN", "PS")

                        // Shared routes (ADMIN and UM)
                        .requestMatchers("/updateUM/**").hasAnyAuthority("ADMIN", "UM")

                        // Shared routes (ADMIN and ES)
                        .requestMatchers("/updateESupervisor/**").hasAnyAuthority("ADMIN", "ES")

                        // Shared routes (ADMIN and STUDENT)
                        .requestMatchers("/updateStudent/**").hasAnyAuthority("ADMIN", "STUDENT")

                        // Authenticated routes (any authenticated user)
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // Use basic authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("CORS configuration is being initialized...");
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from these origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:5173"
        ));
        
        // Allow all methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type", "X-Requested-With", 
            "Accept", "Origin", "Access-Control-Request-Method", 
            "Access-Control-Request-Headers"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Set max age of preflight requests
        configuration.setMaxAge(3600L);
        
        // Expose these headers to the browser
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Disposition"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        System.out.println("CORS configured for paths: /**");
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
