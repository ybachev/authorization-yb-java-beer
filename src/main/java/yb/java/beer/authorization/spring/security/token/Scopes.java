package yb.java.beer.authorization.spring.security.token;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
public enum Scopes {

    REFRESH_TOKEN;

    public String authority() {
        return this.name();
    }
}
