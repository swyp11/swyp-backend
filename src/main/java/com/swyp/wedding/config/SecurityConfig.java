package com.swyp.wedding.config;

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
                        // OAuth 추가정보 입력 API (로그인 후 접근)
                        .requestMatchers("/api/users/oauth-info").authenticated()
                        // TODO 관리자의 경우
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 로그인한 유저의 경우
                        .requestMatchers( "/api/schedule/**").hasRole("USER")

                        // 인증이 필요없는 경로
                        .requestMatchers(
                                "/api/users/join",
                                "/api/users/email-auth",
                                "/api/users/email-auth/verify",
                                "/api/auth/login",
                                "/api/auth/oauth/**",
                                "/api/password/reset/**"
                        ).permitAll()
                        .requestMatchers("/api/swagger-ui/**", "/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
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
