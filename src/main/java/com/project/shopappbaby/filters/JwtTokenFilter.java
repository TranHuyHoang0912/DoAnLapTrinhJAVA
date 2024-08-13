package com.project.shopappbaby.filters;

import com.project.shopappbaby.components.JwtTokenUtil;
import com.project.shopappbaby.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
// lấy ra giá trị token
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //khong yeu cau token
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); //SC là mã trạng thái của Http mà máy chủ trả về
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if (phoneNumber != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber); // ở đây hiểu username là trường chính nằm đại diện cho đối tượng đăng nhập vào hệ thống
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response); //yêu cầu token
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }

    }
    private boolean isBypassToken(@NonNull  HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
    //eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZU51bWJlciI6IjAxMjM0NTY3OCIsInN1YiI6IjAxMjM0NTY3OCIsImV4cCI6MTc1NDYyNjY1N30.PCuwSbjAc3-iidArF6l32BDCe3i_YVKqHhDsJgjpWFg
}
