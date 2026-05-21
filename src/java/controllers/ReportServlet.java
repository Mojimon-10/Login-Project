package controllers;

import com.itextpdf.text.Document;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class ReportServlet extends HttpServlet 
{

    private String DB_USER;
    private String DB_PASSWORD;
    private String DB_URL;

    private Cipher cipher;
    private SecretKeySpec secretKey;
 
    public void init(ServletConfig config) throws ServletException 
    {
        super.init(config);
        DB_USER = config.getInitParameter("DB_USER");
        DB_PASSWORD = config.getInitParameter("DB_PASSWORD");
        DB_URL = config.getInitParameter("DB_URL");

        String keyString = config.getInitParameter("encryptionKey");
        byte[] key = keyString.getBytes();
        
        String cipherAlgorithm = config.getInitParameter("CipherAlgorithm");

        try 
        {
            secretKey = new SecretKeySpec(key, "AES");
            cipher = Cipher.getInstance(cipherAlgorithm);
        } 
        
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) 
        {
            throw new ServletException("Error initializing encryption", e);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, DocumentException, Exception 
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) 
        {
            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("role");
            String username = (String) session.getAttribute("username");

            if (role.equalsIgnoreCase("admin")) 
            {
                generatePDFAdmin(session);
            }
            
            else
            {
                generatePdfForGuest(session, username) ;
            }
 
            response.sendRedirect(request.getContextPath() + "/Success.jsp");
        }
    }

    public void generatePDF() 
    {
        Document document = new Document();
        try 
        {
            String baseDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
            String fileName = "test.pdf";
            String filePath = baseDir + fileName;
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            Paragraph paragraph = new Paragraph();
            paragraph.add("created from servlet");
            document.add(paragraph);
            document.close();
        } 
        
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    class MyFooter extends PdfPageEventHelper 
    {
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        String owner;        
        int totalPages;

        public MyFooter(String owner, int totalPages) 
        {
            this.owner = owner;
            this.totalPages = totalPages;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) 
        {
            PdfPTable footer = new PdfPTable(2);
            try 
            {
                footer.setWidths(new int[]{24, 76});
                footer.setTotalWidth(527);
                footer.setLockedWidth(true);
                footer.getDefaultCell().setFixedHeight(20);
                footer.getDefaultCell().setBorder(Rectangle.TOP);
                footer.addCell(new Phrase("Owner: " + owner, footerFont));
                footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                footer.addCell(new Phrase(String.format("Page %d of %d", writer.getPageNumber(), totalPages), footerFont));
                footer.writeSelectedRows(0, -1, 34, 30, writer.getDirectContent());
            } 
            
            catch (DocumentException de) 
            {
                throw new ExceptionConverter(de);
            }
        }
    }

    private ArrayList<String[]> getAllRecordsFromDatabase() throws SQLException 
    {
        ArrayList<String[]> records = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
            String query = "SELECT username, role FROM USER_INFO";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) 
            {
                try (ResultSet resultSet = preparedStatement.executeQuery()) 
                {
                    while (resultSet.next()) 
                    {
                        String username = resultSet.getString("username");
                        String role = resultSet.getString("role");
                        records.add(new String[]{username, role});
                    }
                }
            }
        }
        
        return records;
    }

    public void generatePDFAdmin(HttpSession session) throws DocumentException, IOException, SQLException 
    {
        Document document = new Document(PageSize.LETTER);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String baseDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
        String fileName = "adminData_" + timeStamp + ".pdf";
        String filePath = baseDir + fileName;
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        int totalPages = 2;
        MyFooter footer = new MyFooter("admin", totalPages);
        writer.setPageEvent(footer);
        writer.setPageEvent(footer);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLDITALIC);
        Paragraph title = new Paragraph("Type of Report: Admin Data", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        ArrayList<String[]> records = null;

        records = getAllRecordsFromDatabase();

        PdfPTable table = new PdfPTable(3); // Three columns: Number, Username, and Role
        Font columnHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        table.addCell(new Phrase("", columnHeaderFont));
        table.addCell(new Phrase("Username", columnHeaderFont));
        table.addCell(new Phrase("Role", columnHeaderFont));

        int counter = 1;
        for (String[] record : records) 
        {
            table.addCell(String.valueOf(counter++)); 
            table.addCell(record[0]); 
            table.addCell(record[1]); 
        }

        document.add(table);
        document.close();
    }

    private String getPasswordForUsername(String username) throws SQLException, Exception 
    {
        String encryptedPassword = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
            String query = "SELECT password FROM USER_INFO WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) 
            {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) 
                {
                    if (resultSet.next()) 
                    {
                        encryptedPassword = resultSet.getString("password");
                        String decryptedPassword = decrypt(encryptedPassword);
                        return decryptedPassword;
                    }
                }
            }
        }
        return null;
    }

    private void generatePdfForGuest(HttpSession session, String username) throws DocumentException, IOException, SQLException, Exception {
        Rectangle pageSize = new Rectangle(600, 144); // Custom size
        Document document = new Document(pageSize);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String baseDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
        String fileName = "guestData_" + username + "_" + timeStamp + ".pdf";
        String filePath = baseDir + fileName;
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        int totalPages = 1; 
        MyFooter footer = new MyFooter(username, totalPages);
        writer.setPageEvent(footer);
        writer.setPageEvent(footer);
        document.open();
        Font boldItalic = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLDITALIC);
        Paragraph title = new Paragraph("Type of Report: Guest Data", boldItalic);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph userData = new Paragraph("Username: " + username, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
        document.add(userData);

        String password = getPasswordForUsername(username);
        if (password != null) 
        {
            Paragraph passwordData = new Paragraph("Password: " + password, new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
            document.add(passwordData);
        } 
        
        else 
        {
            Paragraph passwordData = new Paragraph("Password: Not available", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
            document.add(passwordData);
        }

        document.close();
    }
    
        private String decrypt(String strToDecrypt) throws Exception 
    {
        String decryptedString = null;
        
        try
        {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            decryptedString = new String(decryptedBytes);
        }
        
        catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) 
        {
            System.err.println(e.getMessage());
	}
        
        return decryptedString;
    } 

 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        try 
        {
            processRequest(request, response);
        }
        
        catch (SQLException ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (DocumentException ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        try 
        {
            processRequest(request, response);
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (DocumentException ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getServletInfo() 
    {
        return "Short description";
    }
}

