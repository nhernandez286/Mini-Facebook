<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*, java.util.*, java.sql.*, java.nio.file.*" %>    
<%@ page import="jakarta.servlet.*" %>  
<%@ page import="jakarta.servlet.http.*" %>
<%@ page import="org.apache.commons.fileupload2.jakarta.servlet5.*" %>  
<%@ page import="org.apache.commons.fileupload2.core.*" %>  
<%@ page import="ut.JAR.CPEN410.applicationDBAuthenticationGoodComplete" %> 

<html>
  <head>
    <title>Profile Picture Upload</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="UsingCSS.css" />
  </head>
  <body>
   
      <div class="row" style="max-width: 900px; margin: 0 auto;">
        <div class="col-s-12 col-12 center-text">
<%
File file;
int maxFileSize = 5000 * 1024;
int maxMemSize = 5000 * 1024;
String filePath = "C:\\Users\\nicho\\Documents\\College\\Mobile Web\\apache-tomcat-10.1.39\\webapps\\ROOT\\Facebook_CSS\\Pictures\\";

String contentType = request.getContentType();
if (contentType != null && contentType.indexOf("multipart/form-data") >= 0) {

    DiskFileItemFactory factory = DiskFileItemFactory.builder().setPath(filePath).get();
    JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);
    upload.setSizeMax(maxFileSize);

    try {
        List<FileItem> fileItems = upload.parseRequest(request);
        String userName = null;
        String returnPage = null;

        for (FileItem fi : fileItems) {
            if (fi.isFormField() && fi.getFieldName().equals("userName")) {
                userName = fi.getString();
            }
            if (fi.getFieldName().equals("returnPage")) {
                returnPage = fi.getString();
            }
        }

        for (FileItem fi : fileItems) {
            if (!fi.isFormField() && userName != null) {
                String fieldName = fi.getFieldName();
                String fileName = fi.getName();

                if (fileName != null && !fileName.isEmpty()) {
                    file = new File(filePath + fileName);
                    Path path = FileSystems.getDefault().getPath(filePath + fileName);
                    fi.write(path);

                    if (fieldName.equals("profilePicture")) {
                        try {
                            String relativePath = "Facebook_CSS/Pictures/" + fileName;
                            applicationDBAuthenticationGoodComplete appDB = new applicationDBAuthenticationGoodComplete();

                            boolean res = appDB.addProfilePicture(userName, relativePath);
                            if (!res) {
                                String previousPage = (String) session.getAttribute("currentPage");
                                appDB.close();
                                session.setAttribute("currentPage", "upload_action.jsp");
%>
                                <h1 class="success-title">Upload successful!</h1>
                                <form id="form_1" action="<%= returnPage %>" method="post">
                                  <input type="submit" value="Back to previous page" class="button"/>
                                </form>
<%
                            } else {
                                appDB.close();
                                request.setAttribute("uploadError", "Failed to save profile picture to DB.");
                            }
                        } catch (Exception e) {
                            request.setAttribute("uploadError", "Database update failed: " + e.getMessage());
                        }
                    }
                }
            }
        }
    } catch (Exception ex) {
        request.setAttribute("uploadError", "Upload failed: " + ex.getMessage());
    }
} else {
    request.setAttribute("uploadError", "No file uploaded.");
}
%>
        </div>
      </div>
 
  </body>
</html>
