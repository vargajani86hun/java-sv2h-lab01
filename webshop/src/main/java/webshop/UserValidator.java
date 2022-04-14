package webshop;

public class UserValidator {

    private UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    public void validateUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User name should not be empty!");
        }

        if (userDao.findUserByName(userName).isPresent()) {
            throw new IllegalArgumentException("Name is already taken!");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password should not be empty!");
        }

        if (password.toLowerCase().equals(password) || password.toUpperCase().equals(password)) {
            throw new IllegalArgumentException("Password should contain lowercase and uppercase characters too!");
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
