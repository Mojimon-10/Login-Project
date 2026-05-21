<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html> 
<head>
    <title>Error 2</title>
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
        }

        .centered-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start; 
            height: 100vh;
            font-family: Verdana;
            color: white; 
            margin-top: 5vh; 
        }

        h1 {
            font-size: 36px;
            margin-bottom: 10px;
            color: firebrick;
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

        .go-back-button {
            padding: 10px 20px;
            background-color: #5690E2;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
        }

        .go-back-button:hover {
            background: linear-gradient(to bottom left, #5690E2, #2DD7A0);
        }
    </style>
</head>
<body>
   <div class="dragon-bg"></div>
    <%@ include file="/header.jsp" %>
    <div class="centered-container">
        <h1>Error — Incorrect Password!</h1>
        <h2>The password you have entered is incorrect.</h2> 
        <div class="button-container">
            <a href="index.jsp" class="go-back-button"><strong>Go back to Login</strong></a>
        </div>
    </div>
    <footer>
        <%@ include file="/footer.jsp" %>
    </footer>
</body>
</html>
