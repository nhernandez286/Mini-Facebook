<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
  <head>
    <title>Mini Facebook</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  // Retrieve variables
  String userName = request.getParameter("userName");
  String userPass = request.getParameter("userPass");

  try {
    // Create DB connection
    applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
    System.out.println("Connecting...");
    System.out.println(appDBAuth.toString());

    // Try to authenticate the user
    ResultSet res = appDBAuth.authenticate(userName, userPass);

    if (res.next()) {
      // If authentication is successful, set session
      session.setAttribute("currentPage", "validationHashing.jsp");

      if (session.getAttribute("userName") == null) {
        session.setAttribute("userName", userName);
      } else {
        session.setAttribute("userName", userName);
      }

      // Redirect to main menu
      response.sendRedirect("welcomeMenu.jsp");

    } else {
      // If login failed, reset session
      session.setAttribute("userName", null);
%>
      <!-- Responsive error UI -->

        <div class="row" style="max-width: 900px; margin: 0 auto;">
          <div class="col-s-12 col-12 center-text">
            <h1 class="error-title">Username or Password are incorrect. Please try again.</h1>
            <form action="loginHashing.html" method="post" style="margin-top: 20px;">
              <input type="submit" class="button" value="Back to Login" />
            </form>
          </div>
        </div>

<%
    }

    res.close();
    appDBAuth.close();

  } catch (Exception e) {
%>

      <div class="row">
        <div class="col-s-12 col-12 center-text">
          <h1 class="error-title">Nothing to show!</h1>
        </div>
      </div>

<%
    e.printStackTrace();
    response.sendRedirect("loginHashing.html");
  } finally {
    System.out.println("Finally");
  }
%>

  </body>
</html>
