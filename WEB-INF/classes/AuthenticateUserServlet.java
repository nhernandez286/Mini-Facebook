/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa  
 * CPEN 410 - Mobile, Web, and Internet Programming  
 *
 * This servlet receives a username and password via POST or GET request.
 * It authenticates the user and returns a simple JSON response indicating
 * whether the credentials are valid.
 */

import java.io.*; 
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import org.json.JSONObject;
import ut.JAR.CPEN410.*;

public class AuthenticateUserServlet extends HttpServlet {

    /**
     * Initializes the servlet. Currently unused.
     *
     * @throws ServletException if servlet initialization fails
     */
    public void init() throws ServletException {
        // Initialization logic, if needed
    }

    /**
     * Handles GET requests by forwarding to the POST handler.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * Authenticates a user using their username and password.
     * Responds with a JSON object containing status: "ok" or "not".
     *
     * @param request  the HTTP request containing "user" and "pass" parameters
     * @param response the HTTP JSON response with status info
     * @throws ServletException if servlet processing fails
     * @throws IOException      if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\nAuthenticateUserServlet started!"); 

        // Set content type and encoding for JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter(); // prepare output stream

        // Retrieve login credentials from request
        String userName = request.getParameter("user");
        String passwd = request.getParameter("pass");

        // Create a JSON object for the response
        JSONObject jsonResult = new JSONObject();

        try {
            // Connect to the authentication DB handler
            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

            // Perform authentication
            ResultSet res = appDB.authenticate(userName, passwd);
            System.out.println("\nAuthentication completed.");

            if (res.next()) {
                // Credentials matched: user is authenticated
                jsonResult.put("status", "ok");
                System.out.println("\nUser authenticated successfully.");
            } else {
                // Credentials invalid
                jsonResult.put("status", "not");
                System.out.println("\nAuthentication failed.");
            }

        } catch (Exception e) {
            
            System.out.println("\nException occurred: " + e.getMessage());
        } finally {
            // Output the JSON response
            System.out.println("\nFinished AuthenticateUserServlet.");
            out.println(jsonResult.toString());
        }
    }

    /**
     * Called when the servlet is being destroyed. 
     */
    public void destroy() {
        // Cleanup logic if needed
    }
}
