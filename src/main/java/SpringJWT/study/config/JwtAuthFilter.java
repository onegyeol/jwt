package SpringJWT.study.config;

import SpringJWT.study.jwt.JwtUtil;
import SpringJWT.study.service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 1번만 실행 보장

    private final CustomerUserDetailService customerUserDetailService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // JWT가 헤더에 있는 경우
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            // JWT 유효성 검사 (유효한 토큰인가?)
            if(jwtUtil.validateToken(token)){
                Long userId = jwtUtil.getUserId(token);

                // 유저와 토근 일치 시 userDetails 생성
                UserDetails userDetails = customerUserDetailService.loadUserByUsername(userId.toString()); // 유저가 해당 데이터베이스에 존재하는지

                if(userDetails != null){ //존재한다면
                    // UserDetails, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 현재 Request의 Security Context에 접근권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘김

    }
}
