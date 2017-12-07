package yb.java.beer.authorization.spring.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author yavor bachev
 * @since 0.1
 * 2017 Dec
 */



@Entity
@Table(name="APP_USER")
public class User {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="APP_USER_ID", referencedColumnName="ID")
    private List<UserRole> roles;

    public User() { }

    public User(Long id, String username, String password, List<UserRole> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<UserRole> getRoles() {
        return roles;
    }
}
