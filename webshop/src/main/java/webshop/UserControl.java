package webshop;

public class UserControl {

    private UserDao userDao;

    public UserControl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) {
        String name = user.getName();
        if (userDao.findUserByName(name).isEmpty()) {
            userDao.addUser(user);
        } else {
            throw new IllegalArgumentException("Name is already taken!");
        }
    }
}
