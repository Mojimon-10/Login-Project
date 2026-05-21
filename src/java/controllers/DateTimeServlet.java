/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

/**
 *
 * @author Marc Jomerick Lo
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Locale;

public class DateTimeServlet implements ServletContextListener 
{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) 
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
        String formattedDate = currentDateTime.format(dateFormatter);
        String formattedTime = currentDateTime.format(timeFormatter);

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("currentDate", formattedDate);
        servletContext.setAttribute("currentTime", formattedTime);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) 
    {}
}
