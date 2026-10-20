
package com.training;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        res.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null
                || email.isBlank() || password.isEmpty()
                || password.length() > 72) {
            res.setStatus(400);
            res.getWriter().write("Enter valid login details.");
            return;
        }

        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() &&
                    BCrypt.checkpw(password, rs.getString("password"))) {

                    res.getWriter().write("Login successful!");
                } else {
                    res.setStatus(401);
                    res.getWriter().write("Invalid email or password.");
                }
            }

        } catch (SQLException e) {
            log("Login failed", e);
            res.setStatus(500);
            res.getWriter().write("Something went wrong.");
        }
    }
}
