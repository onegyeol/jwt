package SpringJWT.study.config;
import SpringJWT.study.exception.CustomAccessDeniedHandler;
import SpringJWT.study.exception.CustomAuthenticationEntryPoint;
import SpringJWT.study.jwt.JwtUtil;
import SpringJWT.study.service.CustomerUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //스프링 시큐리티 컨텍스트 설정임을 명시
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //API 보안 수준 설정
@AllArgsConstructor
public class SecurityConfig {
    private final CustomerUserDetailService customerUserDetailService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDenieHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/member/**", "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //CSRF, CORS
        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults()); // 다른 도메인의 웹페이지에서 리소스에 접근할 수 있도록 허용

        // 세션 관리 상태 없음으로 구성, SpringSecurity가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        // FormLogin, BasicHttp 활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        // JWTAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        // JWTAuthFilter -> UsernamePasswordAUthenticationFilter 순서대로 진행. JWT 필터 먼저 거치도록 설정함
        http.addFilterBefore(new JwtAuthFilter(customerUserDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 인증, 인가 실패 시 ExceptionHandler 설정
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint) //인증 예외 핸들러 지정
                .accessDeniedHandler(accessDenieHandler) // 인가 예외 핸들러 지정
        );

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().permitAll()
        );

        return http.build();
    }
}
