package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (InputStream in
                     = PsqlStore.class.getClassLoader().getResourceAsStream("db.properties")) {
            cfg.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Ticket> findAllTicketsBySessionId(int sessionId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM ticket WHERE session_id = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setInt(1, sessionId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("session_id"),
                            rs.getInt("row"),
                            rs.getInt("cell")
                    );
                    ticket.setId(rs.getInt("id"));
                    tickets.add(ticket);
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ALL TICKETS BY SESSION ID", e);
        }
        return tickets;
    }

    @Override
    public void save(Account account) {
        if (account.getId() == 0) {
            create(account);
        } else {
            update(account);
        }
    }

    @Override
    public void save(Ticket ticket) {
        try (Connection cn = pool.getConnection()) {
            try (PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO ticket(session_id, row, cell, account_id) VALUES (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, ticket.getSessionId());
                ps.setInt(2, ticket.getRow());
                ps.setInt(3, ticket.getCell());
                ps.setInt(4, ticket.getAccount().getId());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticket.setId(rs.getInt(1));
                    }
                }
            } catch (SQLIntegrityConstraintViolationException ignored) {
            }
        } catch (Exception e) {
            LOG.error("Database query failed", e);
        }
    }

    @Override
    public Account findAccountByPhone(String phone) {
        Account account = null;
        String query = "SELECT * FROM account WHERE phone = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setString(1, phone);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    account = new Account(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                    account.setId(rs.getInt("id"));
                    return account;
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed. FIND ACCOUNT BY PHONE", e);
        }
        return account;
    }

    private void create(Account account) {
        String query = "INSERT INTO account(username, email, phone) VALUES (?, ?, ?)";
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(query,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getEmail());
            statement.setString(3, account.getPhone());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Database query failed. CREATE ACCOUNT", e);
        }
    }

    private void update(Account account) {
        String query = "UPDATE account SET username = ?, email = ? WHERE id = ?";
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getEmail());
            statement.setInt(3, account.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            LOG.error("Database query failed. UPDATE ACCOUNT", e);
        }
    }
}
