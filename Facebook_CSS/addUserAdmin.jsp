<%@ page import="java.lang.*"%> 	<% //Import Java's core classes %>
<%@ page import="ut.JAR.CPEN410.*"%> 	<% //Import custom package ut.JAR.CPEN410 %>
<%@ page import="java.sql.*"%>  	<% //Import java.sql package for database access %>

<html>
  <head>
    <!-- sets browser tab title -->
    <title>New user admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="UsingCSS.css">
  </head>
  <body>

   
      <div class="row" style="max-width: 900px; margin: 0 auto;">
        <div class="col-12 col-s-12 center-text">

<%
    // Retrieve variables
    String newUserName = request.getParameter("newUserName");
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
    String profilePicturePath = "Facebook/Pictures/default_pfp.jpg"; // default
    String userName = request.getParameter("userName");

    if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
        session.setAttribute("currentPage", null);
        session.setAttribute("userName", null);
%>
          <h1 class="error-title">Not logged in!</h1>
<%
    } else {
        try {
            applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
            System.out.println("Connecting...");
            System.out.println(appDBAuth.toString());

            String currentPage = "addUserAdmin.jsp";
            String previousPage = session.getAttribute("currentPage").toString();

            ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

            if (res.next()) {
                session.setAttribute("currentPage", currentPage);
                session.setAttribute("userName", userName);
                session.setAttribute("newUserName", newUserName);

                boolean resAdd = appDBAuth.addUser(
                    newUserName, completeName, userPass, DateofBirth,
                    Gender, Street, Town, State, Country, Degree, School,
                    profilePicturePath
                );

                if (!resAdd) {
                    response.sendRedirect("addRoleUserAdmin.jsp");
                } else {
%>
          <h1 class="error-title">Account could not be created. Please try again</h1>
          <form action="welcomeMenu.jsp" method="post">
            <input type="submit" class="button" value="Try again"/>
          </form>
<%
                }
            } else {
                session.setAttribute("currentPage", currentPage);
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
        } finally {
            System.out.println("Finally ");
        }
    }
%>

        </div>
      </div>
  

  </body>
</html>
