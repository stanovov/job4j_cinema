package ru.job4j.cinema.model;

public class Account {

    private int id;

    private final String username;

    private final String email;

    private final String phone;

    public Account(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
