package com.example.campuscheck;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("role") != null) {
            String role = (String) session.getAttribute("role");
            String username = (String) session.getAttribute("username");

            ArrayList<String> subjects = getSubjects(username, role);
            request.setAttribute("subjectList", subjects);

            if (session.getAttribute("attendanceSubmitted") != null && (boolean) session.getAttribute("attendanceSubmitted") && role.equals("teacher")) {
                request.setAttribute("message", "Attendance marked successfully!");
                session.removeAttribute("attendanceSubmitted");
            } else
                request.setAttribute("message", "");

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        String subject = request.getParameter("subject");
        session.setAttribute("subject", subject);

        switch (role) {
            case "student":
                ArrayList<String> attendance = getStudentAttendance(username, subject);

                request.setAttribute("totalClasses", attendance.size());
                request.setAttribute("attendanceList", attendance);

                int present = 0;
                for (String s : attendance) {
                    if (s.equals("Present")) {
                        present++;
                    }
                }

                request.setAttribute("present", present);
                request.setAttribute("percentage", (int) Math.ceil((double) present / attendance.size() * 100));

                request.getRequestDispatcher("WEB-INF/webpages/student.jsp").forward(request, response);
                break;
            case "teacher":
                request.setAttribute("studentList", getStudentList(username, subject));

                request.getRequestDispatcher("WEB-INF/webpages/teacher.jsp").forward(request, response);
                break;
        }
    }

    private ArrayList<String> getSubjects(String username, String role) {
        ArrayList<String> subjects = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campuscheck",
                    "root", "root");

            Statement statement;
            statement = conn.createStatement();

            ResultSet resultSet;

            if (role.equals("student")) {
                resultSet = statement.executeQuery("select (select Name from Subject where Student_Subject.SubjectID = Subject.SubjectID) AS Subject from Student_Subject where StudentID = (select ID from student where Name = '" + username + "');");
                while (resultSet.next()) {
                    subjects.add(resultSet.getString("Subject"));
                }
                resultSet.close();
            } else if (role.equals("teacher")) {
                resultSet = statement.executeQuery("select distinct (select Name from Subject where Student_Subject.SubjectID = Subject.SubjectID) AS Subject from student_subject where TeacherID = (select ID from teacher where Name = '" + username + "');");
                while (resultSet.next()) {
                    subjects.add(resultSet.getString("Subject"));
                }
                resultSet.close();
            }

            statement.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return subjects;
    }

    private ArrayList<String> getStudentAttendance(String username, String subject) {
        ArrayList<String> attendance = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campuscheck",
                    "root", "root");

            Statement statement;
            statement = conn.createStatement();

            ResultSet resultSet;

            resultSet = statement.executeQuery("select Class, Present from attendance where SSID = (select ID from Student_Subject WHERE Student_Subject.SubjectID = (SELECT Subject.SubjectID from Subject WHERE Name = '" + subject + "') AND StudentID = (SELECT ID from Student where Name = '" + username + "')) order by Class ASC;");
            while (resultSet.next()) {
                int status = resultSet.getInt("Present");
                String statusString = (status == 1) ? "Present" : "Absent";

                attendance.add(statusString);
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return attendance;
    }

    private ArrayList<String> getStudentList(String username, String subject) {
        ArrayList<String> students = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/campuscheck",
                    "root", "root");

            Statement statement;
            statement = conn.createStatement();

            ResultSet resultSet;

            resultSet = statement.executeQuery("select (select Name from student where ID = StudentID) AS Student from student_subject where TeacherID = (select ID from teacher where Name = '" + username + "') and SubjectID = (select SubjectID from subject where Name = '" + subject + "');");
            while (resultSet.next()) {
                students.add(resultSet.getString("Student"));
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return students;
    }
}
