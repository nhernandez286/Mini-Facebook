<%@ page import="java.lang.*"%> 	<% //Import Java's core classes %>
<%@ page import="ut.JAR.CPEN410.*"%> 	<% //Import custom package ut.JAR.CPEN410 %>
<%@ page import="java.sql.*"%>  	<% //Import java.sql package for database access %>

<html>
  <head>
    <title>Modify user pic</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>

    <div class="row" style="max-width: 900px; margin: 0 auto;">
      <div class="col-s-12 col-12 center-text">
        <h1 class="error-title">Not logged in!</h1>
      </div>
    </div>

<%
  } else {
    String userNameToModify = request.getParameter("originalUserName");
    String profilePicturePath = "Facebook/Pictures/default_pfp.jpg";
%>


    <div class="row" style="max-width: 900px; margin: 0 auto;">
      <div class="col-s-12 col-12">
        <h1>All users</h1>

        <!-- Logout Button -->
        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
      String userName = session.getAttribute("userName").toString();
      String currentPage = "removeUserPic.jsp";
      String previousPage = session.getAttribute("currentPage").toString();

      try {
        applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
        System.out.println("Conecting...");
        System.out.println(appDBAuth.toString());

        ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

        if (res.next()) {
          String roleId = res.getString("roleId");
          session.setAttribute("roleId", roleId);
          session.setAttribute("currentPage", "ModifyUser.jsp");
          session.setAttribute("userName", userName);

          boolean resAdd = appDBAuth.addProfilePicture(userNameToModify, profilePicturePath);

          if (!resAdd) {
%>
        <!-- Success message -->
        <h1 class="success-title">Photo deleted successfully</h1>

        <!-- Back to modify users button -->
        <form action="ModifyUserAdmin.jsp" method="post">
          <input type="submit" class="button" value="Back to modify users" />
        </form>
<%
          }
        } else {
          session.setAttribute("userName", null);
          response.sendRedirect("loginHashing.html");
          res.close();
          appDBAuth.close();
        }

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


  </body>
</html>
