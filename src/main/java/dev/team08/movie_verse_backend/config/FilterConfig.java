package dev.team08.movie_verse_backend.config;

import dev.team08.movie_verse_backend.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class FilterConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public FilterConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        // NOTE: This filter is currently disabled. It was registered for
        // `/api/orders/*`, a path pattern that doesn't exist in this app — so
        // in practice it never matches and authentication for the real API
        // happens at the controller layer via `userService.verifyToken` /
        // `getUserFromToken`. We also intentionally do NOT register it for
        // `/api/**` here, because the filter rejects every request without a
        // valid Bearer token, which would break login and register too. If
        // you want filter-level auth, add an allow-list for public endpoints
        // inside `JwtAuthenticationFilter` first, then widen the pattern here.
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/api/__disabled__/*");
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Use allowedOriginPatterns instead of allowedOrigins
        config.addAllowedOriginPattern("*");

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
