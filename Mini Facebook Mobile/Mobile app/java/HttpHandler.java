package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 *  CPEN 410 - Mobile, Web, and Internet Programming
 *
 *  This class downloads the JSON data from a web server.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    /***
     *  Default constructor
     */
    public HttpHandler() {
    }

    /**
     * This method performs a POST request to the given URL using a username and password.
     *
     * @param reqUrl  Target URL for the request
     * @param userName Username to send as POST parameter
     * @param pass     Password to send as POST parameter
     * @return JSON response from the server as a String, or null on failure
     */
    public String makeServiceCallPost(String reqUrl, String userName, String pass) {
        String response = null; // HTTP response holder
        try {
            //Generate a URL object from the requested URL
            URL url = new URL(reqUrl);
            // Create a Http Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Define Request POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Define the parameters list
            String parameters = "user=" + userName + "&pass=" + pass;

            // Establish the option for sending parameters using the POST method
            conn.setDoOutput(true);

            // Add the parameters list to the http request
            conn.getOutputStream().write(parameters.getBytes("UTF-8"));

            // Read the response from server
            InputStream in = new BufferedInputStream(conn.getInputStream());

            // Convert the InputStream into a String
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            System.out.println("ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * This method performs a GET request to the given URL.
     *
     * @param reqUrl Target URL for the request
     * @return JSON response from the server as a String, or null on failure
     */
    public String makeServiceCall(String reqUrl) {
        String response = null; // HTTP response holder
        try {
            // Generate a URL object from the requested URL
            URL url = new URL(reqUrl);

            // Create a Http Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Define Request GET
            conn.setRequestMethod("GET");

            System.out.println(conn.getRequestProperties().toString());

            // Read the response from server
            InputStream in = new BufferedInputStream(conn.getInputStream());

            // Convert the InputStream into a String
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            System.out.println("ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * This method converts the content of an InputStream into a single String.
     *
     * @param is InputStream to convert
     * @return Combined string content of the stream
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is)); // wrap the InputStream
        StringBuilder sb = new StringBuilder(); // container for lines

        String line;
        try {
            // Traverse the InputStream and append each line
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace(); // log error if stream fails
        } finally {
            try {
                is.close(); // ensure stream is closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString(); // return final string
    }
}
