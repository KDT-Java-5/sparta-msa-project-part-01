package com.sparta.msa.project.global.exception;

import com.sparta.msa.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
  private static final String NOT_FOUND = "NOT_FOUND";
  private static final String METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
  private static final String DATA_INTEGRITY_VIOLATION = "DATA_INTEGRITY_VIOLATION";
  private static final String SERVER_ERROR = "SERVER_ERROR";

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException ex) {
    log.warn("[DomainException] : code={}, message={}", ex.getCode(), ex.getMessage());
    return ApiResponse.fail(ex.getHttpStatus(), ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    String errorMessage = extractErrorMessages(ex);
    log.warn("[ValidationException] : {}", errorMessage);
    return ApiResponse.fail(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, errorMessage);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
    String errorMessage = extractErrorMessages(ex);
    log.warn("[BindException] : {}", errorMessage);
    return ApiResponse.fail(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, errorMessage);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    log.warn("[HttpMessageNotReadableException] : {}", ex.getMessage());
    return ApiResponse.fail(HttpStatus.BAD_REQUEST, VALIDATION_ERROR, "요청 본문의 형식이 올바르지 않습니다.");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    log.warn("[MethodArgumentTypeMismatchException] : {}", ex.getMessage());
    return ApiResponse.fail(HttpStatus.BAD_REQUEST, VALIDATION_ERROR,
        "'" + ex.getName() + "' 파라미터의 형식이 올바르지 않습니다.");
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
      NoResourceFoundException ex) {
    return ApiResponse.fail(HttpStatus.NOT_FOUND, NOT_FOUND, "요청한 경로를 찾을 수 없습니다.");
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex) {
    return ApiResponse.fail(HttpStatus.METHOD_NOT_ALLOWED, METHOD_NOT_ALLOWED, ex.getMessage());
  }

  // 애플리케이션 검증을 통과했더라도 DB 제약 조건(CHECK, FK)에 걸린 경우의 최종 방어선
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    log.warn("[DataIntegrityViolationException] : {}", ex.getMostSpecificCause().getMessage());
    return ApiResponse.fail(HttpStatus.CONFLICT, DATA_INTEGRITY_VIOLATION, "데이터 제약 조건에 위배되는 요청입니다.");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
    log.error("[Exception] : ", ex);
    String message = ex.getMessage() != null ? ex.getMessage() : "서버 오류가 발생하였습니다.";
    return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, SERVER_ERROR, message);
  }

  private String extractErrorMessages(BindException ex) {
    return ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
  }
}
