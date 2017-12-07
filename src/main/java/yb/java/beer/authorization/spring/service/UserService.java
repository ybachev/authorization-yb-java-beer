package yb.java.beer.authorization.spring.service;

import yb.java.beer.authorization.spring.model.User;

import java.util.Optional;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
public interface UserService {
    public Optional<User> getByUsername(String username);
}
