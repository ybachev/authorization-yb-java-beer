package yb.java.beer.authorization.spring.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import yb.java.beer.authorization.spring.configuration.WebSecurityConfig;
import yb.java.beer.exceptions.InvalidJwtToken;
import yb.java.beer.authorization.spring.model.User;
import yb.java.beer.authorization.spring.security.UserContext;
import yb.java.beer.authorization.spring.security.token.JwtSettings;
import yb.java.beer.authorization.spring.security.token.JwtToken;
import yb.java.beer.authorization.spring.security.token.JwtTokenFactory;
import yb.java.beer.authorization.spring.security.token.RawAccessToken;
import yb.java.beer.authorization.spring.security.token.RefreshToken;
import yb.java.beer.authorization.spring.security.token.TokenExtractor;
import yb.java.beer.authorization.spring.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

@RestController
public class RefreshTokenEndpoint {

    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    private JwtSettings jwtSettings;

    @Autowired
    private UserService userService;


    @RequestMapping(value="/api/auth/token", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
    public @ResponseBody
    JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessToken rawToken = new RawAccessToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

        /*String jti = refreshToken.getJti();
        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }*/

        String subject = refreshToken.getSubject();
        User user = userService.getByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
                                                 .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUsername(), authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }


}
