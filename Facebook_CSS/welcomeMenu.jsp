<%@ page import="java.lang.*"%> 	
<%@ page import="ut.JAR.CPEN410.*"%> 	
<%@ page import="java.sql.*"%>  	


<html>
  <head>
    <title>Welcome</title> <!-- set browser tab title -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  // Check authentication
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
    <div class="container center-text">
      <h1 class="error-title">Not logged in!</h1>
    </div>
<%
  } else {
    // Retrieve variables
    String currentPage = "welcomeMenu.jsp"; 
    String previousPage = session.getAttribute("currentPage").toString();
    String userName = session.getAttribute("userName").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        String userActualName = res.getString(3);
        String userRole = res.getString(2);

        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);
%>

  <!-- Page Layout -->
  <div class="container">

    <!-- Welcome title -->
    <div class="row">
      <div class="col-s-12 col-12 center-text">
        <h1>Welcome to Mini Facebook, <%= userActualName %>!</h1>
      </div>
    </div>

    <!-- Menu label -->
    <div class="row">
      <div class="col-s-12 col-12">
        <div class="section-border">Menu Options</div>
      </div>
    </div>

    <!-- Dynamic menu items -->
    <div class="row menu-layout">
      <%
        ResultSet menuRes = appDBAuth.menuElements(userName, userRole);
        String currentMenu = "";
        while (menuRes.next()) {
          if (!currentMenu.equals(menuRes.getString(2))) {
            currentMenu = menuRes.getString(2);
      %>
        <div class="col-s-12 col-12 menu-category"><%= currentMenu %></div>
      <%
          }
      %>
        <div class="col-s-12 col-6 col-4 options-box">
          <a href="<%= menuRes.getString(1) %>" class="button"><%= menuRes.getString(3) %></a>
        </div>
      <%
        }
      %>
    </div>

    <!-- Logout -->
    <div class="row">
      <div class="col-s-12 col-12 center-text logout-container">
        <form action="signout.jsp" method="post">
          <button type="submit" class="button">Log out</button>
        </form>
      </div>
    </div>

  </div>

<%
      } else {
        session.setAttribute("userName", null);
        response.sendRedirect("loginHashing.html");
      }

      res.close();
      appDBAuth.close();

    } catch (Exception e) {
%>
    <div class="container center-text">
      <h1 class="error-title">Nothing to show!</h1>
    </div>
<%
      e.printStackTrace();
      response.sendRedirect("loginHashing.html");
    } finally {
      System.out.println("Finally");
    }
  }
%>

  </body>
</html>
