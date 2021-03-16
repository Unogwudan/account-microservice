package com.reloadly.accountmicroservice.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    private static final String[] SWAGGER_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui.html", "/webjars/**" };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    log.error("UNAUTHORIZED {}", swe.getResponse());
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    log.error("FORBIDDEN {}", swe.getResponse());
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers(HttpMethod.POST).permitAll()
                .pathMatchers(HttpMethod.PUT).permitAll()
                .pathMatchers("/api/v1/auth/").permitAll()
                .pathMatchers( "/swagger-ui.html").permitAll()
                .pathMatchers( "/webjars/**").permitAll()
                .pathMatchers( "/configuration/security").permitAll()
                .pathMatchers( "/configuration/ui").permitAll()
                .pathMatchers( "/swagger-resources/**").permitAll()
                .pathMatchers( "/swagger-resources").permitAll()
                .pathMatchers( "/v2/api-docs").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
}

