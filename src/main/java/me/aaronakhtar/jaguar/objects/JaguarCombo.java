package me.aaronakhtar.jaguar.objects;

public class JaguarCombo {

    private String username;
    private String password;

    public JaguarCombo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
