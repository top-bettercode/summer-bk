package top.bettercode.simpleframework.security.server;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.util.CollectionUtils;
import top.bettercode.simpleframework.web.RespEntity;
import top.bettercode.simpleframework.web.error.AbstractErrorHandler;

/**
 * @author Peter Wu
 */
@Deprecated
public class SecurityOAuth2ErrorHandler extends AbstractErrorHandler {

  public SecurityOAuth2ErrorHandler(MessageSource messageSource,
      HttpServletRequest request) {
    super(messageSource, request);
  }

  @Override
  public void handlerException(Throwable error, RespEntity<?> respEntity,
      Map<String, String> errors, String separator) {
    if (error instanceof InvalidTokenException) {
      int httpErrorCode = ((OAuth2Exception) error).getHttpErrorCode();
      respEntity.setHttpStatusCode(httpErrorCode);
      respEntity.setMessage("Invalid access token");
    } else if (error instanceof OAuth2Exception) {
      int httpErrorCode = ((OAuth2Exception) error).getHttpErrorCode();
      Throwable cause = error.getCause();
      if (cause instanceof InternalAuthenticationServiceException) {
        cause = cause.getCause();
      }
      if (cause instanceof IllegalUserException || cause instanceof IllegalArgumentException) {
        httpErrorCode = HttpStatus.BAD_REQUEST.value();
      }
      if (cause instanceof IllegalUserException) {
        Map<String, String> userErrors = ((IllegalUserException) cause).getErrors();
        if (!CollectionUtils.isEmpty(userErrors)) {
          errors.putAll(userErrors);
        }
      }
      respEntity.setHttpStatusCode(httpErrorCode);

      Map<String, String> additionalInformation = ((OAuth2Exception) error)
          .getAdditionalInformation();
      if (additionalInformation != null) {
        respEntity.setErrors(additionalInformation);
      }
    }
  }
}
