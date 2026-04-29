<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
  <head>
    <title>Modify user</title>
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
    <h1 class="error-title">Not logged in!</h1>
  </div>
<%
  } else {
%>

  <div class="container">
    <div class="row">
      <div class="col-s-12 col-12">

        <h1>All users</h1>

        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
      String currentPage = "ModifyUserAdmin.jsp";
      String userName = session.getAttribute("userName").toString();
      String previousPage = session.getAttribute("currentPage").toString();

      try {
        applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
        ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

        if (res.next()) {
          session.setAttribute("currentPage", currentPage);
          session.setAttribute("userName", userName);

          ResultSet searchRes = appDBAuth.searchAllUser(userName);

          if (searchRes.next()) {
            do {
              String profilePicture = searchRes.getString("profilePicture");
%>
        <div class="user-row">
          <div class="user-image">
            <img src="/<%= profilePicture %>" alt="Profile Picture" class="profile-pic" />
          </div>

          <div class="user-info">
            <p><b>Username:</b> <%= searchRes.getString("userName") %></p>
            <p><b>Full Name:</b> <%= searchRes.getString("Name") %></p>
            <p><b>Date of Birth:</b> <%= searchRes.getString("DateOfBirth") %></p>
            <p><b>Gender:</b> <%= searchRes.getString("Gender") %></p>
            <p><b>Street:</b> <%= searchRes.getString("Street") %></p>
            <p><b>Town:</b> <%= searchRes.getString("Town") %></p>
            <p><b>State:</b> <%= searchRes.getString("State") %></p>
            <p><b>Country:</b> <%= searchRes.getString("Country") %></p>
            <p><b>Degree:</b> <%= searchRes.getString("Degree") %></p>
            <p><b>School:</b> <%= searchRes.getString("School") %></p>
          </div>

          <div class="user-action">
            <form action="ModifyUser.jsp" method="post">
              <input type="hidden" name="userNameToModify" value="<%= searchRes.getString("UserName") %>" />
              <input type="submit" class="button" name="ModifyUserAdminbtn" value="Modify User" />
            </form>
          </div>
        </div>
<%
            } while (searchRes.next());
          } else {
%>
        <p>No user in website</p>
<%
          }
%>

        <form action="welcomeMenu.jsp" method="post" style="margin-top: 20px;">
          <button type="submit" class="button">Back to menu</button>
        </form>

<%
        } else {
          session.setAttribute("userName", null);
          response.sendRedirect("loginHashing.html");
          res.close();
          appDBAuth.close();
        }

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
  </div>

  </body>
</html>
