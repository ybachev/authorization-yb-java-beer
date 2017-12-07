package yb.java.beer.authorization.spring.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import yb.java.beer.authorization.spring.security.UserContext;

import java.util.Collection;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1023L;

    private RawAccessToken rawAccessToken;
    private UserContext userContext;

    public JwtAuthenticationToken(RawAccessToken unsafeToken) {
        super(null);
        this.rawAccessToken = unsafeToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                            "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return rawAccessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawAccessToken = null;
    }
}