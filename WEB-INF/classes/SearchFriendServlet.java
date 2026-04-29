/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa  
 * CPEN 410 - Mobile, Web, and Internet Programming  
 *
 * This servlet receives user credentials and search filters (town, state, country, gender, age),
 * authenticates the user, and returns a JSON array of matching users separated by friend status.
 */

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.*;
import ut.JAR.CPEN410.*;
import java.sql.*;

public class SearchFriendServlet extends HttpServlet {

    /**
     * Initializes the servlet. Currently unused.
     *
     * @throws ServletException if servlet setup fails
     */
    public void init() throws ServletException {
        // Initialization logic, if needed
    }

    /**
     * Redirects GET requests to POST for unified handling.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws ServletException if servlet error occurs
     * @throws IOException      if I/O error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * Authenticates a user and returns a list of users matching the given criteria,
     * labeled by friend/non-friend status.
     *
     * @param request  the HTTP request containing filters and login credentials
     * @param response the HTTP JSON response with matching user data
     * @throws ServletException if servlet fails
     * @throws IOException      if I/O error occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("\nSearchFriendServlet started."); 

        // Prepare JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Extract authentication and search parameters
        String userName = request.getParameter("user");
        String password = request.getParameter("pass");

        String town = request.getParameter("town");
        String state = request.getParameter("state");
        String country = request.getParameter("country");
        String gender = request.getParameter("gender");
        String age = request.getParameter("age");

        JSONObject jsonResult = new JSONObject();

        try {
            // Connect to the DB helper
            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

            // Authenticate user
            ResultSet auth = appDB.authenticate(userName, password);
            System.out.println("\nSearchFriend Authentication completed.");

            if (auth.next()) {
                // If authentication is valid
                JSONArray users = new JSONArray();

                // Get users that ARE already friends
                ResultSet friends = appDB.searchUserFriend(userName, town, state, country, gender, age);
                System.out.println("\nRetrieved matching friends.");

                while (friends.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", friends.getString("name"));
                    obj.put("userName", friends.getString("userName"));
                    obj.put("profilePicture", friends.getString("profilePicture"));
                    obj.put("town", friends.getString("town"));
                    obj.put("state", friends.getString("state"));
                    obj.put("country", friends.getString("country"));
                    obj.put("gender", friends.getString("gender"));
                    obj.put("age", friends.getInt("Age"));
                    obj.put("isFriend", true); // mark as friend
                    users.put(obj);
                }

                // Get users that are NOT friends yet
                ResultSet nonFriends = appDB.searchUserNotFriend(userName, town, state, country, gender, age);
                System.out.println("\nRetrieved matching non-friends.");

                while (nonFriends.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("name", nonFriends.getString("name"));
                    obj.put("userName", nonFriends.getString("userName"));
                    obj.put("profilePicture", nonFriends.getString("profilePicture"));
                    obj.put("town", nonFriends.getString("town"));
                    obj.put("state", nonFriends.getString("state"));
                    obj.put("country", nonFriends.getString("country"));
                    obj.put("gender", nonFriends.getString("gender"));
                    obj.put("age", nonFriends.getInt("Age"));
                    obj.put("isFriend", false); // mark as non-friend
                    users.put(obj);
                }

                // Attach the user list to the response
                jsonResult.put("users", users);

            } else {
                // If authentication fails
                jsonResult.put("status", "not");
                System.out.println("\nAuthentication failed.");
            }

        } catch (Exception e) {
           
            System.out.println("\nException occurred: " + e.getMessage());
        } finally {
            // Send response to client
            System.out.println("\nFinished SearchFriendServlet.");
            out.println(jsonResult.toString());
        }
    }

    /**
     * Called when servlet is destroyed.
     */
    public void destroy() {
        // Cleanup logic if needed
    }
}
