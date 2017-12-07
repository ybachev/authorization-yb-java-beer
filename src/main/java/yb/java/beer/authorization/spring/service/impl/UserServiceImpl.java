package yb.java.beer.authorization.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yb.java.beer.authorization.spring.model.User;
import yb.java.beer.authorization.spring.model.repo.UserRepository;
import yb.java.beer.authorization.spring.service.UserService;

import java.util.Optional;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}
