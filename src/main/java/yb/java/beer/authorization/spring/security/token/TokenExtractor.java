package yb.java.beer.authorization.spring.security.token;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
public interface TokenExtractor {

    public String extract(String header);
}
