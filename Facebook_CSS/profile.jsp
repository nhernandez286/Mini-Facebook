<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
<head>
  <title>Profile</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href="UsingCSS.css">
</head>
<body>

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
  <div class="container center-text">
    <h1 class="error-title">Not logged in!</h1>
  </div>
<%
  } else {
%>

  <!-- Logout button -->
  <div class="container">
    <div class="row">
      <div class="col-s-12 col-12 center-text">
        <form action="signout.jsp" method="post">
          <button type="submit" class="button">Log out</button>
        </form>
      </div>
    </div>
  </div>

<%
    String currentPage = "profile.jsp";
    String userName = session.getAttribute("userName").toString();
    String previousPage = session.getAttribute("currentPage").toString();

    try {
      applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();
      System.out.println("Connecting...");
      System.out.println(appDB.toString());

      ResultSet res = appDB.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);

        ResultSet rs = appDB.getUserProfile(userName);

        if (rs.next()) {
          String profilePicture = rs.getString("profilePicture");
%>

  <div class="container">
    <div class="row center-text">
      <div class="col-s-12 col-12">
        <h1><%= rs.getString("Name") %>'s Profile</h1>
      </div>
    </div>

    <div class="row user-row" style="justify-content: center;">
      <div class="col-s-12 col-4 center-text">
        <% if (profilePicture != null) { %>
          <img src="/<%= profilePicture %>" alt="Profile Picture" class="profile-pic" style="max-width: 150px;" />
        <% } else { %>
          <p>No profile picture uploaded.</p>
        <% } %>
      </div>

      <div class="col-s-12 col-8">
        <p><strong>Name:</strong> <%= rs.getString("Name") %></p>
        <p><strong>Date of Birth:</strong> <%= rs.getString("DateOfBirth") %></p>
        <p><strong>Gender:</strong> <%= rs.getString("Gender") %></p>
        <p><strong>Address:</strong> <%= rs.getString("Street") %>, <%= rs.getString("Town") %>, <%= rs.getString("State") %>, <%= rs.getString("Country") %></p>
        <p><strong>Degree:</strong> <%= rs.getString("Degree") %></p>
        <p><strong>School:</strong> <%= rs.getString("School") %></p>
      </div>
    </div>

    <div class="row center-text" style="margin-top: 24px;">
      <div class="col-s-12 col-6">
        <form action="upload.jsp" method="post">
          <input type="hidden" name="originalUserName" value="<%= userName %>" />
          <input type="hidden" name="returnPage" value="profile.jsp" />
          <input type="submit" class="button" value="Upload Profile Picture" />
        </form>
      </div>
      <div class="col-s-12 col-6">
        <form action="welcomeMenu.jsp" method="post">
          <input type="submit" class="button" value="Back to Menu" />
        </form>
      </div>
    </div>
  </div>

<%
        } else {
%>
  <div class="container center-text">
    <h1 class="error-title">Error: User profile not found.</h1>
  </div>
<%
        }
      } else {
        session.setAttribute("userName", null);
        response.sendRedirect("loginHashing.html");
      }

      res.close();
      appDB.close();

    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("loginHashing.html");
    } finally {
      System.out.println("Finally");
    }
  }
%>

</body>
</html>
