<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
  <head>
    <title>Search Friend</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
    <div class="container">
      <div class="row center-text">
        <div class="col-s-12 col-12">
          <h1 class="error-title">Not logged in!</h1>
        </div>
      </div>
    </div>
<%
  } else {
%>
  <div class="container">
    <div class="row">
      <div class="col-s-12 col-12">

        <!-- Logout button -->
        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "searchUser.jsp";
    String previousPage = session.getAttribute("currentPage").toString();
    String town = request.getParameter("town");
    String state = request.getParameter("state");
    String country = request.getParameter("country");
    String gender = request.getParameter("gender");
    String age = request.getParameter("age");
    String userName = session.getAttribute("userName") != null ? session.getAttribute("userName").toString() : null;

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);

        ResultSet searchRes = appDBAuth.searchUserNotFriend(userName, town, state, country, gender, age);

        if (searchRes.next()) {
%>
        <h1>Search Results</h1>
<%
          do {
%>
        <div class="user-row">
          <div class="user-image">
            <img src="<%= request.getContextPath() + "/" + searchRes.getString("profilePicture") %>" 
                 alt="Profile Picture" class="profile-pic">
          </div>
          <div class="user-info">
            <p><b>Name:</b> <%= searchRes.getString("Name") %></p>
            <p><b>Location:</b> <%= searchRes.getString("Town") %>, <%= searchRes.getString("State") %>, <%= searchRes.getString("Country") %></p>
            <p><b>Gender:</b> <%= searchRes.getString("Gender") %></p>
            <p><b>Age:</b> <%= searchRes.getString("Age") %></p>
          </div>
          <div class="user-action">
            <form action="addFriend.jsp" method="post">
              <input type="hidden" name="friendUsername" value="<%= searchRes.getString("UserName") %>">
              <input type="submit" class="button" value="Add Friend">
            </form>
          </div>
        </div>
<%
          } while (searchRes.next());
        } else {
%>
        <p>No possible friend to add found matching the criteria.</p>
<%
        }

        ResultSet searchRes2 = appDBAuth.searchUserFriend(userName, town, state, country, gender, age);

        if (searchRes2.next()) {
%>
        <h1>Friends Matching Criteria</h1>
<%
          do {
%>
        <div class="user-row">
          <div class="user-image">
            <img src="<%= request.getContextPath() + "/" + searchRes2.getString("profilePicture") %>" 
                 alt="Profile Picture" class="profile-pic">
          </div>
          <div class="user-info">
            <p><b>Name:</b> <%= searchRes2.getString("Name") %></p>
            <p><b>Location:</b> <%= searchRes2.getString("Town") %>, <%= searchRes2.getString("State") %>, <%= searchRes2.getString("Country") %></p>
            <p><b>Gender:</b> <%= searchRes2.getString("Gender") %></p>
            <p><b>Age:</b> <%= searchRes2.getString("Age") %></p>
            <p><b>Status:</b> Friend</p>
          </div>
        </div>
<%
          } while (searchRes2.next());
        } else {
%>
        <p>No friend found matching the search criteria.</p>
<%
        }
%>
        <!-- Back to Search -->
        <form action="searchFriend.jsp" method="post" style="margin-top: 20px;">
          <button type="submit" class="button">Back to search</button>
        </form>
<%
      } else {
        session.setAttribute("userName", null);
        response.sendRedirect("loginHashing.html");
      }

      res.close();
      appDBAuth.close();

    } catch (Exception e) {
%>
    <p>Nothing to show!</p>
<%
      e.printStackTrace();
      response.sendRedirect("loginHashing.html");
    }
  }
%>
      </div>
    </div>
  </body>
</html>
