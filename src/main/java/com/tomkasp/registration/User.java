package com.tomkasp.registration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(min = 5, message = Constants.USERNAME_LENGTH_ERROR)
    @Pattern(regexp = Constants.ALPHANUMERIC_REGEX, message = Constants.USERNAME_ALPHANUMERIC_ERROR)
    private String username;

    @Size(min = 8, message = Constants.PASSWORD_LENGTH_ERROR)
    @Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.PASSWORD_CHARACTERS_ERROR)
    private String password;


    public User id(final Integer id) {
        this.id = id;
        return this;
    }

    public User username(final String username) {
        this.username = username;
        return this;
    }

    public User password(final String password) {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
