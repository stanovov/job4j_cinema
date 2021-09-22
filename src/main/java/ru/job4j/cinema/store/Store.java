package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;

public interface Store {
    Collection<Ticket> findAllTicketsBySessionId(int sessionId);
    void save(Account account);
    void save(Ticket ticket);
    Account findAccountByPhone(String phone);
}
