<%@ page import="java.lang.*"%> 	
<%@ page import="ut.JAR.CPEN410.*"%> 	
<%@ page import="java.sql.*"%>  	

<html>
  <head>
    <title>Search Friend</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

<%
  if ((session.getAttribute("userName") == null) || (session.getAttribute("currentPage") == null)) {
    session.setAttribute("currentPage", null);
    session.setAttribute("userName", null);
%>
    <div class="container center-text">
      <h1 class="error-title">Not logged in!</h1>
    </div>
<%
  } else {
%>
  <div class="container">
    <div class="row">
      <div class="col-s-12 col-12">

        <!-- Logout button -->
        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String currentPage = "searchFriend.jsp";
    String previousPage = session.getAttribute("currentPage").toString();
    String userName = session.getAttribute("userName").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);
%>

        <h1 class="center-text">Search for Friends</h1>

        <!-- Search Form -->
        <form action="searchUser.jsp" method="post" style="margin-bottom: 30px;">
          <div class="row">
            <div class="col-s-12 col-6">
              <label for="town">Town:</label>
              <input type="text" id="town" name="town" style="width: 100%; padding: 6px;" />
            </div>
            <div class="col-s-12 col-6">
              <label for="state">State:</label>
              <input type="text" id="state" name="state" style="width: 100%; padding: 6px;" />
            </div>
          </div>

          <div class="row">
            <div class="col-s-12 col-6">
              <label for="country">Country:</label>
              <input type="text" id="country" name="country" style="width: 100%; padding: 6px;" />
            </div>
            <div class="col-s-12 col-3">
              <label for="gender">Gender:</label>
              <select id="gender" name="gender" style="width: 100%; padding: 6px;">
                <option value="">Any</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
            <div class="col-s-12 col-3">
              <label for="age">Age:</label>
              <input type="number" id="age" name="age" min="0" style="width: 100%; padding: 6px;" />
            </div>
          </div>

          <div class="row center-text" style="margin-top: 16px;">
            <div class="col-s-12 col-6">
              <input type="submit" class="button" value="Search" />
            </div>
          </div>
        </form>

        <!-- Back button -->
        <form action="welcomeMenu.jsp" method="post">
          <button type="submit" class="button">Back to menu</button>
        </form>

<%
      } else {
        session.setAttribute("userName", null);
        session.setAttribute("currentPage", null);
        response.sendRedirect("loginHashing.html");
      }

      res.close();
      appDBAuth.close();
    } catch (Exception e) {
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
