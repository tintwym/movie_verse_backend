package dev.team08.movie_verse_backend.filter;

import jakarta.servlet.*;

import java.io.IOException;

// This filter is kept for backwards compatibility with WebConfig#sessionTimeoutFilter
// but is intentionally a pass-through. Authentication and session lifetime are
// handled entirely by JWTs (see JwtUtility / UserService), and the previous
// implementation guarded its redirect with `!requestURI.contains("/")` — a
// condition that is **always false** for any real URI, so the redirect never
// fired and the filter has always been a no-op in practice. Removing it
// outright would require touching WebConfig's registration too; leaving it as
// an honest pass-through keeps wiring stable without lying about what it does.
public class SessionTimeoutFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
