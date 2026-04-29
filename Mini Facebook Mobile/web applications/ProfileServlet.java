/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa  
 * CPEN 410 - Mobile, Web, and Internet Programming  
 *
 * This servlet receives a username and password via POST or GET request,
 * verifies the credentials, and responds with the full user profile in JSON format.
 */

import java.io.*; 
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import org.json.JSONObject;
import ut.JAR.CPEN410.*;

public class ProfileServlet extends HttpServlet {

    /**
     * Initializes the servlet. Currently unused but can be used for setup.
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
     * Handles the POST request to authenticate a user and return their profile data in JSON.
     *
     * @param request  the HTTP request containing "user" and "pass" parameters
     * @param response the HTTP response to return the profile JSON
     * @throws ServletException if servlet fails
     * @throws IOException      if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\nProfile Servlet started!"); // debug message

        // Set response type and encoding
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter(); // writer for response body

        // Get credentials from request parameters
        String userName = request.getParameter("user");
        String passwd = request.getParameter("pass");

        // Create a JSON object to store the result
        JSONObject jsonResult = new JSONObject();

        try {
            // Instantiate the database helper
            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

            // Authenticate the user with given credentials
            ResultSet res = appDB.authenticate(userName, passwd);

            System.out.println("\nProfile authentication done!");

            // If credentials are valid
            if (res.next()) {
                // Fetch the user’s full profile data
                ResultSet profile = appDB.getUserProfile(userName);
                System.out.println("\nProfile method done!");

                // If profile exists
                if (profile.next()) {
                    jsonResult.put("status", "ok");
                    jsonResult.put("userName", profile.getString("UserName"));
                    jsonResult.put("name", profile.getString("Name"));
                    jsonResult.put("dateOfBirth", profile.getString("DateOfBirth"));
                    jsonResult.put("gender", profile.getString("Gender"));
                    jsonResult.put("street", profile.getString("Street"));
                    jsonResult.put("town", profile.getString("Town"));
                    jsonResult.put("state", profile.getString("State"));
                    jsonResult.put("country", profile.getString("Country"));
                    jsonResult.put("degree", profile.getString("Degree"));
                    jsonResult.put("school", profile.getString("School"));
                    jsonResult.put("profilePicture", profile.getString("profilePicture")); 
                } else {
                    // If user is authenticated but no profile found
                    jsonResult.put("status", "not");
                }
            } else {
                // Invalid username or password
                jsonResult.put("status", "not");
            }

        } catch (Exception e) {
           
            e.printStackTrace();
        } finally {
            System.out.println("\nFinally finished Profile Servlet!"); 
            out.println(jsonResult.toString()); // write JSON to response
        }
    }

    /**
     * Called when the servlet is being destroyed. 
     */
    public void destroy() {
        // Cleanup logic if needed
    }
}
