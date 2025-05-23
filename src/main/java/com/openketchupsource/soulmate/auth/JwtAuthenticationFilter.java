package com.openketchupsource.soulmate.auth;

import com.openketchupsource.soulmate.auth.jwt.JwtTokenProvider;
import com.openketchupsource.soulmate.auth.jwt.JwtValidationType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * 모든 요청에 대해 JWT 토큰을 검사하고, 인증된 사용자 정보를 SecurityContext에 넣어주는 역할
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            final String token = getJwtFromRequest(request);
            if (jwtTokenProvider.validateToken(token) == JwtValidationType.VALID_JWT) {
                Long memberId = jwtTokenProvider.getUserFromJwt(token);
                MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(memberId);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info(authentication.toString());
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}