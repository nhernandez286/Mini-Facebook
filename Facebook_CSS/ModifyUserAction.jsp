<%@ page import="java.lang.*"%>
<%@ page import="ut.JAR.CPEN410.*"%>
<%@ page import="java.sql.*"%>

<html>
  <head>
    <title>Modify user info</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
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
    <div class="row center-text">
      <div class="col-12 col-s-12">

        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "ModifyUserAction.jsp";
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
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);

        String originalUserName = request.getParameter("originalUserName");
        String userPass = request.getParameter("userPass");
        String completeName = request.getParameter("completeName");
        String DateofBirth = request.getParameter("DateofBirth");
        String Gender = request.getParameter("Gender");
        String Street = request.getParameter("Street");
        String Town = request.getParameter("Town");
        String State = request.getParameter("State");
        String Country = request.getParameter("Country");
        String Degree = request.getParameter("Degree");
        String School = request.getParameter("School");

        boolean updUser = appDBAuth.UpdateUser(
          originalUserName, userPass, completeName, DateofBirth,
          Gender, Street, Town, State, Country, Degree, School
        );

        if (!updUser) {
%>
        <h1 class="success-title">User was modified successfully!</h1>
<%
        } else {
%>
        <h1 class="error-title">User was not modified successfully</h1>
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
