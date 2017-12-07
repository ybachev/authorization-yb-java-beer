package yb.java.beer.authorization.spring.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yb.java.beer.authorization.spring.security.UserContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    public AccessToken createAccessJwtToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername()))
            throw new IllegalArgumentException("Cannot create JWT Token without username");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("User doesn't have any privileges");

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

        LocalDateTime currentTime = LocalDateTime.now();

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuer(settings.getTokenIssuer())
                           .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                           .setExpiration(Date.from(currentTime
                                                                    .plusMinutes(settings.getTokenExpirationTime())
                                                                    .atZone(ZoneId.systemDefault()).toInstant()))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                           .compact();

        return new AccessToken(token, claims);
    }

    public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuer(settings.getTokenIssuer())
                           .setId(UUID.randomUUID().toString())
                           .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                           .setExpiration(Date.from(currentTime
                                                                    .plusMinutes(settings.getRefreshTokenExpTime())
                                                                    .atZone(ZoneId.systemDefault()).toInstant()))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                           .compact();

        return new AccessToken(token, claims);
    }
}
