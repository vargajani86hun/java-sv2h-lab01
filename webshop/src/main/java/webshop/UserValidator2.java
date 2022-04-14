package webshop;

import java.util.Optional;

public class UserValidator2 {

    private UserDao userDao;

    public UserValidator2(UserDao userDao) {
        this.userDao = userDao;
    }

    public void validateUserName(String userName) {
        validateUserNameEmpty(userName);
        if (userDao.findUserByName(userName).isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
    }

    public void validateNewUserName(String userName) {
        validateUserNameEmpty(userName);
        if (userDao.findUserByName(userName).isPresent()) {
            throw new IllegalArgumentException("Name is already taken!");
        }
    }

    public void validateUserNameEmpty(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User name should not be empty!");
        }
    }

    public void validatePassword(String password) {
        validatePasswordEmpty(password);
        if (password.toLowerCase().equals(password) || password.toUpperCase().equals(password)) {
            throw new IllegalArgumentException("Password should contain lowercase and uppercase characters too!");
        }
    }

    public void validatePasswordEmpty(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password should not be empty!");
        }
    }

    public void validatePasswordLogIn(String userName, String password, User userFound) {
        validatePasswordEmpty(password);
        if (userFound.getPassword() != (userName + password).hashCode()) {
            throw new IllegalArgumentException("Password is Invalid!");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("E-mail address should not be empty!");
        }

        if (email.length() < 3) {
            throw new IllegalArgumentException("It's too short to be valid e-mail address");
        }

        if (email.indexOf('@') < 1) {
            throw new IllegalArgumentException("E-mail address should contain '@' character!");
        }

        if (email.lastIndexOf('.') < email.indexOf('@')) {
            throw new IllegalArgumentException("E-mail address should contain a domain at its end.");
        }
    }

}
