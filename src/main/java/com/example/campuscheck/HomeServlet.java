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
            String username = (String) session.getAttribute("username");
            String role = (String) session.getAttribute("role");

            if (role.equalsIgnoreCase("student"))
                request.getRequestDispatcher("index.jsp").forward(request, response);
                //response.sendRedirect("index.jsp");
            else if (role.equalsIgnoreCase("teacher"))
                request.getRequestDispatcher("teacher.jsp").forward(request, response);
                //response.sendRedirect("/teacher");
            else
                //request.getRequestDispatcher("LoginServlet").forward(request, response);
                response.sendRedirect("/login");
        } else {
            //request.getRequestDispatcher("LoginServlet").forward(request, response);
            response.sendRedirect("/login");
        }
    }
}
