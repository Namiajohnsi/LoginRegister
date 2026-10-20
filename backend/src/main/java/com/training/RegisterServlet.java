
package com.training;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (name == null || email == null || password == null
                || name.isBlank() || email.isBlank()
                || password.length() < 8 || password.length() > 72) {
            res.setStatus(400);
            res.getWriter().write(
                "Enter valid details and a password of 8-72 characters."
            );
            return;
        }

        String sql = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(
                password, BCrypt.gensalt()
            );

            ps.setString(1, name.trim());
            ps.setString(2, email.trim().toLowerCase());
            ps.setString(3, hashedPassword);

            ps.executeUpdate();

            res.setStatus(201);
            res.getWriter().write("Registration successful!");

        } catch (SQLIntegrityConstraintViolationException e) {
            res.setStatus(409);
            res.getWriter().write("Email already registered.");

        } catch (SQLException e) {
            log("Registration failed", e);
            res.setStatus(500);
            res.getWriter().write("Something went wrong.");
        }
    }
}
