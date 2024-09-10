package SpringJWT.study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private int status;            // HTTP 상태 코드
    private String message;        // 에러 메시지
    private LocalDateTime timestamp;  // 에러 발생 시간
}
