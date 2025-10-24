package com.swyp.wedding.security.user;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        //DB에서 조회
        User user = userRepository.findByUserId(userId);

        if(user != null){
            //UserDetails에 담아서 return하면 AutneticationManager가 검증
            return new CustomUserDetails(user);
        }
        return null;
    }
}
