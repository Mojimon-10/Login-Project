<%@ page import="controllers.CaptchaServlet" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <style>
        body, html {
            height: 100%;
            overflow: hidden; 
        }

        body {
            background: linear-gradient(#1C1D31, #1C1D31);
            background: -webkit-linear-gradient(#1C1D31, #1C1D31);
            background: -o-linear-gradient(#1C1D31, #1C1D31);
            background: -moz-linear-gradient(#1C1D31, #1C1D31);
            background: -webkit-gradient(linear, left top, left bottom, from(#1C1D31), to(#233A54));
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
            margin-top: 1vh; 
        }

        h1 {
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        h2 {
            font-size: 20px;
            margin-bottom: 70px;
        }

        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin: 5px;
            border: 1px solid #ccc;
            border-radius: 3px;
            width: 100%; 
            box-sizing: border-box; 
        }

        label {
            width: 100px;
            display: inline-block; 
            text-align: left; 
            margin-left: 5px; 
            margin-bottom: 5px;
            font-weight: bold; 
        }

        .form-group {
            margin-bottom: 15px; 
        }

        input[type="submit"] {
            padding: 10px 20px;
            background: #3399ff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        
        input[type="submit"]:hover {
            background: linear-gradient(to bottom left, #5690E2, #2DD7A0);   
        }

        .button-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: 30px;
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

    </style>
</head>
<body>
    <%@ include file="/header.jsp" %>
    <% 
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        String captcha = CaptchaServlet.generateCaptcha(Integer.parseInt(getServletContext().getInitParameter("captchaLength")));
        session.setAttribute("captcha", captcha);
    %>
    
    <%       
        if (session.getAttribute("username") != null) 
        {
            session.invalidate();
            response.sendRedirect("index.jsp");
        }
    %>

    <div class="centered-container">
        <h1>Welcome to the Login Page!</h1>
        <h2>Please enter your credentials</h2>
        <form action="login" method="post">
            <div class="form-group">
                <label for="username"><b>Username:</b></label>
                <input type="text" id="username" name="username">
            </div>
            <div class="form-group">
                <label for="password"><b>Password:</b></label>
                <input type="password" id="password" name="password">
            </div>
            <div class="form-group">
                <label for="captcha"><b>CAPTCHA:</b></label>
                <span style="font-weight: bold; user-select: none; -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none; pointer-events: none;"><%= captcha %></span>
                <input type="text" id="captcha" name="captcha" onpaste="return false;" oncopy="return false;" oncut="return false;">
            </div>
            <div class="button-container">
                <input type="submit" value="Login" style="font-weight: bold;">
            </div>
        </form>
    </div>

    <footer>
        <%@ include file="/footer.jsp" %>
    </footer>
</body>
</html>