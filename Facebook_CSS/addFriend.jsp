<%@ page import="java.lang.*"%> 	
<%@ page import="ut.JAR.CPEN410.*"%> 	
<%@ page import="java.sql.*"%>  

<html>
  <head>
    <title>Search Friend</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="UsingCSS.css">
  </head>
  <body>

<%
  String friendUsername = request.getParameter("friendUsername");
  String userName = (String) session.getAttribute("userName");

  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
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
    <div class="row">
      <div class="col-12 col-s-12 center-text">

        <!-- Logout Button -->
        <form action="signout.jsp" method="post">
          <button type="submit" class="button">Log out</button>
        </form>

<%
      String currentPage = "addFriend.jsp";
      String previousPage = session.getAttribute("currentPage").toString();

      if (previousPage == null) {
        previousPage = request.getParameter("currentPage");
        if (previousPage != null) {
          session.setAttribute("currentPage", previousPage);
        }
      }

      try {
        applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
        ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

        if (res.next()) {
          session.setAttribute("currentPage", "searchFriend.jsp");

          if (session.getAttribute("userName") == null) {
            session.setAttribute("userName", userName);
          } else {
            session.setAttribute("userName", userName);
          }

          boolean resFriend = appDBAuth.addFriend(userName, friendUsername);

          if (!resFriend) {
%>
        <h1 class="success-title">Friend was added successfully!</h1>
<%
          } else {
%>
        <h1 class="error-title">Friend was not added successfully</h1>
<%
          }
%>

        <form action="welcomeMenu.jsp" method="post">
          <input type="submit" class="button" value="Back to menu" />
        </form>

<%
        } else {
          session.setAttribute("userName", null);
          response.sendRedirect("loginHashing.html");

          if (res != null) {
            res.close();
          }
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
    }
%>

      </div> <!-- col -->
    </div> <!-- row -->
  </div> <!-- container -->

  </body>
</html>
