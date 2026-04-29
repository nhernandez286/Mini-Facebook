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
    String userName = session.getAttribute("userName").toString();
%>


  <div class="row" style="max-width: 900px; margin: 0 auto;">
    <div class="col-s-12 col-12">

      <h1>All Users</h1>

      <!-- Logout button -->
      <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
        <button type="submit" class="button">Log out</button>
      </form>

<%
    String currentPage = "removeUserAdmin.jsp";
    String previousPage = session.getAttribute("currentPage").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();

      System.out.println("Conecting...");
      System.out.println(appDBAuth.toString());

      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);

        ResultSet searchRes = appDBAuth.searchAllUser(userName);

        if (searchRes.next()) {
%>
      <!-- Responsive table wrapper -->
      <div class="table-responsive">
        <table border="1" cellpadding="5" cellspacing="0" style="width: 100%; border-collapse: collapse;">
          <thead style="background-color: #eaf1fb;">
            <tr>
              <th>Username</th>
              <th>Full Name</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
<%
          do {
%>
            <tr>
              <td><%= searchRes.getString("userName") %></td>
              <td><%= searchRes.getString("name") %></td>
              <td>
                <form action="removeUser.jsp" method="post">
                  <input type="hidden" name="userNameToDelete" value="<%= searchRes.getString("UserName") %>" />
                  <input type="submit" class="button" value="Delete User" />
                </form>
              </td>
            </tr>
<%
          } while (searchRes.next());
%>
          </tbody>
        </table>
      </div>
<%
        } else {
%>
      <p>No user in website</p>
<%
        }
      } else {
        session.setAttribute("userName", null);
        res.close();
        appDBAuth.close();
        response.sendRedirect("loginHashing.html");
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

      <br>
      <form action="welcomeMenu.jsp" method="post">
        <input type="submit" class="button" value="Back to menu" />
      </form>

    </div>
  </div>


<%
  }
%>

  </body>
</html>
