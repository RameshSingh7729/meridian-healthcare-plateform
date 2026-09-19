package com.apiGateway.filter;

import com.apiGateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("JWT FILTER INITIALIZED");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Allow OPTIONS request (CORS preflight)
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        // Public APIs
        if (path.startsWith("/api/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            jwtUtil.validateToken(token);

            List<String> roles = jwtUtil.extractRoles(token);
            // ================================
            // DOCTOR API AUTHORIZATION
            // ================================

            // GET doctor APIs -> PATIENT, DOCTOR, ADMIN
            if (path.startsWith("/api/doctors")
                    && exchange.getRequest().getMethod() == HttpMethod.GET) {

                if (!roles.contains("PATIENT")
                        && !roles.contains("DOCTOR")
                        && !roles.contains("ADMIN")) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            // POST doctor APIs -> ADMIN only
            if (path.startsWith("/api/doctors")
                    && exchange.getRequest().getMethod() == HttpMethod.POST) {

                if (!roles.contains("ADMIN")) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            // PUT doctor APIs -> DOCTOR or ADMIN
            if (path.startsWith("/api/doctors")
                    && exchange.getRequest().getMethod() == HttpMethod.PUT) {

                if (!roles.contains("DOCTOR")
                        && !roles.contains("ADMIN")) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

           // DELETE doctor APIs -> ADMIN only
            if (path.startsWith("/api/doctors")
                    && exchange.getRequest().getMethod() == HttpMethod.DELETE) {

                if (!roles.contains("ADMIN")) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }

            System.out.println("USER ROLES: " + roles);
            System.out.println("REQUEST PATH: " + path);

            // ==========================================
            // PATIENT API AUTHORIZATION
            // ==========================================

            // Patient APIs -> PATIENT or ADMIN
            if (path.startsWith("/api/patients")) {

                if (!roles.contains("PATIENT")
                        && !roles.contains("ADMIN")) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);

                    return exchange.getResponse().setComplete();
                }
            }


            // ==========================================
            // APPOINTMENT API AUTHORIZATION
            // ==========================================

            // Appointment APIs -> PATIENT, DOCTOR or ADMIN
            if (path.startsWith("/api/appointments")) {

                if (!roles.contains("PATIENT")
                        && !roles.contains("DOCTOR")
                        && !roles.contains("ADMIN")) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);

                    return exchange.getResponse().setComplete();
                }
            }


            // ==========================================
            // PAYMENT API AUTHORIZATION
            // ==========================================

            // Payment APIs -> PATIENT or ADMIN
            if (path.startsWith("/api/payments")) {

                if (!roles.contains("PATIENT")
                        && !roles.contains("ADMIN")) {

                    exchange.getResponse()
                            .setStatusCode(HttpStatus.FORBIDDEN);

                    return exchange.getResponse().setComplete();
                }
            }

        } catch (Exception e) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);


    }

    @Override
    public int getOrder() {
        return -1;
    }
}