package exe.exe201be.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

public class CookieOrHeaderBearerTokenResolver implements BearerTokenResolver {
    private final String cookieName;
    public CookieOrHeaderBearerTokenResolver(String cookieName) { this.cookieName = cookieName; }

    @Override
    public String resolve(HttpServletRequest request) {
        // 1) Từ cookie
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (cookieName.equals(c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                    return c.getValue();
                }
            }
        }
        // 2) Fallback header Authorization
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);
        return null;
    }
}
