package top.bettercode.simpleframework.security.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口 权限标识
 *
 * @author Peter Wu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ConfigAuthority {

  /**
   * @return 需要的权限标识，有任意其中一个即可，不能为null或空白字符串
   */
  String[] value();

}
