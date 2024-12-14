package com.example.campuscheck;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"", "/login"})
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            request.setAttribute("error", "");
            request.getRequestDispatcher("WEB-INF/webpages/login.jsp").forward(request, response);
        }
        else
            response.sendRedirect("/home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String role = request.getParameter("role");
        String password;
        try {
            password = hashPassword(request.getParameter("password"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e.getMessage());
            password = "";
        }

        if (validateLogin(username, password, role)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            response.sendRedirect("/home");
        } else {
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("WEB-INF/webpages/login.jsp").forward(request, response);
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    private boolean validateLogin(String username, String password, String role) {
        boolean isValid = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campuscheck",
                    "root", "root");

            Statement statement;
            statement = conn.createStatement();

            ResultSet resultSet;
            resultSet = statement.executeQuery("SELECT * FROM " + role + " WHERE Name='" + username + "' AND Password='" + password + "'");

            isValid = resultSet.next();

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return isValid;
    }
}
