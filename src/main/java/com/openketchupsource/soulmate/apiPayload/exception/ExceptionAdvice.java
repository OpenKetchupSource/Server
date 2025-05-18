package com.openketchupsource.soulmate.apiPayload.exception;

import com.openketchupsource.soulmate.apiPayload.ApiResponse;
import com.openketchupsource.soulmate.apiPayload.exception.handler.LoginHandler;
import com.openketchupsource.soulmate.apiPayload.form.ReasonDTO;
import com.openketchupsource.soulmate.apiPayload.form.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 전역 예외 처리 담당
 * @RestController에서 발생하는 예외를 전역적으로 잡아냄
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    // 주로 @RequestParam, @PathVariable에 대한 유효성 검사 실패 시 발생
    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), HttpHeaders.EMPTY,request);
    }

    // @RequestBody에 대한 @Valid 유효성 검증 실패 처리
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(e,HttpHeaders.EMPTY, ErrorStatus.valueOf("_BAD_REQUEST"),request,errors);
    }

    // 예상하지 못한 일반적인 예외 처리 :모든 에러를 500 응답으로 보내고 에러 메시지(혹은 예외 타입)를 result로 넘김
    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
        e.printStackTrace();

        return handleExceptionInternalFalse(e, ErrorStatus._INTERNAL_SERVER_ERROR, HttpHeaders.EMPTY, ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(),request, e.getMessage());
    }

    // 커스텀 예외 처리
    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity onThrowException(GeneralException generalException, HttpServletRequest request) {
        ReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();
        Object result = (generalException instanceof LoginHandler) ? ((LoginHandler) generalException).getResult() : null;

        ApiResponse<Object> body = ApiResponse.onFailure(
                errorReasonHttpStatus.getCode(),
                errorReasonHttpStatus.getMessage(),
                result
        );

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                generalException,
                body,
                HttpHeaders.EMPTY,
                errorReasonHttpStatus.getHttpStatus(),
                webRequest
        );

    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ReasonDTO reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(),reason.getMessage(),null);
//        e.printStackTrace();

        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonErrorStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonErrorStatus.getCode(), errorCommonErrorStatus.getMessage(),errorPoint);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonErrorStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonErrorStatus.getCode(), errorCommonErrorStatus.getMessage(),errorArgs);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonErrorStatus.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonErrorStatus,
                                                                     HttpHeaders headers, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorCommonErrorStatus.getCode(), errorCommonErrorStatus.getMessage(), null);
        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonErrorStatus.getHttpStatus(),
                request
        );
    }
}