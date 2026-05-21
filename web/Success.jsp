<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="/header.jsp" %>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome Page</title>
    <style>
        body, html {
            height: 100%;
            overflow: hidden; 
        }

        body {
            background: linear-gradient(#1C1D31, #1C1D31);
            margin: 0; 
            padding: 0; 
            font-family: Verdana;
            color: white; 
        }

        .centered-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start; 
            height: 100vh;
            margin-top: 5vh; 
        }

        h1 {
            font-size: 36px;
            margin-bottom: 10px;
            color: #2DD7A0;
        }
        
        h2 {
            font-size: 20px;
            margin-bottom: 70px;
        }

        footer {
            position: fixed;
            bottom: 0;
            width: 100%;
            background-color: #1C1D31;
            color: white;
            text-align: center;
            padding: 5px 0;
            font-size: 12px; 
        }
        
        .button-container {
            margin-top: 20px;
        }

        .continue-button {
            padding: 10px 20px;
            background-color: #3399ff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            bottom: 30px;
        }

        .continue-button:hover {
            background: linear-gradient(to bottom left, #5690E2, #2DD7A0);
        }

        .logout-button {
            padding: 10px 20px;
            background-color: #3399ff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .logout-button:hover {
            background: linear-gradient(to bottom left, #ff6666, #ffcc99);
        }

    </style>
    
</head>
<body>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma","no-cache");
        response.setHeader("Expires","0");
        
        if (session.getAttribute("username") == null) 
        {
            response.sendRedirect("error_session.jsp");
        }
    %>
<div class="centered-container">
    <h1>Login Successful!</h1>
    <% 
        String username = (String) session.getAttribute("username");
        String fullName = (String) session.getAttribute("fullName");
        String role = (String) session.getAttribute("role");
        out.println("<h2>Welcome, " + fullName + "!</h2>");
        out.println("<p><strong>Username:</strong> " + username + "</p>");
        out.println("<p><strong>Your Role:</strong> " + role + "</p>");
    %>
    <form action="logout" method="post">
        <div class="button-container">
            <input type="submit" class="logout-button" value="Logout" style="font-weight: bold;">
        </div>
    </form>
    <div class="button-container">
        <button id="generatePDF" class="continue-button" style="font-weight: bold;">Generate Report</button>
    </div>
</div>

<script>
    document.getElementById("generatePDF").addEventListener("click", function() {
        
        window.location.href = "ReportServlet?type=admin";
        alert("PDF has been created.");
 
    });
</script>

<footer>
    <%@ include file="/footer.jsp" %>
</footer>
</body>
</html>