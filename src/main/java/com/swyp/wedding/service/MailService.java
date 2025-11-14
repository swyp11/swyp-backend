package com.swyp.wedding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // 공통 메일 전송 로직
    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("hj_3963@naver.com");
        mailSender.send(message);
    }

    // 인증 메일
    public void sendAuthCode(String toEmail, String code) {
        send(toEmail, "회원가입 인증코드", "인증코드: " + code + "\n이 코드를 입력해주세요.");
    }

    // 비밀번호 재설정 메일
    public void sendResetPassword(String toEmail, String tempPw) {
        send(toEmail, "임시 비밀번호 안내", "임시 비밀번호: " + tempPw);
    }
}
