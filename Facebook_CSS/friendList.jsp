<%@ page import="java.lang.*"%> 	<%-- Java core classes --%>
<%@ page import="ut.JAR.CPEN410.*"%> 	<%-- Custom DB package --%>
<%@ page import="java.sql.*"%>  	<%-- SQL classes --%>

<html>
  <head>
    <title>Friends</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

  <div class="container">
    <div class="row center-text">
      <div class="col-12 col-s-12">

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
        <h1 class="error-title">Not logged in!</h1>
<%
  } else {
%>
        <h1>Friend List</h1>

        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "friendList.jsp";
    String userName = session.getAttribute("userName").toString();
    String previousPage = session.getAttribute("currentPage").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();

      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);

        ResultSet searchRes = appDBAuth.getFriend(userName);

        if (searchRes.next()) {
          do {
%>
        <div class="user-row">
          <div class="user-image">
            <img src="<%= request.getContextPath() + "/" + searchRes.getString("profilePicture") %>" class="profile-pic" />
          </div>

          <div class="user-info">
            <p><b>Name:</b> <%= searchRes.getString("Name") %></p>
            <p><b>Date of Birth:</b> <%= searchRes.getString("DateOfBirth") %></p>
            <p><b>Location:</b> <%= searchRes.getString("Town") %>, <%= searchRes.getString("State") %>, <%= searchRes.getString("Country") %></p>
            <p><b>Gender:</b> <%= searchRes.getString("Gender") %></p>
          </div>
        </div>
<%
          } while (searchRes.next());
        } else {
%>
        <p>No friends</p>
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
      }

      res.close();
      appDBAuth.close();

    } catch (Exception e) {
%>
        <h1 class="error-title">Nothing to show!</h1>
<%
      e.printStackTrace();
      response.sendRedirect("loginHashing.html");
    } finally {
      System.out.println("Finally");
    }
  }
%>

      </div>
    </div>
  </div>

  </body>
</html>
