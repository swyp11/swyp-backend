package com.swyp.wedding.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "존재하지 않는 엔티티입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C006", "허용되지 않은 메소드입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "U003", "잘못된 아이디 또는 비밀번호입니다."),

    // Auth
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "만료된 토큰입니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "A003", "지원하지 않는 OAuth 제공자입니다."),
    OAUTH_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "A004", "OAuth 인증 처리 중 오류가 발생했습니다."),

    // Hall
    HALL_NOT_FOUND(HttpStatus.NOT_FOUND, "H001", "홀을 찾을 수 없습니다."),
    HALL_DELETE_FAILED(HttpStatus.CONFLICT, "H002", "홀 삭제에 실패했습니다. 참조 데이터가 존재합니다."),

    // WeddingHall
    WEDDING_HALL_NOT_FOUND(HttpStatus.NOT_FOUND, "WH001", "웨딩홀을 찾을 수 없습니다."),
    WEDDING_HALL_DELETE_FAILED(HttpStatus.CONFLICT, "WH002", "웨딩홀 삭제에 실패했습니다. 참조 데이터가 존재합니다."),

    // Dress
    DRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "D001", "드레스를 찾을 수 없습니다."),
    DRESS_SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "D002", "드레스샵을 찾을 수 없습니다."),

    // MakeupShop
    MAKEUP_SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메이크업샵을 찾을 수 없습니다."),

    // Likes
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "찜 정보를 찾을 수 없습니다."),
    INVALID_LIKES_CATEGORY(HttpStatus.BAD_REQUEST, "L002", "잘못된 찜 카테고리입니다."),
    DUPLICATE_LIKES(HttpStatus.CONFLICT, "L003", "이미 찜한 항목입니다."),

    // Schedule
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "일정을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
