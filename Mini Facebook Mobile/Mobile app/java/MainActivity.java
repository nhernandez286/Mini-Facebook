package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This class implements the login screen. It takes a username and password,
 * sends them to a servlet for authentication, and opens the user menu upon success.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Activity context
    public Activity activity;

    //Host address
    protected String hostAddress = "192.168.1.6:8080";

    //Autentication Servlet name
    protected String servletName = "/AuthenticateUserServlet";

    /**
     * Initializes the login screen and sets up the submit button listener.
     *
     * @param savedInstanceState saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define current context
        activity = this;

        Button sbButton = findViewById(R.id.buttonSubmit);

        //Create Listener to detect a click
        sbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create and execute a new GetItems thread
                //Every network transaction cannot be performed in the main thread
                new GetItems(activity).execute(); // trigger background task
            }
        });
    }

    /**
     *  This class defines a thread for network transactions.
     *  It performs POST authentication and processes the response.
     */
    private class GetItems extends AsyncTask<Void, Void, Void> {

        // Context: every transaction in a Android application must be attached to a context
        private Activity activity;

        //Server response
        private String serverResponse;

        private String url;

        /**
         * Constructor: assigns the context and builds the servlet URL.
         *
         * @param activity the activity context to use
         */
        protected GetItems(Activity activity) {
            //Define the servlet URL
            url = "http://" + hostAddress + servletName;
            this.activity = activity;
        }

        /**
         * Runs before the background thread; displays a loading message.
         */
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Authenticating..." + url, Toast.LENGTH_LONG).show(); // show loading message
        }

        /**
         * Performs the POST request to authenticate the user.
         *
         * @param arg0 unused
         * @return null (required return type of AsyncTask)
         */
        protected Void doInBackground(Void... arg0) {
            //Read GUI inputs
            String userName, passWord;
            userName = ((EditText) findViewById(R.id.editUsername)).getText().toString();
            passWord = ((EditText) findViewById(R.id.editPwd)).getText().toString();

            //Define a HttpHandler
            HttpHandler handler = new HttpHandler();

            //perform the authentication process and capture the result in serverResponse variable
            serverResponse = handler.makeServiceCallPost(url, userName, passWord); // send POST request

            return null;
        }

        /**
         * Verifies the authentication result.
         * If authenticated, opens the UserMenuActivity.
         * Otherwise, shows an error toast.
         *
         * @param result unused (AsyncTask standard signature)
         */
        protected void onPostExecute(Void result) {
            try {
                // check for empty or null server response
                if (serverResponse == null || serverResponse.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_LONG).show();
                    return;
                }

                // parse JSON from server
                JSONObject responseJson = new JSONObject(serverResponse);
                String status = responseJson.getString("status");

                if (status.equalsIgnoreCase("ok")) {
                    // Success
                    Intent i = new Intent(MainActivity.this, UserMenuActivity.class);
                    i.putExtra("username", ((EditText) findViewById(R.id.editUsername)).getText().toString());
                    i.putExtra("password", ((EditText) findViewById(R.id.editPwd)).getText().toString());
                    startActivity(i); // go to user menu
                } else {
                    // Failure with optional message
                    String message = responseJson.optString("message", "Wrong user or password");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show(); // show error
                }

            } catch (Exception e) {
                // JSON parsing error or unexpected format
                Toast.makeText(getApplicationContext(), "Error parsing server response", Toast.LENGTH_LONG).show();
                e.printStackTrace(); // print debug info
            }
        }
    }
}
