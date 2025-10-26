package com.swyp.wedding.config;

import com.swyp.wedding.handler.CustomOAuth2FailureHandler;
import com.swyp.wedding.handler.CustomOAuth2SuccessHandler;
import com.swyp.wedding.security.jwt.JwtAuthenticationFilter;
import com.swyp.wedding.security.jwt.JwtFilter;
import com.swyp.wedding.security.jwt.JwtUtil;
import com.swyp.wedding.service.CustomOAuth2UserService;
import jakarta.servlet.http.Cookie;
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

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuthUserService;
    private final CustomOAuth2SuccessHandler customSuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

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
                        .requestMatchers("/", "/login", "/join" ,"/home", "/logout","/oauth2/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        //권한 확인 용
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().authenticated());


        //JWTFilter 등록
        http
                .addFilterBefore(new JwtFilter(jwtUtil), JwtAuthenticationFilter.class);


        //필터 추가 JwtAuthenticationFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
        http
                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);


        //oauth2
        http
                .oauth2Login((oauth)-> oauth
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuthUserService)))
                        .successHandler(customSuccessHandler)
                        .failureHandler(customOAuth2FailureHandler));

        // logout
        http.logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 요청 URL
                .logoutSuccessHandler((request, response, authentication) -> {
                    // ✅ JWT 쿠키 삭제
                    Cookie cookie = new Cookie("Authorization", null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);

                    // ✅ 로그인 페이지로 리다이렉트
                    response.sendRedirect("/login");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
        );


        //세션 설정💡
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


}
