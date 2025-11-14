package com.swyp.wedding.service;

import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(UserRequest userRequest){

        // 1. 이메일 인증 여부 확인
        String key = "emailAuth:" + userRequest.getUserId(); // ID = EMAIL

        // 2. userId(아이디 = 이메일) 중복 확인
        if(userRepository.existsByUserId(userRequest.getUserId())){
            throw new IllegalStateException("해당 아이디가 이미 존재합니다.");
        }
        // 3. 비밀번호 검증
        if(!isValidPassword(userRequest.getPassword())){
            throw new IllegalArgumentException( "비밀번호는 문자 + 숫자 8~20자를 충족해야합니다.");
        }

        // 4. 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.getPassword());

        //User 객체 생성
        User user = User.builder()
                .userId(userRequest.getUserId()) // // ID = EMAIL
                .password(encodedPassword)
                .nickname(userRequest.getNickname())
                .birth(userRequest.getBirth())
                .weddingDate(userRequest.getWeddingDate())
                .weddingRole(userRequest.getWeddingRole())
                .auth(UserEnum.USER)
                .build();


        //중복이 없을 경우
        userRepository.save(user);

    }


    //비밀번호 검증 로직
    private boolean isValidPassword(String password) {
        if (password == null) return false;
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
        return password.matches(regex);

    }

    // OAuth 로그인/회원가입 시 추가정보 입력
    @Transactional
    public UserResponse OAuthJoinProcess(CustomUserDetails userDetails, OAuthExtraInfoRequest request) {

        User user = userRepository.findByUserId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        user.updateExtraInfo(request);
        return UserResponse.builder()
                .userId(user.getUserId())
                .auth(user.getAuth())
                .nickname(user.getNickname())
                .weddingDate(user.getWeddingDate())
                .weddingRole(user.getWeddingRole())
                .build();
    }
}
