package exe.exe201be.config;

import exe.exe201be.exception.CustomAuthHandlers;
import exe.exe201be.utils.CookieOrHeaderBearerTokenResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain swaggerChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs.yaml")
                .csrf(csrf -> csrf.disable())
                .cors(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain apiChain(HttpSecurity http, CustomAuthHandlers handlers) throws Exception {
        return http
                .securityMatcher("/api/**") // chỉ áp cho API
                .csrf(csrf -> csrf.disable())
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST,
                                "/api/users/create",
                                "/api/images/upload",
                                "/api/v1/auth/register",
                                "/api/webhook/sepay",
                                "/api/payment/create-order",
                                "api/payment/orders/{id}",
                                "/api/v1/auth/login").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/users/getAllUser").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "api/service-packages",
                                "/api/service-packages/*",
                                "/api/service-providers",
                                "/api/service-providers/*",
                                "/api/users/{id}",
                                "/api/projects",
                                "/api/projects/{id}"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/service-providers")
                        .hasAnyAuthority("ADMIN", "USER")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/service-providers/{id}"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PATCH,
                                "/api/service-packages/{id}",
                                "/api/orders/{id}"
                        ).hasAuthority("PROVIDER")

                        .requestMatchers(HttpMethod.POST,
                                "/api/service-packages")
                        .hasAuthority("PROVIDER")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/service-providers/{providerId}",
                                "/api/service-packages/{serviceId}").hasAuthority("PROVIDER")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/users/{id}").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/users/{id}").hasAnyAuthority("USER", "PROVIDER")

                        .requestMatchers(HttpMethod.POST,
                                "/api/{projectId}/tasks",
                                "/api/projects/{projectId}/members").access(this::isProjectLeader)

                        .requestMatchers(HttpMethod.POST,
                                "/api/projects",
                                "/api/orders").hasAuthority("USER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/{projectId}/tasks",
                                "/api/{projectId}/tasks/{taskId}").access(this::isProjectMemberOrLeader)

                        .requestMatchers(HttpMethod.GET,
                                "/api/orders/user").hasAuthority("USER")

                        .requestMatchers(HttpMethod.GET,
                                "/api/dashboard/member-data").hasAuthority("USER")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        // đọc token từ cookie "access_token" hoặc header Authorization
                        .bearerTokenResolver(new CookieOrHeaderBearerTokenResolver("access_token"))
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(handlers.authenticationEntryPoint()) // 401
                        .accessDeniedHandler(handlers.accessDeniedHandler())           // 403
                )
                .build();
    }

    private AuthorizationDecision isProjectMemberOrLeader(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String projectId = context.getVariables().get("projectId").toString();

        boolean granted = authentication.get().getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("PROJECT_" + projectId + "_PROJECT_MEMBER") ||
                        a.getAuthority().equals("PROJECT_" + projectId + "_PROJECT_LEADER")
        );

        return new AuthorizationDecision(granted);
    }

    private AuthorizationDecision isProjectLeader(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String projectId = context.getVariables().get("projectId").toString();

        boolean granted = authentication.get().getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("PROJECT_" + projectId + "_PROJECT_LEADER")
        );

        return new AuthorizationDecision(granted);
    }


    private AuthorizationDecision hasTaskRole(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String projectId = context.getVariables().get("projectId").toString();
        String taskId = context.getVariables().get("taskId").toString();

        boolean granted = authentication.get().getAuthorities().stream().anyMatch(a ->
                a.getAuthority().equals("PROJECT_" + projectId + "_TASK_" + taskId + "_TASK_ASSIGNEE") ||
                        a.getAuthority().equals("PROJECT_" + projectId + "_TASK_" + taskId + "_TASK_OWNER")
        );

        return new AuthorizationDecision(granted);
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthConverter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Khuyến nghị: chỉ định origin FE thật thay vì "*"
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true); // cần true nếu gửi cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
