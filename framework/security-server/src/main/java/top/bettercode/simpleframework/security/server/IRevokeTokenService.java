package top.bettercode.simpleframework.security.server;

/**
 * @author Peter Wu
 */
public interface IRevokeTokenService {

  void revokeToken(Object principal, String access_token);
}
