package com.example.englishstudying.security.config;

import com.example.englishstudying.security.AuthenticationManager;
import com.example.englishstudying.security.BearerTokenAuthenticationConverter;
import com.example.englishstudying.security.TokenHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${path.registration}")
    private String registrationPath;
    @Value("${path.authorization}")
    private String authorizationPath;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(registrationPath,authorizationPath)
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(
                                () -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                        )
                        .accessDeniedHandler(((exchange, denied) -> Mono.fromRunnable(
                                () -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                        )))
                .addFilterAt(authenticationWebFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter authenticationWebFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationWebFilter.setServerAuthenticationConverter(
                new BearerTokenAuthenticationConverter(new TokenHandler(secret))
        );
        bearerAuthenticationWebFilter.setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers("/**")
        );
        return bearerAuthenticationWebFilter;

    }
}
