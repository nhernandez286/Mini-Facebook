<%@ page import="java.lang.*"%> 	
<%@ page import="ut.JAR.CPEN410.*"%> 	
<%@ page import="java.sql.*"%>  	

<html>
  <head>
    <title>Remove user</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  String userNameToDelete = request.getParameter("userNameToDelete");
  String userName = (String) session.getAttribute("userName");

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
%>


    <div class="row" style="max-width: 900px; margin: 0 auto;">
      <div class="col-s-12 col-12">

        <!-- Logout button -->
        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "removeUser.jsp";
    String previousPage = session.getAttribute("currentPage").toString();

    if (previousPage == null) {
      previousPage = request.getParameter("currentPage");
      if (previousPage != null) {
        session.setAttribute("currentPage", previousPage);
      }
    }

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();

      System.out.println("Connecting...");
      System.out.println(appDBAuth.toString());

      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);

        boolean remUser = appDBAuth.removeUser(userNameToDelete);

        if (!remUser) {
%>
        <h1 class="success-title">User was deleted successfully!</h1>
        <form action="welcomeMenu.jsp" method="post">
          <input type="submit" class="button" value="Back to menu" />
        </form>
<%
        } else {
%>
        <h1 class="error-title">User was not deleted successfully</h1>
        <form action="welcomeMenu.jsp" method="post">
          <input type="submit" class="button" value="Back to menu" />
        </form>
<%
        }
      } else {
        session.setAttribute("userName", null);
        response.sendRedirect("loginHashing.html");
      }

      appDBAuth.close();
    } catch (Exception e) {
%>
      <h1 class="error-title">Nothing to show!</h1>
<%
      e.printStackTrace();
    } finally {
      System.out.println("Finally");
    }
%>
      </div>
    </div>

<%
  }
%>

  </body>
</html>
