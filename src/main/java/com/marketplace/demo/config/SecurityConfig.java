package com.marketplace.demo.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApplicationContext applicationContext;
    private final JwtFilter jwtFilter;

    @Value("${bcrypt.rounds}")
    private int rounds;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("login", "register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/post/{id:[0-9]+}/views").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user", "/user/{id:[0-9]+}",
                                "/user/{id:[a-zA-Z]+}", "/user/{id:[0-9]+}/posts",
                                "/image/{id:[0-9]+}", "/post", "/post/{id:[0-9]+}",
                                "/post/{id:[0-9]+}/rating", "/post/{id:[0-9]+}/views",
                                "/product", "/product/{id:[0-9]+}", "/review",
                                "review/{id:[0-9]+}", "/tag/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/user/{id:[0-9]+}").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        UserDetailsService detailsService = applicationContext.getBean(UserDetailsService.class);

        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(rounds));
        authProvider.setUserDetailsService(detailsService);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR \n ROLE_MODERATOR > ROLE_DEVELOPER \n ROLE_SELLER > ROLE_USER \n" +
                "ROLE_DEVELOPER > ROLE_USER \n ROLE_SUPPORT_AGENT > ROLE_USER \n ROLE_CONTENT_MANAGER > ROLE_USER \n" +
                "ROLE_MODERATOR > ROLE_SELLER \n ROLE_MODERATOR > ROLE_SUPPORT_AGENT \n ROLE_MODERATOR > ROLE_CONTENT_MANAGER";

        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(DefaultWebSecurityExpressionHandler webSecurityExpressionHandler){
        return (web -> web.expressionHandler(customWebSecurityExpressionHandler()));
    }
}
