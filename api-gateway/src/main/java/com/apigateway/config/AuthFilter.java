package com.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Autowired
    private RouteValidator validator;

    //@Autowired
    //private RestTemplate template;

    @Autowired
    private JwtUtil jwtUtil;


  /*  @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        ServerWebExchange exchange = (ServerWebExchange) request;
        if (validator.isSecured.test(exchange.getRequest())) {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    //REST call to AUTH service -- It is not good approach as a security Point of View
                    //template.getForObject("http:/USER-SERVICE/users/auth/validate?token" + authHeader, String.class);
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new RuntimeException("unauthorized access to application");
                }
            } else {

                throw new RuntimeException("missing authorization header");
            }
        }
        filterChain.doFilter(request, response);
    }*/

    @Override
    public void doFilter(ServletRequest request , ServletResponse response , FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(validator.isSecured.test(httpRequest)) {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    System.out.println("Invalid Access...!");
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.setContentType("application/json");
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(new ErrorResponse("Unauthorized access to application - "+e.getMessage()));
                    response.getWriter().write(json);
                    return;
                }
            } else {
                System.out.println("Missing Authorization Header !!");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(new ErrorResponse("Missing Authorization Header !!"));
                response.getWriter().write(json);
                return;
            }
        }
        chain.doFilter(httpRequest , httpResponse);
    }
}

@Data
class ErrorResponse {
    private String status;
    private String message;
    private boolean success;

    public ErrorResponse(String message) {
        this.status = HttpStatus.UNAUTHORIZED.toString();
        this.message = message;
        this.success = false;
    }
}
