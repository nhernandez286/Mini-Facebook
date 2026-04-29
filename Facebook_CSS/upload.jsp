<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Profile Picture</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" type="text/css" href="UsingCSS.css">
</head>
<body>

<%
  String modifiedUserName = request.getParameter("originalUserName");
  String returnPage = request.getParameter("returnPage");
  String currentPage = (String) session.getAttribute("currentPage");
  session.setAttribute("previousPage", "currentPage");
  System.out.println("upload.jsp received originalUserName = " + modifiedUserName);
%>


  <div class="row" style="max-width: 900px; margin: 0 auto;">
    <div class="col-s-12 col-12 center-text">
      <h1>Upload Profile Picture</h1>

      <!-- Profile picture upload form -->
      <form action="upload_action.jsp" method="post" enctype="multipart/form-data" style="margin-top: 16px;">
        <input type="file" name="profilePicture" accept="image/*" required style="margin-bottom: 16px;" />
        <input type="hidden" name="userName" value="<%= modifiedUserName %>" />
        <input type="hidden" name="returnPage" value="<%= returnPage %>" />

        <br>
        <input type="submit" class="button" value="Upload" />
      </form>
    </div>
  </div>


</body>
</html>
