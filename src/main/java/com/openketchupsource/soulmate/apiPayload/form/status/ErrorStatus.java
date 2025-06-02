package com.openketchupsource.soulmate.apiPayload.form.status;

import com.openketchupsource.soulmate.apiPayload.form.BaseCode;
import com.openketchupsource.soulmate.apiPayload.form.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 가장 일반적인 응답 : COMMON
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NULL_JSON(HttpStatus.BAD_REQUEST, "DIARY4004", "JSON에 내용이 존재하지 않습니다."),

    // kakao 로그인 관련 에러
    KAKAO_INFORM_NOT_EXIST(HttpStatus.BAD_REQUEST, "KAKAO4001", "카카오 사용자 정보가 누락되었습니다."),
    KAKAO_INVALID_CODE(HttpStatus.BAD_REQUEST, "KAKAO4002", "카카오 인가 코드가 유효하지 않습니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "해당 ID의 사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수입니다."),

    // 캐릭터 관련 에러
    CHARACTER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHARACTER4001", "해당 ID의 캐릭터가 없습니다."),

    // 일기 관련 에러
    DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY4001", "일기가 없습니다."),
    TITLE_NOT_EXIST(HttpStatus.BAD_REQUEST, "DIARY4002", "제목은 필수입니다."),
    COMMENT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "DIARY4003", "이미 코멘트가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}