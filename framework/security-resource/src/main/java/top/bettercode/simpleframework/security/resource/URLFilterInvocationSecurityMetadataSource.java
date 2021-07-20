package top.bettercode.simpleframework.security.resource;

import top.bettercode.logging.AnnotatedUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.bettercode.simpleframework.web.UserInfoHelper;

/**
 * 自定义权限过滤
 *
 * @author Peter Wu
 */
public class URLFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

  private final Map<AntPathRequestMatcher, Set<ConfigAttribute>> defaultConfigAttributes = new HashMap<>();
  private Map<AntPathRequestMatcher, Set<ConfigAttribute>> requestMatcherConfigAttributes;
  private final IResourceService securityService;

  // ~ Constructors
  // ===================================================================================================
  public URLFilterInvocationSecurityMetadataSource(
      IResourceService securityService,
      RequestMappingHandlerMapping handlerMapping,
      SecurityProperties securityProperties) {
    this.securityService = securityService;

    handlerMapping.getHandlerMethods().forEach((mappingInfo, handlerMethod) -> {
      //非匿名权限
      if (!AnnotatedUtils.hasAnnotation(handlerMethod, Anonymous.class) && !AnnotatedUtils
          .hasAnnotation(handlerMethod, ClientAuthorize.class)) {
        for (String pattern : mappingInfo.getPatternsCondition().getPatterns()) {
          if (!securityProperties.ignored(pattern)) {
            Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
            ConfigAuthority authority = AnnotatedUtils
                .getAnnotation(handlerMethod, ConfigAuthority.class);
            Set<ConfigAttribute> configAttributes = new HashSet<>();
            if (authority != null) {
              for (String s : authority.value()) {
                configAttributes.add(new SecurityConfig(s.trim()));
              }
            }
            if (methods.isEmpty()) {
              defaultConfigAttributes
                  .put(new AntPathRequestMatcher(pattern), configAttributes);
            } else {
              for (RequestMethod requestMethod : methods) {
                defaultConfigAttributes
                    .put(new AntPathRequestMatcher(pattern, requestMethod.name()),
                        configAttributes);
              }
            }
          }
        }
      }
    });
    bindConfigAttributes();
  }

  /**
   * @return 不再检查是否支持
   */
  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return Collections.emptyList();
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) {
    final HttpServletRequest request = ((FilterInvocation) object).getRequest();
    UserInfoHelper.put(request, AuthenticationHelper.getPrincipal());
    List<Match> matches = new ArrayList<>();
    Comparator<String> comparator = new AntPathMatcher()
        .getPatternComparator(getRequestPath(request));
    for (Map.Entry<AntPathRequestMatcher, Set<ConfigAttribute>> entry : requestMatcherConfigAttributes
        .entrySet()) {
      if (entry.getKey().matches(request)) {
        matches.add(new Match(comparator, entry.getKey().getPattern(), entry.getValue()));
      }
    }
    if (!matches.isEmpty()) {
      Collections.sort(matches);
      Match bestMatch = matches.get(0);
      if (matches.size() > 1) {
        Match secondBestMatch = matches.get(1);
        if (comparator.compare(bestMatch.path, secondBestMatch.path) == 0) {
          String m1 = bestMatch.path;
          String m2 = secondBestMatch.path;
          throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" +
              request.getRequestURL() + "': {" + m1 + ", " + m2 + "}");
        }
      }
      return bestMatch.configAttributes.isEmpty() ? SecurityConfig
          .createList("authenticated") : bestMatch.configAttributes;
    }
    return Collections.emptyList();
  }

  private String getRequestPath(HttpServletRequest request) {
    String url = request.getServletPath();

    if (request.getPathInfo() != null) {
      url += request.getPathInfo();
    }

    return url;
  }

  private static class Match implements Comparable<Match> {

    private final Comparator<String> comparator;
    private final String path;

    private final Collection<ConfigAttribute> configAttributes;

    private Match(Comparator<String> comparator, String path,
        Collection<ConfigAttribute> configAttributes) {
      this.comparator = comparator;
      this.path = path;
      this.configAttributes = configAttributes;
    }

    @Override
    public int compareTo(Match o) {
      return comparator.compare(path, o.path);
    }
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }

  protected void bindConfigAttributes() {
    requestMatcherConfigAttributes = new HashMap<>(defaultConfigAttributes);
    List<? extends IResource> allResources = securityService.findAllResources();
    for (IResource resource : allResources) {
      String ress = resource.getRess();
      ConfigAttribute configAttribute = new SecurityConfig(resource.getMark().trim());
      for (String api : ress.split(",")) {
        if (api.contains(":")) {
          String[] methodUrl = api.split(":");
          String method = methodUrl[0].toUpperCase();
          String url = methodUrl[1];
          for (String u : url.split("\\|")) {
            if (StringUtils.hasText(method)) {
              for (String m : method.split("\\|")) {
                Set<ConfigAttribute> authorities = requestMatcherConfigAttributes
                    .computeIfAbsent(new AntPathRequestMatcher(u, m),
                        k -> new HashSet<>());
                authorities.add(configAttribute);
              }
            } else {
              Set<ConfigAttribute> authorities = requestMatcherConfigAttributes
                  .computeIfAbsent(new AntPathRequestMatcher(u),
                      k -> new HashSet<>());
              authorities.add(configAttribute);
            }
          }
        } else {
          for (String u : api.split("\\|")) {
            Set<ConfigAttribute> authorities = requestMatcherConfigAttributes
                .computeIfAbsent(new AntPathRequestMatcher(u), k -> new HashSet<>());
            authorities.add(configAttribute);
          }
        }
      }
    }
  }

  /**
   * 刷新资源权限配置
   */
  public void refreshResuorces() {
    synchronized (this) {
      this.bindConfigAttributes();
    }
  }

}