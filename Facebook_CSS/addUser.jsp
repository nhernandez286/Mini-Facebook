<%@ page import="java.lang.*"%> 	<%-- Java core classes --%>
<%@ page import="ut.JAR.CPEN410.*"%> 	<%-- Custom database/auth package --%>
<%@ page import="java.sql.*"%>  	<%-- SQL classes --%>

<html>
  <head>
    <title>Sign Up</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="UsingCSS.css">
  </head>
  <body>

  <div class="container">
    <div class="row center-text">
      <div class="col-12 col-s-12">

<%
  // Retrieve parameters
  String userName = request.getParameter("userName");
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
  String profilePicturePath = "Facebook/Pictures/default_pfp.jpg";  // default profile pic

  try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      System.out.println("Connecting...");
      System.out.println(appDBAuth.toString());

      boolean res = appDBAuth.addUser(
        userName, completeName, userPass, DateofBirth,
        Gender, Street, Town, State, Country, Degree, School,
        profilePicturePath
      );

      if (!res) { // success
        session.setAttribute("userName", userName);
        response.sendRedirect("addRoleUser.jsp");
      } else { // failure
        appDBAuth.close();
%>
        <h1 class="error-title">Account could not be created. Please try again</h1>
        <form action="newUser.html" method="post">
          <input type="submit" class="button" value="Try again" />
        </form>
<%
      }

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
  </div>

  </body>
</html>
