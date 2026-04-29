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
  String userNameToModify = request.getParameter("userNameToModify");

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
      <div class="col-12 col-s-12">

        <h1>All users</h1>

        <form action="signout.jsp" method="post" style="margin-bottom: 20px;">
          <button type="submit" class="button">Log out</button>
        </form>

<%
    String userName = session.getAttribute("userName").toString();
    String currentPage = "ModifyUser.jsp";
    String previousPage = session.getAttribute("currentPage").toString();

    try {
      applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
      ResultSet res = appDBAuth.verifyUser(userName, currentPage, previousPage);

      if (res.next()) {
        String roleId = res.getString("roleId");
        session.setAttribute("roleId", roleId);
        session.setAttribute("currentPage", currentPage);
        session.setAttribute("userName", userName);
%>

        <form action="ModifyUserAction.jsp" method="post">
          <input type="hidden" name="originalUserName" value="<%= userNameToModify %>" />
          <h2>Information to change</h2>

          <div class="row">
            <div class="col-12 col-s-12">
              <label>Password</label>
              <input type="password" id="userPass" name="userPass" class="input-field" />
            </div>

            <div class="col-12 col-s-12">
              <label>Name</label>
              <input type="text" id="completeName" name="completeName" class="input-field" />
            </div>

            <div class="col-12 col-s-12">
              <label>Date of birth</label>
              <input type="date" id="DateofBirth" name="DateofBirth" class="input-field" />
            </div>

            <div class="col-12 col-s-12">
              <label>Gender</label>
              <select id="Gender" name="Gender" class="input-field">
                <option value="">--Select--</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div class="col-12 col-s-12"><label>Street</label><input type="text" id="Street" name="Street" class="input-field" /></div>
            <div class="col-12 col-s-12"><label>Town</label><input type="text" id="Town" name="Town" class="input-field" /></div>
            <div class="col-12 col-s-12"><label>State</label><input type="text" id="State" name="State" class="input-field" /></div>
            <div class="col-12 col-s-12"><label>Country</label><input type="text" id="Country" name="Country" class="input-field" /></div>
            <div class="col-12 col-s-12"><label>Degree</label><input type="text" id="Degree" name="Degree" class="input-field" /></div>
            <div class="col-12 col-s-12"><label>School</label><input type="text" id="School" name="School" class="input-field" /></div>
          </div>

          <div class="row center-text" style="margin-top: 16px;">
            <input type="submit" class="button" value="Update" />
            <input type="reset" class="button" value="Reset" />
          </div>
        </form>

        <form action="upload.jsp" method="post" style="margin-top: 16px;">
          <input type="hidden" name="originalUserName" value="<%= userNameToModify %>" />
          <input type="hidden" name="returnPage" value="ModifyUserAdmin.jsp" />
          <input type="submit" class="button" value="Change Profile Picture" />
        </form>

        <form action="removeUserPic.jsp" method="post" style="margin-top: 8px;">
          <input type="hidden" name="originalUserName" value="<%= userNameToModify %>" />
          <input type="submit" class="button" value="Delete Profile Picture" />
        </form>

        <form action="ModifyUserAdmin.jsp" method="post" style="margin-top: 8px;">
          <input type="submit" class="button" value="Back to previous page" />
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
        <p class="error-title">Nothing to show!</p>
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
