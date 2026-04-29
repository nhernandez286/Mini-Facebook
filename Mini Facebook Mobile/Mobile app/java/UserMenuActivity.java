package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This class displays the user's main menu after a successful login.
 * It validates the session and provides navigation to profile, friend list,
 * search friend, and logout functionalities.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserMenuActivity extends AppCompatActivity {

    private String username, password; // stores username and password received from previous activity

    protected String hostAddress = "192.168.1.6:8080"; // local server address
    protected String servletName = "/AuthenticateUserServlet"; // servlet to validate credentials

    private Button buttonProfile, buttonFriendList, buttonSearchFriend, buttonLogout; // UI buttons

    /**
     * Initializes the user menu UI, gets credentials, and verifies session.
     *
     * @param savedInstanceState previously saved state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu); // set layout for the menu screen

        // Get username and password from intent extras
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // Kick off async task to verify credentials and setup UI
        new GetUser().execute();
    }

    /**
     * Background task to verify user credentials using a POST request.
     * If successful, updates UI with menu options; otherwise returns to login.
     */
    private class GetUser extends AsyncTask<Void, Void, String> {

        /**
         * Sends a POST request to verify user credentials.
         *
         * @param voids unused
         * @return server response string
         */
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler(); // helper to make POST call
            String fullURL = "http://" + hostAddress + servletName; // build full URL
            return sh.makeServiceCallPost(fullURL, username, password); // call servlet
        }

        /**
         * Handles authentication result and updates the menu interface.
         *
         * @param result response from authentication servlet
         */
        @Override
        protected void onPostExecute(String result) {
            // If authentication fails, go back to login
            if (result == null || result.trim().equalsIgnoreCase("not")) {
                Toast.makeText(getApplicationContext(), "Invalid credentials. Please log in again.", Toast.LENGTH_LONG).show();
                Intent i = new Intent(UserMenuActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                // Auth success: show welcome message and initialize buttons
                TextView welcomeText = findViewById(R.id.textWelcome);
                welcomeText.setText("Welcome, " + username + "!");

                // Link button variables to UI elements
                buttonProfile = findViewById(R.id.buttonProfile);
                buttonFriendList = findViewById(R.id.buttonFriendList);
                buttonSearchFriend = findViewById(R.id.buttonSearchFriend);
                buttonLogout = findViewById(R.id.buttonLogout);

                // Navigate to profile view
                buttonProfile.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        Intent i = new Intent(UserMenuActivity.this, profileActivity.class);
                        i.putExtra("username", username);
                        i.putExtra("password", password);
                        startActivity(i);
                    }
                });

                // Navigate to friend list view
                buttonFriendList.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        Intent i = new Intent(UserMenuActivity.this, FriendListActivity.class);
                        i.putExtra("username", username);
                        i.putExtra("password", password);
                        startActivity(i);
                    }
                });

                // Navigate to friend search view
                buttonSearchFriend.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        Intent i = new Intent(UserMenuActivity.this, SearchFriendActivity.class);
                        i.putExtra("username", username);
                        i.putExtra("password", password);
                        startActivity(i);
                    }
                });

                // Logout: go back to login screen and clear activity stack
                buttonLogout.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {
                        Intent i = new Intent(UserMenuActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish(); // close UserMenuActivity
                    }
                });
            }
        }
    }
}
