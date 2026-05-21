package controllers;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginServlet extends HttpServlet 
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String enteredCaptcha = request.getParameter("captcha"); 

        try 
        {
            if (username == null && password == null || username.isEmpty() && password.isEmpty()) 
            {
                throw new NullPointerException();
            }

            String role = authenticateUser(username, password);
            HttpSession session = request.getSession();
            String storedCaptcha = (String) session.getAttribute("captcha");

            if (role != null) 
            {
                
                if (!enteredCaptcha.equals(storedCaptcha)) 
                {
                    if (enteredCaptcha.isEmpty())
                    {
                        request.getRequestDispatcher("error_nullcaptcha.jsp").forward(request, response);
                    }
                    
                    request.getRequestDispatcher("error_captcha.jsp").forward(request, response);
                }

                session.setAttribute("username", username);
                session.setAttribute("role", role);
                session.setMaxInactiveInterval(300);

                String fullName = getFullName(username);
                session.setAttribute("fullName", fullName);
                response.sendRedirect("Success.jsp");
   
            }
            
            else 
            {
                if (userExists(username)) 
                {
                    if (password == null || password.isEmpty()) 
                    {
                        request.getRequestDispatcher("error_1.jsp").forward(request, response);
                    } 
                    
                    else 
                    {
                        request.getRequestDispatcher("error_2.jsp").forward(request, response);
                    }
                } 
                
                else 
                {
                    if (password == null || password.isEmpty()) 
                    {
                        request.getRequestDispatcher("error_1.jsp").forward(request, response);
                    }
                    
                    request.getRequestDispatcher("error_3.jsp").forward(request, response);
                }
            }
        } 
        
        catch (NullPointerException e) 
        {
            request.getRequestDispatcher("noLoginCredentials.jsp").forward(request, response);
        } 
        
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        private String getFullName(String username) 
        {
            String[] parts = username.split("@");
            return parts[0];
        }
    
    
    private String authenticateUser(String username, String password) throws ClassNotFoundException, Exception 
    {
   
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String query = "SELECT Role, Password FROM USER_INFO WHERE Username = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) 
            {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) 
                {
                    String storedEncryptedPassword = resultSet.getString("Password");
                    String decryptedPassword = decrypt(storedEncryptedPassword);
                    
                    if (password.equals(decryptedPassword) || password == decryptedPassword) 
                    {
                        return resultSet.getString("Role");
                    } 
                }

            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private boolean userExists(String username) throws ClassNotFoundException 
    {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String query = "SELECT COUNT(*) AS count FROM USER_INFO WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) 
            {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) 
                {
                    if (resultSet.next()) 
                    {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                }
            }
        } 
            
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return false;
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
    
    	/* public static String encrypt(String strToEncrypt) 
        {
		String encryptedString = null;
		
                try 
                {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedString = org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
		} 
                
                catch (Exception e) 
                {
			System.err.println(e.getMessage());
		}
                
		return encryptedString;
	}*/
}

