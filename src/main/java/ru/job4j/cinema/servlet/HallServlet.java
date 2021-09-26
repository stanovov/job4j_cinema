package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.cinema.model.Account;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.store.PsqlStore;
import ru.job4j.cinema.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HallServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        int sessionId = Integer.parseInt(req.getParameter("id_session"));
        String json = GSON.toJson(
                PsqlStore.instOf().findAllTicketsBySessionId(sessionId)
        );
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8));
        Ticket ticket = GSON.fromJson(req.getReader(), Ticket.class);
        Account account = ticket.getAccount();
        Store store = PsqlStore.instOf();
        Account foundAcc = store.findAccountByPhone(account.getPhone());
        if (foundAcc != null) {
            account.setId(foundAcc.getId());
        }
        store.save(account);
        if (account.getId() == 0) {
            writer.print("409 Conflict");
            writer.flush();
            return;
        }
        store.save(ticket);
        if (ticket.getId() == 0) {
            writer.print("409 Conflict");
            writer.flush();
            return;
        }
        writer.print("200 OK");
        writer.flush();
    }
}
