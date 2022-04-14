package webshop;

public class UserValidator {

    private UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    public void validateExistUserName(String userName) {
        checkUserNameIsEmpty(userName);
        if (userDao.findUserByName(userName).isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
    }

    public void validateNewUserName(String userName) {
        checkUserNameIsEmpty(userName);

        if (userDao.findUserByName(userName).isPresent()) {
            throw new IllegalArgumentException("Name is already taken!");
        }
    }

    private void checkUserNameIsEmpty(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("User name should not be empty!");
        }
    }

    public void validatePassword(String password) {
        checkPasswordIsEmpty(password);

        if (password.toLowerCase().equals(password) || password.toUpperCase().equals(password)) {
            throw new IllegalArgumentException("Password should contain lowercase and uppercase characters too!");
        }
    }

    public void validatePasswordLogIn(String userName, String password, User userFound) {
        checkPasswordIsEmpty(password);
        if (userFound.getPassword() != (userName + password).hashCode()) {
            throw new IllegalArgumentException("Password is Invalid!");
        }
    }

    private void checkPasswordIsEmpty(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password should not be empty!");
        }
    }

    public void validateEmail(String email) {
        checkEmailIsEmpty(email);

        checkEmailIsUsed(email);

        validateEmailLength(email);

        checkEmailHasAtChar(email);

        checkEmailNotHasMoreAtChar(email);

        checkEmailHasDomain(email);
    }

    private void checkEmailHasDomain(String email) {
        if (email.lastIndexOf('.') < email.indexOf('@')) {
            throw new IllegalArgumentException("Email address should contain a domain at its end.");
        }
    }

    private void checkEmailNotHasMoreAtChar(String email) {
        if (email.indexOf('@') != email.lastIndexOf('@')) {
            throw new IllegalArgumentException("Email address should not contain more then one from '@' character!");
        }
    }

    private void checkEmailHasAtChar(String email) {
        if (email.indexOf('@') < 1) {
            throw new IllegalArgumentException("Email address should contain '@' character!");
        }
    }

    private void validateEmailLength(String email) {
        if (email.length() < 3) {
            throw new IllegalArgumentException("It's too short to be valid e-mail address");
        }
    }

    private void checkEmailIsEmpty(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email address should not be empty!");
        }
    }

    private void checkEmailIsUsed(String email) {
        if (userDao.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An user has been already registered with this email: " + email);
        }
    }
}
