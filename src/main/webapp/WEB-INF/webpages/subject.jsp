<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>Subjects</title>
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
            margin-bottom: 30px;
        }

        select {
            padding: 15px;
            font-size: 15px;
            border: 1px solid #ccc;
            border-radius: 3px;
            margin-bottom: 15px;
        }

        button {
            padding: 15px 30px;
            font-size: 18px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        .profile-section {
            position: absolute;
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
    <h2>Select Subject</h2>
    <form action="home" method="post">
        <label for="subject">Subject:</label>
        <select id="subject" name="subject" required>
            <option value="">-- Select --</option>

            <% ArrayList<String> subjects = (ArrayList<String>) request.getAttribute("subjectList"); %>
            <% for (int i = 0; i < subjects.size(); i++) { %>
                <% String subject = subjects.get(i); %>
                <option value=<%= subject %>> <%= subject %> </option>
            <% } %>
        </select>
        <br>

        <% String button_text = ""; %>
        <% if (role.equals("student")) button_text = "View"; %>
        <% if (role.equals("teacher")) button_text = "Mark"; %>
        <button type="submit"><%= button_text %> Attendance</button>
    </form>
</div>

</body>
</html>
