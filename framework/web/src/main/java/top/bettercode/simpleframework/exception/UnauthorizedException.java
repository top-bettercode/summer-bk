package top.bettercode.simpleframework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Peter Wu
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = -5815843402866992074L;

  public UnauthorizedException() {
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
