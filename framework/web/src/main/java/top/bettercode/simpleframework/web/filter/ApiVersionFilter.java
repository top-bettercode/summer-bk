package top.bettercode.simpleframework.web.filter;

import top.bettercode.simpleframework.config.WebProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 支持PUT DELETE form提交
 *
 * @author Peter Wu
 */
public class ApiVersionFilter extends OncePerRequestFilter implements Ordered {

  /**
   * Higher order to ensure the filter is applied before Spring Security.
   */
  public static final int DEFAULT_ORDER = -9900;
  private int order = DEFAULT_ORDER;

  private final WebProperties webProperties;

  public ApiVersionFilter(WebProperties webProperties) {
    this.webProperties = webProperties;
  }

  @Override
  public int getOrder() {
    return this.order;
  }

  /**
   * Set the order for this filter.
   *
   * @param order the order to set
   */
  public void setOrder(int order) {
    this.order = order;
  }

  @Override
  protected void doFilterInternal(final HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    response.setHeader(webProperties.getVersionName(), webProperties.getVersion());
    response.setHeader(webProperties.getVersionNoName(), webProperties.getVersionNo());
    filterChain.doFilter(request, response);
  }

}

