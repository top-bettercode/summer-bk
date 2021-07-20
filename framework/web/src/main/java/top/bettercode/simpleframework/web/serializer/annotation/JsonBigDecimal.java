package top.bettercode.simpleframework.web.serializer.annotation;

import top.bettercode.simpleframework.web.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Peter Wu
 * @since 0.1.15
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = BigDecimalSerializer.class)
public @interface JsonBigDecimal {

  /**
   * @return 小数位数
   */
  int scale() default 2;

  /**
   * @return 序列化为字符
   */
  boolean toPlainString() default false;

  /**
   * @return 当小数位为零时，是否精简小数位
   */
  boolean reduceFraction() default false;

}
