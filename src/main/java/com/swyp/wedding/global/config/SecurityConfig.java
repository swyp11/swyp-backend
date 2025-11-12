package com.swyp.wedding.global.config;

import com.swyp.wedding.security.jwt.JwtFilter;
import com.swyp.wedding.security.jwt.JwtProvider;
import com.swyp.wedding.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //💡 h2db 데이터 확인 과정
        http
                .headers(header -> header
                        .frameOptions(frame -> frame.sameOrigin()));
        http
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/h2-console/**").permitAll());


        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        // Swagger UI 관련 엔드포인트 (정적 리소스 포함)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 인증이 필요없는 경로 (구체적인 경로부터 먼저)
                        .requestMatchers("/api/user/join", "/api/user/join/oAuth/extra-info").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/home", "/logout").permitAll()
                        // 조회 API (GET) - 인증 불필요
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/api/dress/**",
                                "/api/dress-shop/**",
                                "/api/makeup-shop/**",
                                "/api/wedding/**",
                                "/api/hall/**"
                        ).permitAll()
                        // TODO 관리자의 경우
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 로그인한 유저의 경우 (더 구체적인 경로를 뒤에 배치)
                        .requestMatchers("/api/user/**", "/api/schedule/**").hasRole("USER")
                        .anyRequest().authenticated());


        //JWTFilter 등록
        http
                .addFilterBefore(new JwtFilter(jwtUtil, jwtProvider), UsernamePasswordAuthenticationFilter.class);


        //세션 설정💡
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


}
