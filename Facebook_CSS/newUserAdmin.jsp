<%@ page import="java.lang.*"%> 	
<%@ page import="ut.JAR.CPEN410.*"%> 	
<%@ page import="java.sql.*"%>  	

<html>
  <head>
    <title>New user admin</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
    <div class="container">
      <h1 class="error-title">Not logged in!</h1>
    </div>
<%
  } else {
%>

  <div class="container">
    <div class="row center-text">
      <div class="col-s-12 col-12">
        <h1>All users</h1>
      </div>
    </div>

    <div class="row" style="max-width: 800px; margin: 0 auto;">
      <div class="col-s-12 col-12">

        <!-- Logout -->
        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "newUserAdmin.jsp";
    String userName = session.getAttribute("userName").toString();
    String previousPage = session.getAttribute("currentPage").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      System.out.println("Conecting...");
      System.out.println(appDBAuth.toString());

      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        String roleId = res.getString("roleId");
        session.setAttribute("roleId", roleId);
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);
%>

        <form action="addUserAdmin.jsp" method="post">
          <h2>Personal Info</h2>

          <div class="row"><div class="col-12">
            <label>Username:</label>
            <input type="text" id="newUserName" name="newUserName" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Password:</label>
            <input type="password" id="userPass" name="userPass" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Name:</label>
            <input type="text" id="completeName" name="completeName" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Date of birth (yyyy/mm/dd):</label>
            <input type="date" id="DateofBirth" name="DateofBirth" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Gender:</label>
            <select id="Gender" name="Gender" class="input-field">
              <option value="">--Select--</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
          </div></div>

          <div class="row"><div class="col-12">
            <label>Street:</label>
            <input type="text" id="Street" name="Street" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Town:</label>
            <input type="text" id="Town" name="Town" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>State:</label>
            <input type="text" id="State" name="State" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Country:</label>
            <input type="text" id="Country" name="Country" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>Degree:</label>
            <input type="text" id="Degree" name="Degree" class="input-field" />
          </div></div>

          <div class="row"><div class="col-12">
            <label>School:</label>
            <input type="text" id="School" name="School" class="input-field" />
          </div></div>

          <input type="hidden" name="userName" value="<%= session.getAttribute("userName") %>">

          <!-- Buttons -->
          <div class="row center-text" style="margin-top: 16px;">
            <div class="col-6 col-s-6">
              <input type="submit" class="button" value="Sign Up" />
            </div>
            <div class="col-6 col-s-6">
              <input type="reset" class="button" value="Reset" />
            </div>
          </div>
        </form>

<%
      } else {
        session.setAttribute("userName", null);
        response.sendRedirect("loginHashing.html");
        res.close();
        appDBAuth.close();
      }
    } catch (Exception e) {
%>
        <p>Nothing to show!</p>
<%
      e.printStackTrace();
      response.sendRedirect("loginHashing.html");
    } finally {
      System.out.println("Finally");
    }
%>

        <!-- Back to menu -->
        <form action="welcomeMenu.jsp" method="post" style="margin-top: 20px;">
          <input type="submit" class="button" value="Back to menu" />
        </form>

      </div>
    </div>
  </div>

<%
  }
%>

  </body>
</html>
