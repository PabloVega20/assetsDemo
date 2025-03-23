package interview.assets.demo.api;

import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleInvalidAssetException(Exception ex) {
    String trackingCode = UUID.randomUUID().toString();
    composeLog(trackingCode, ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of(
            "message", ex.getMessage(),
            "trackingCode", trackingCode
        ));
  }

  private void composeLog(String trackingCode, String message, Exception ex) {
    log.error("""
        /*
         ***************************************
         *           ERROR LOG START          \s
         ***************************************
         *  Tracking Code: {}
         *  Message: {}
         ****************************************
         *  Stack Trace: {}
         ****************************************
         *           ERROR LOG END             \s
         ****************************************
         */
        """, trackingCode, message, ex.getStackTrace());
  }


}
