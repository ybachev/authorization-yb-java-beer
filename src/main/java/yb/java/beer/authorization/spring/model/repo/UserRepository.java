package yb.java.beer.authorization.spring.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yb.java.beer.authorization.spring.model.User;

import java.util.Optional;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String userName);
}
