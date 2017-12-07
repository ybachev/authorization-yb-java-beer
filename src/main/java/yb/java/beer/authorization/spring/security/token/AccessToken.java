package yb.java.beer.authorization.spring.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

public final class AccessToken implements JwtToken {

    private final String rawToken;
    @JsonIgnore
    private Claims claims;

    public AccessToken(final String token, Claims claims) {
        this.rawToken = token;
        this.claims = claims;
    }

    public String getToken() {
        return this.rawToken;
    }

    public Claims getClaims() {
        return claims;
    }
}
