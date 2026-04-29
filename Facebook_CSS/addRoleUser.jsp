<%@ page import="java.lang.*"%>       
<%@ page import="ut.JAR.CPEN410.*"%>  
<%@ page import="java.sql.*"%>       

<html>
  <head>
    <title>Sign Up</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="UsingCSS.css">
  </head>
  <body>

  <div class="container">
    <div class="row center-text">
      <div class="col-12 col-s-12">
<%
        // Retrieve user from session
        String userName = (String) session.getAttribute("userName");

        try {
            applicationDBAuthenticationGoodComplete appDBAuth = new applicationDBAuthenticationGoodComplete();
            System.out.println("Connecting...");
            System.out.println(appDBAuth.toString());

            boolean resAdd = appDBAuth.addRoleUser(userName);

            if (resAdd) {
%>
        <h1 class="error-title">Account could not be created. Please try again</h1>
        <form action="newUser.html" method="post" style="margin-top: 16px;">
          <button type="submit" class="button">Try Again</button>
        </form>
<%
            } else {
%>
        <h1 class="success-title">Account has been created successfully!</h1>
        <form action="loginHashing.html" method="post" style="margin-top: 16px;">
          <button type="submit" class="button">Go to Login</button>
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
      </div> <!-- end col -->
    </div> <!-- end row -->
  </div> <!-- end container -->

  </body>
</html>
