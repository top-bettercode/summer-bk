package top.bettercode.summer.util.test

import org.springframework.core.Ordered
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Peter Wu
 */
class AutoDocFilter(
    private val handlers: List<AutoDocRequestHandler>?
) : OncePerRequestFilter(), Ordered {

    override fun getOrder(): Int {
        return Ordered.HIGHEST_PRECEDENCE
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!handlers.isNullOrEmpty()) {
            val servletRequest = AutoDocHttpServletRequest(request)
            handlers.filter { it.support(servletRequest) }.forEach { it.handle(servletRequest) }
            filterChain.doFilter(servletRequest, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}