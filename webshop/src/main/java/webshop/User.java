package webshop;

public class User {

    private long id;
    private String name;
    private int password;
    private  String email;
    private Cart cart;
    private boolean logIn;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = (name + password).hashCode();
        this.email = email;
    }

    public User(long id, String name, int password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Cart getCart() {
        return cart;
    }

    public boolean isLogIn() {
        return logIn;
    }

    public void setLogIn(boolean logIn) {
        this.logIn = logIn;
    }
}