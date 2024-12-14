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
            if (role.equals("teacher")) {
                String username = (String) session.getAttribute("username");

                Enumeration<String> students = request.getParameterNames();
                insertAttendance(username, (String) session.getAttribute("subject"), students, request);

                session.setAttribute("attendanceSubmitted", true);
            }
        }

        response.sendRedirect("/home");
    }

    private void insertAttendance(String username, String subject, Enumeration<String> attendance, HttpServletRequest request) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campuscheck",
                    "root", "root");

            Statement statement;
            statement = conn.createStatement();

            ResultSet resultSet;

            String query = "INSERT INTO attendance (SSID, Class, Present) VALUES (?, ?, ?);";

            PreparedStatement preparedStatement = conn.prepareStatement(query);

            while (attendance.hasMoreElements()) {
                String student = attendance.nextElement();
                String status = request.getParameter(student);

                resultSet = statement.executeQuery("select ID from Student_Subject where StudentID = (select ID from Student where Name = '" + student + "') and SubjectID = (select SubjectID from Subject where Name = '" + subject + "') and TeacherID = (select ID from teacher where Name = '" + username + "');");
                resultSet.next();
                int ssid = resultSet.getInt("ID");

                resultSet.close();

                resultSet = statement.executeQuery("select max(Class) AS Class from attendance where SSID = " + ssid + ";");
                resultSet.next();

                int classNumber = resultSet.getInt("Class") + 1;
                resultSet.close();

                int present = (status.equals("Present")) ? 1 : 0;

                preparedStatement.setInt(1, ssid);
                preparedStatement.setInt(2, classNumber);
                preparedStatement.setInt(3, (status.equals("Present")) ? 1 : 0);

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            statement.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
