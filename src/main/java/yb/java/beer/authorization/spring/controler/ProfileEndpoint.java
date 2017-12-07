package yb.java.beer.authorization.spring.controler;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import yb.java.beer.authorization.spring.security.UserContext;
import yb.java.beer.authorization.spring.security.token.JwtAuthenticationToken;


/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

@RestController
public class ProfileEndpoint {
    @RequestMapping(value="/api/me", method= RequestMethod.GET)
    public @ResponseBody
    UserContext get(JwtAuthenticationToken token) {
        return (UserContext) token.getPrincipal();
    }
}
