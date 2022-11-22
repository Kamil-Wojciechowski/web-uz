package com.uz.shop.animal.world.security.config;

import com.uz.shop.animal.world.security.config.filter.CustomAuthenticationFilter;
import com.uz.shop.animal.world.security.config.filter.CustomAuthorizationFilter;
import com.uz.shop.animal.world.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    public static final String ALLOWED_ORIGIN = "http://localhost:3000";
    @Autowired
    private final UserService userService;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final AuthenticationManagerBuilder auth,
                                           final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), userService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        auth.authenticationProvider(authenticationProvider());

        return http
                .cors(withDefaults())
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.ttf",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.jpeg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/error")
                .permitAll()

                .and()

                .authorizeRequests()
                .antMatchers("/api/v*/register/**", "/api/v*/token/refresh/**" ,"/api/v*/recovery/**")
                .permitAll()

                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()

                .and()

                .authorizeRequests().anyRequest().authenticated()

                .and()

                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
        configuration.setAllowedHeaders(Arrays.asList("access-control-allow-origin", "content-type"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authProvider;
    }
}
