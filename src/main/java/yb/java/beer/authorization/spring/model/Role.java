package yb.java.beer.authorization.spring.model;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
public enum Role {
    ADMIN, MEMBER;

    public String authority() {
        return this.name();
    }
}
