package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This class displays the user's profile screen. It fetches profile data and image
 * from the backend and presents it on screen. It also allows returning to the user menu.
 */

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.net.URL;

public class profileActivity extends AppCompatActivity {

    private String username, password; // received credentials
    private ImageView profileImage; // profile picture view
    private String imageURL; // full path to the profile image

    protected String hostAddress = "192.168.1.6:8080"; // backend IP address
    protected String servletName = "/ProfileServlet"; // servlet to retrieve user profile

    /**
     * Called when the activity is starting. Loads user data and sets up UI.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // load profile screen layout

        // get username and password from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // fetch user data from the server
        new GetUserProfile().execute();

        // setup back button to return to the user menu
        Button buttonBack = findViewById(R.id.buttonBackProfile);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, UserMenuActivity.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish(); // close profileActivity
            }
        });
    }

    /**
     * AsyncTask to fetch the user's profile JSON from the server.
     */
    private class GetUserProfile extends AsyncTask<Void, Void, String> {

        /**
         * Sends a POST request to retrieve profile JSON.
         *
         * @param voids unused
         * @return JSON string from the servlet or "not" on failure
         */
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String fullURL = "http://" + hostAddress + servletName;
            return handler.makeServiceCallPost(fullURL, username, password); // send POST request
        }

        /**
         * Processes the JSON response and displays profile details.
         *
         * @param json the JSON response string from the server
         */
        @Override
        protected void onPostExecute(String json) {
            if (json == null || json.trim().equalsIgnoreCase("not")) {
                Toast.makeText(profileActivity.this, "Failed to fetch profile info", Toast.LENGTH_LONG).show();
                return;
            }

            // parse the response into jsonPerson object
            jsonPerson jPerson = new jsonPerson(json);

            // display user info in the TextView
            ((TextView) findViewById(R.id.textUserName)).setText("Welcome " + jPerson.getName()
                    + "\n- Date of Birth: " + jPerson.getDateOfBirth()
                    + "\n- Gender: " + jPerson.getGender()
                    + "\n- Street: " + jPerson.getStreet()
                    + "\n- Town: " + jPerson.getTown()
                    + "\n- State: " + jPerson.getState()
                    + "\n- Country: " + jPerson.getCountry()
                    + "\n- Degree: " + jPerson.getDegree()
                    + "\n- School: " + jPerson.getSchool()
            );

            // prepare image loading
            profileImage = findViewById(R.id.imageProfile);
            String fullPath = jPerson.getProfilePicture();
            imageURL = "http://" + hostAddress + "/" + fullPath;

            // fetch and display the profile image
            new GetProfilePicture().execute();
        }
    }

    /**
     * Loads an image from the given URL and returns it as a Drawable.
     *
     * @param url the full image URL
     * @return Drawable object of the image, or null if failed
     */
    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AsyncTask to load and set the user's profile image from URL.
     */
    private class GetProfilePicture extends AsyncTask<Void, Void, Drawable> {

        /**
         * Downloads the image in the background.
         *
         * @param voids unused
         * @return Drawable profile picture, or null if loading failed
         */
        @Override
        protected Drawable doInBackground(Void... voids) {
            return LoadImageFromWebOperations(imageURL); // download image
        }

        /**
         * Displays the image if successful; shows error if failed.
         *
         * @param result the loaded Drawable object
         */
        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                profileImage.setImageDrawable(result); // show image
            } else {
                Toast.makeText(profileActivity.this, "Image failed to load from: " + imageURL, Toast.LENGTH_LONG).show();
            }
        }
    }
}
