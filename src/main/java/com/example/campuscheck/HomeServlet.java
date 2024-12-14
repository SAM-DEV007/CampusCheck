package com.example.campuscheck;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("role") != null) {
            String role = (String) session.getAttribute("role");

            switch (role) {
                case "student": case "teacher":
                    request.getRequestDispatcher("WEB-INF/webpages/subject.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect("/login");
            }
        } else {
            response.sendRedirect("/login");
        }
    }
}
