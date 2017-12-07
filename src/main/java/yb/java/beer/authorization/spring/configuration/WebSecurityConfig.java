package yb.java.beer.authorization.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import yb.java.beer.common.CustomCorsFilter;
import yb.java.beer.authorization.spring.security.JwtAuthenticationProvider;
import yb.java.beer.authorization.spring.security.JwtTokenAuthenticationProcessingFilter;
import yb.java.beer.authorization.spring.security.LocalAuthenticationProvider;
import yb.java.beer.authorization.spring.security.LoginProcessingFilter;
import yb.java.beer.authorization.spring.security.token.SkipPathRequestMatcher;
import yb.java.beer.authorization.spring.security.token.TokenExtractor;

import java.util.Arrays;
import java.util.List;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    public static final String AUTHENTICATION_URL = "/api/auth/login";

    public static final String REFRESH_TOKEN_URL = "/api/auth/token";

    public static final String API_ROOT_URL = "/api/**";

    @Autowired
    private LocalAuthenticationProvider localAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Bean
    protected BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(localAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(AUTHENTICATION_URL, REFRESH_TOKEN_URL);

        http.csrf().disable() // We don't need CSRF for JWT based authentication
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and().authorizeRequests().antMatchers(
                        permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
            .permitAll()

            .and().authorizeRequests()
            .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points

            .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL), UsernamePasswordAuthenticationFilter.class);
    }

    private LoginProcessingFilter buildAjaxLoginProcessingFilter(String authenticationUrl) {
        LoginProcessingFilter filter = new LoginProcessingFilter(authenticationUrl, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
                        = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
}
