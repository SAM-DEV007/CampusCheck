<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
    <title>Student Portal</title>
    <style>
        body {
            font-family: sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background-color: #f0f0f0;
            margin: 0;
            background-image: url("../../images/academic.avif");
            background-size: cover;
            background-position: center;
        }

        .container {
            text-align: center;
            padding: 60px;
            width: 500px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 5px 5px rgba(0, 0, 0, 0.1);
            border: 1px solid #ccc;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h2 {
            font-size: 28px;
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        /* Profile Section Styling */
        .profile-section {
            position: fixed;
            top: 20px;
            right: 20px;
            width: 90px;
            height: 90px;
            border-radius: 50%;
            overflow: hidden;
            background-color: #fff;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border: 5px solid #ccc;
        }

        .profile-section img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .content {
            position: absolute;
            background-color: #fff;
            top: 25px;
            right: 70px;
            width: 150px;
            height: 70px;
            font-size: 10px;
            text-align: left;
            padding: 5px;
            padding-left: 20px;
        }

        .content button {
            padding: 5px 10px;
            font-size: 15px;
            background-color: black;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="content">
    <% String role = (String) session.getAttribute("role"); %>
    <h1><%= session.getAttribute("username")%></h1>
    <h3><%= role.substring(0,1).toUpperCase() + role.substring(1).toLowerCase() %></h3>

    <form action="logout">
        <button type="submit">Logout</button>
    </form>
</div>

<div class="profile-section">
    <img src="../../images/profile.jpg" alt="Profile">
</div>

<div class="container">
    <h2>Student Portal</h2>

    <table>
        <thead>
        <tr>
            <th>Subject</th>
            <th>Classes Attended</th>
            <th>Total Classes</th>
            <th>Percentage</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><%= session.getAttribute("subject") %></td>
            <td><%= request.getAttribute("present") %></td>
            <td><%= request.getAttribute("totalClasses") %></td>
            <td><%= request.getAttribute("percentage") %>%</td>
        </tr>
        </tbody>
    </table>

    <table>
        <thead>
        <tr>
            <th>Class</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <% ArrayList<String> attendance = (ArrayList<String>) request.getAttribute("attendanceList"); %>
        <% for (int i = 0; i < attendance.size(); i++) { %>
            <tr>
                <td><%= i + 1 %></td>
                <td><%= attendance.get(i) %></td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>
