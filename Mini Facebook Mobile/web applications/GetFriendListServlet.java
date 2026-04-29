/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa  
 * CPEN 410 - Mobile, Web, and Internet Programming  
 *
 * This servlet authenticates a user and retrieves their friend list from the database.
 * If authentication is successful, it returns a JSON array of friend profile objects.
 */

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.*;
import ut.JAR.CPEN410.*;
import java.sql.*;

public class GetFriendListServlet extends HttpServlet {

    /**
     * Initializes the servlet. Currently unused.
     *
     * @throws ServletException if servlet initialization fails
     */
    public void init() throws ServletException {
        // Initialization logic, if needed
    }

    /**
     * Handles GET requests by forwarding them to doPost().
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * Authenticates the user and returns their friend list in JSON format.
     *
     * @param request  the HTTP request with parameters: "user", "pass"
     * @param response the HTTP JSON response containing friend data
     * @throws ServletException if servlet processing fails
     * @throws IOException      if an I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\nGetFriendListServlet started!");

        // Configure the response to return JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Extract parameters from request
        String userName = request.getParameter("user");
        String password = request.getParameter("pass");

        // Prepare the root JSON response object
        JSONObject jsonResult = new JSONObject();

        try {
            // Connect to the database handler
            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

            // Authenticate the user credentials
            ResultSet auth = appDB.authenticate(userName, password);
            System.out.println("\nFriendList Authentication complete.");

            if (auth.next()) {
                // Retrieve the friend list for the authenticated user
                ResultSet res = appDB.getFriend(userName);
                System.out.println("\nFriend list retrieval complete.");

                // Create a JSON array to hold all friend entries
                JSONArray array = new JSONArray();

                // Loop through each friend result
                while (res.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", res.getString("Name"));
                    obj.put("dateOfBirth", res.getString("DateOfBirth"));
                    obj.put("gender", res.getString("Gender"));
                    obj.put("street", res.getString("Street"));
                    obj.put("town", res.getString("Town"));
                    obj.put("state", res.getString("State"));
                    obj.put("country", res.getString("Country"));
                    obj.put("degree", res.getString("Degree"));
                    obj.put("school", res.getString("School"));
                    obj.put("profilePicture", res.getString("profilePicture")); 

                    array.put(obj); // add friend JSON to array
                }

                // Attach the full array to the response
                jsonResult.put("friends", array);
            } else {
                // If authentication failed
                jsonResult.put("status", "not");
                System.out.println("\nAuthentication failed.");
            }

        } catch (Exception e) {
            
            System.out.println("\nException occurred: " + e.getMessage());
        } finally {
            // Send the JSON result to the client
            System.out.println("\nFinished GetFriendListServlet.");
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
