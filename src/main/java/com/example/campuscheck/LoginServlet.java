package com.example.campuscheck;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null)
            //response.sendRedirect("login.jsp");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            //response.sendRedirect("login.jsp");
        else
            //response.sendRedirect("index.jsp");
            //request.getRequestDispatcher("index.jsp").forward(request, response);
            //request.getRequestDispatcher("HomeServlet").forward(request, response);
            response.sendRedirect("/home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username.equals("username") && password.equals("password")) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);
            //response.sendRedirect("index.jsp");
            //request.getRequestDispatcher("index.jsp").forward(request, response);
            //request.getRequestDispatcher("HomeServlet").forward(request, response);
            response.sendRedirect("/home");
        } else {
            //response.sendRedirect("login.jsp");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            //response.sendRedirect("login.jsp");
        }
    }
}
