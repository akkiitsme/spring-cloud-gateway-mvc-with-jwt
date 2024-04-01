package com.apigateway.config;

import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    //It's Ignore these mappings
    public static final List<String> openApiEndpoints =
            new ArrayList<>(Arrays.asList("/users","/users/","/users/**",
                    "/eureka"
            ));

    public Predicate<HttpServletRequest> isSecured = request -> openApiEndpoints.stream()
            .noneMatch(uri -> request.getServletPath().contains(uri));

  /*  public Predicate<HttpServletRequest> isNewSecured = request -> {
        String servletPath = request.getServletPath();
        System.out.println("Servlet Path: " + servletPath);

        boolean matchFound = openApiEndpoints.stream()
                .noneMatch(uri -> {
                    System.out.println("uri : "+uri);
                    return servletPath.contains(uri);
                });

        System.out.println("Match Found: " + matchFound);
        return matchFound;
    };*/

}
