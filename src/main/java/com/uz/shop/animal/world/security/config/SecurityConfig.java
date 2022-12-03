package com.uz.shop.animal.world.security.config;

import com.uz.shop.animal.world.security.config.filter.CustomAuthenticationFilter;
import com.uz.shop.animal.world.security.config.filter.CustomAuthorizationFilter;
import com.uz.shop.animal.world.services.AuthorizationService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private final UserService userService;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private final AuthorizationService authorizationService;

    @Autowired
    private CustomAuthorizationFilter customAuthorizationFilter;


    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http,
                                           final AuthenticationManagerBuilder auth,
                                           final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), userService, authorizationService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        auth.authenticationProvider(authenticationProvider());


        http
                .cors(withDefaults())
                .csrf()
                .disable()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
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
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers("/api/v*/register/**", "/api/v*/token/refresh/**" ,"/api/v*/recovery/**")
                .permitAll();

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v*/products/tags")
                .permitAll();

        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v*/products")
                .permitAll();

        http
                .authorizeRequests().anyRequest().authenticated();

        http
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", "http://127.0.0.1:8081"));
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
