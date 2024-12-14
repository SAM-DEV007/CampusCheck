package com.example.campuscheck;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

import java.util.Enumeration;

@WebServlet("/submitAttendance")
public class SubmitAttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("role") != null) {
            String role = (String) session.getAttribute("role");
            String username = (String) session.getAttribute("username");

            Enumeration<String> students = (Enumeration<String>) request.getParameterNames();
            while (students.hasMoreElements()) {
                String student = students.nextElement();
                String status = request.getParameter(student);
                System.out.println(student + " " + status);
            }

            session.setAttribute("attendanceSubmitted", true);
        }

        response.sendRedirect("/home");
    }
}
