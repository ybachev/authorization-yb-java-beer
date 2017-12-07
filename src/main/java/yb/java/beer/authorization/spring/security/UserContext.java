package yb.java.beer.authorization.spring.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
public class UserContext {
    private final String username;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public static UserContext create(String username, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
