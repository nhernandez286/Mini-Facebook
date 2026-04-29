/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa  
 * CPEN 410 - Mobile, Web, and Internet Programming  
 *
 * This servlet receives a username, password, and target friend username.
 * If the user is authenticated, the target user is added as a friend.
 * Returns a JSON response indicating success or failure.
 */

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.*;
import ut.JAR.CPEN410.*;
import java.sql.*;

public class AddFriendServlet extends HttpServlet {

    /**
     * Initializes the servlet. Currently unused.
     *
     * @throws ServletException if servlet initialization fails
     */
    public void init() throws ServletException {
        // Initialization logic, if needed
    }

    /**
     * Forwards GET requests to the POST handler.
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
     * Authenticates a user and adds a friend to their list if valid.
     *
     * @param request  the HTTP request containing parameters "user", "pass", and "friend"
     * @param response the HTTP JSON response containing operation status
     * @throws ServletException if servlet processing fails
     * @throws IOException      if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\nAddFriendServlet started!"); 

        // Set up JSON response formatting
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Extract parameters from request
        String userName = request.getParameter("user");
        String password = request.getParameter("pass");
        String friendName = request.getParameter("friend");

        // JSON object to hold the result
        JSONObject jsonResult = new JSONObject();

        try {
            // Instantiate database connection handler
            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

            // Authenticate user credentials
            ResultSet auth = appDB.authenticate(userName, password);
            System.out.println("\nAddFriend Authentication completed.");

            if (auth.next()) {
                // If valid user, attempt to add the friend
                appDB.addFriend(userName, friendName);
                jsonResult.put("status", "OK");
                System.out.println("\nFriend added successfully.");
            } else {
                // Authentication failed
                jsonResult.put("status", "INVALID_CREDENTIALS");
                System.out.println("\nAuthentication failed.");
            }

        } catch (Exception e) {
           
            System.out.println("\nException occurred: " + e.getMessage());
        } finally {
            // Output final JSON response to client
            System.out.println("\nFinished AddFriendServlet.");
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
