<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
  <head>
    <title>New user admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="UsingCSS.css">
  </head>
  <body>

<%
  String userName = (String) session.getAttribute("userName");
  String newUserName = (String) session.getAttribute("newUserName");

  if ((userName == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
  <div class="container">
    <div class="row center-text">
      <div class="col-12 col-s-12">
        <h1 class="error-title">Not logged in!</h1>
      </div>
    </div>
  </div>

<%
  } else {
%>
  <div class="container">
    <div class="row center-text">
      <div class="col-12 col-s-12">

        <!-- Logout -->
        <form action="signout.jsp" method="post">
          <button type="submit" class="button">Log out</button>
        </form>

<%
      try {
        applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
        System.out.println("Connecting...");
        System.out.println(appDBAuth.toString());

        String currentPage = "addRoleUserAdmin.jsp";
        String previousPage = session.getAttribute("currentPage").toString();
        ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

        if (res.next()) {
          session.setAttribute("currentPage", currentPage);
          session.setAttribute("userName", userName);

          boolean resAdd = appDBAuth.addRoleUser(newUserName); // false = success

          if (!resAdd) {
%>
        <h1 class="success-title">Account has been created successfully!</h1>
        <form action="welcomeMenu.jsp" method="post">
          <input type="submit" class="button" value="Go to Menu"/>
        </form>
<%
          } else {
%>
        <h1 class="error-title">Account could not be created. Please try again</h1>
        <form action="newUserAdmin.html" method="post">
          <input type="submit" class="button" value="Try again"/>
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
%>

      </div>
    </div>
  </div>

<%
  }
%>

  </body>
</html>
