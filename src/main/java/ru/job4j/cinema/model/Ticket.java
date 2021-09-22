package ru.job4j.cinema.model;

public class Ticket {

    private int id;

    private final int sessionId;

    private final int row;

    private final int cell;

    private final Account account;

    public Ticket(int sessionId, int row, int cell) {
        this(sessionId, row, cell, new Account("", "", ""));
    }

    public Ticket(int sessionId, int row, int cell, Account account) {
        this.sessionId = sessionId;
        this.row = row;
        this.cell = cell;
        this.account = account;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    public Account getAccount() {
        return account;
    }
}
