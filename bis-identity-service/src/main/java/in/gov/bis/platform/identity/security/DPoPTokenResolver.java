package in.gov.bis.platform.identity.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.util.StringUtils;

public class DPoPTokenResolver implements BearerTokenResolver {

    private static final String DPOP_PREFIX = "DPoP ";
    private final DefaultBearerTokenResolver delegate = new DefaultBearerTokenResolver();

    public DPoPTokenResolver() {
        delegate.setAllowFormEncodedBodyParameter(false);
        delegate.setAllowUriQueryParameter(false);
    }

    @Override
    public String resolve(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(DPOP_PREFIX)) {
            String token = authorization.substring(DPOP_PREFIX.length()).trim();
            return StringUtils.hasText(token) ? token : null;
        }
        return delegate.resolve(request);
    }
}
