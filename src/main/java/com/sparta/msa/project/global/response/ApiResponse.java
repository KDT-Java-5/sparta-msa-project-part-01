package com.sparta.msa.project.global.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 모든 API 응답 포맷.
 * <pre>
 * 성공: { "result": true,  "error": {}, "data": { ... } }
 * 실패: { "result": false, "error": { "code": "...", "message": "..." }, "data": {} }
 * </pre>
 */
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"result", "error", "data"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

  private static final Map<String, Object> EMPTY = Map.of();

  boolean result;
  Error error;
  T data;

  public static <T> ApiResponse<T> ok() {
    return ApiResponse.<T>builder()
        .result(true)
        .build();
  }

  public static <T> ApiResponse<T> ok(T data) {
    return ApiResponse.<T>builder()
        .result(true)
        .data(data)
        .build();
  }

  public static <T> ResponseEntity<ApiResponse<T>> fail(HttpStatus httpStatus, String errorCode,
      String errorMessage) {
    return ResponseEntity.status(httpStatus)
        .body(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(errorCode, errorMessage))
            .build());
  }

  public boolean isResult() {
    return result;
  }

  public Object getError() {
    return error != null ? error : EMPTY;
  }

  public Object getData() {
    return data != null ? data : EMPTY;
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class Error {

    String code;
    String message;

    public static Error of(String code, String message) {
      return new Error(code, message);
    }
  }
}
