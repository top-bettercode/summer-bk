package top.bettercode.simpleframework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Peter Wu
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = -7941819415782111951L;

  public AccessDeniedException() {
    this("access.denied");
  }

  public AccessDeniedException(String message) {
    super(message);
  }
}
