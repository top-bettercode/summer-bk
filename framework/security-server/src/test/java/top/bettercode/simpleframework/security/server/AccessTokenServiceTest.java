package top.bettercode.simpleframework.security.server;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import top.bettercode.lang.util.StringUtil;
import top.bettercode.simpleframework.security.impl.TestApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Peter Wu
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class, webEnvironment = RANDOM_PORT)
public class AccessTokenServiceTest {

  @Deprecated
  @Autowired
  AccessTokenService accessTokenService;

  @Deprecated
  @Test
  public void getToken() {
    OAuth2AccessToken accessToken = accessTokenService.getAccessToken("root");
    System.err.println(StringUtil.valueOf(accessToken, true));
  }


}
