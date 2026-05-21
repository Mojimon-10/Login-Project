package controllers;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class CaptchaServlet extends HttpServlet 
{
 
    static boolean checkCaptcha(String captcha, String user_captcha) 
    {
        return captcha.equals(user_captcha);
    }
    
    public static String generateCaptcha(int n) 
    {
        String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  
        StringBuilder captcha = new StringBuilder();
        Random rand = new Random();
        
        for (int i = 0; i < n; i++) 
        {
            int index = rand.nextInt(chrs.length());
            captcha.append(chrs.charAt(index));
        }
        
        return captcha.toString();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        response.setContentType("text/plain");
  
        ServletContext context = getServletContext();
        int captchaLength = Integer.parseInt(context.getInitParameter("captchaLength"));
 
        String captcha = generateCaptcha(captchaLength);
 
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captcha);

        response.getWriter().write(captcha);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {}
}