package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This class displays the friend search interface. It verifies credentials,
 * allows filtering by form criteria, and shows matching users from the server.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchFriendActivity extends Activity {

    private String username;
    private String password;

    // form inputs
    private EditText editTown, editState, editCountry, editAge;
    private Spinner spinnerGender;
    private Button buttonSearch;
    private ListView resultListView;

    private ArrayList<FriendItem> resultList; // search result list
    private SearchFriendAdapter adapter; // custom adapter to populate list view

    protected String hostAddress = "192.168.1.6:8080"; // local server address
    protected String servletName = "/SearchFriendServlet"; // search friend endpoint
    protected String authServlet = "/AuthenticateUserServlet"; // auth check endpoint

    /**
     * Initializes the UI, disables input, verifies credentials, and sets up listeners.
     *
     * @param savedInstanceState previously saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend); // load layout

        // disable UI actions until user is verified
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setEnabled(false);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setEnabled(false);

        // get credentials passed from previous screen
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        // verify credentials before allowing search
        new AuthenticateBeforeSearch().execute();

        // link form fields to layout
        editTown = findViewById(R.id.editTown);
        editState = findViewById(R.id.editState);
        editCountry = findViewById(R.id.editCountry);
        editAge = findViewById(R.id.editAge);
        spinnerGender = findViewById(R.id.spinnerGender);
        resultListView = findViewById(R.id.listViewSearchResults);

        // populate gender dropdown
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Any", "Male", "Female", "Other"});
        spinnerGender.setAdapter(genderAdapter);

        // set up result list and adapter
        resultList = new ArrayList<>();
        adapter = new SearchFriendAdapter(this, resultList, username, password);
        resultListView.setAdapter(adapter);

        // search button logic
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadSearchResults().execute(); // fetch results
            }
        });

        // back button logic
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchFriendActivity.this, UserMenuActivity.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish(); // finish this activity
            }
        });
    }

    /**
     * AsyncTask to search for users based on entered filter criteria.
     */
    private class LoadSearchResults extends AsyncTask<Void, Void, String> {

        /**
         * Builds the full query URL and sends the POST request.
         *
         * @param voids unused
         * @return server response as JSON string
         */
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();

            // collect form data
            String town = editTown.getText().toString().trim();
            String state = editState.getText().toString().trim();
            String country = editCountry.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();
            if (gender.equals("Any")) gender = ""; // treat "Any" as empty string

            String fullURL = "http://" + hostAddress + servletName
                    + "?town=" + town.replace(" ", "%20")
                    + "&state=" + state.replace(" ", "%20")
                    + "&country=" + country.replace(" ", "%20")
                    + "&gender=" + gender.replace(" ", "%20")
                    + "&age=" + age.replace(" ", "%20");

            // send POST with username/password as payload
            return handler.makeServiceCallPost(fullURL, username, password);
        }

        /**
         * Parses the server response and updates the list view with results.
         *
         * @param response the raw JSON string returned from the server
         */
        @Override
        protected void onPostExecute(String response) {
            resultList.clear(); // clear previous results

            try {
                JSONObject obj = new JSONObject(response);
                JSONArray users = obj.getJSONArray("users");

                // show message if nothing was found
                if (users.length() == 0) {
                    Toast.makeText(SearchFriendActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    return;
                }

                // build list from results
                for (int i = 0; i < users.length(); i++) {
                    JSONObject u = users.getJSONObject(i);

                    // extract fields from JSON
                    String name = u.optString("name", "");
                    String user = u.optString("userName", "");
                    String pic = u.optString("profilePicture", "");
                    String school = u.optString("school", "");
                    String degree = u.optString("degree", "");
                    String town = u.optString("town", "");
                    String state = u.optString("state", "");
                    String location = town + ", " + state;
                    String gender = u.optString("gender", "");
                    String age = String.valueOf(u.optInt("age", 0));
                    boolean isFriend = u.optBoolean("isFriend", false);

                    // add to result list
                    resultList.add(new FriendItem(
                            name, degree, school, location,
                            null, user, isFriend, pic, gender, age
                    ));
                }

                adapter.notifyDataSetChanged(); // refresh UI

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SearchFriendActivity.this, "Failed to load search results", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * AsyncTask to check if the user is authenticated before allowing search.
     */
    private class AuthenticateBeforeSearch extends AsyncTask<Void, Void, String> {

        /**
         * Sends a POST request to verify user credentials.
         *
         * @param voids unused
         * @return server response indicating auth status
         */
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String fullURL = "http://" + hostAddress + authServlet;
            return handler.makeServiceCallPost(fullURL, username, password);
        }

        /**
         * Parses response and enables UI if authenticated; closes otherwise.
         *
         * @param result the raw server response
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                // no server reply
                if (result == null || result.trim().isEmpty()) {
                    Toast.makeText(SearchFriendActivity.this, "No response from server", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                // parse and validate auth response
                JSONObject obj = new JSONObject(result);
                if (!obj.getString("status").equalsIgnoreCase("ok")) {
                    Toast.makeText(SearchFriendActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // enable search and back buttons
                    buttonSearch.setEnabled(true);
                    findViewById(R.id.buttonBack).setEnabled(true);
                }
            } catch (Exception e) {
                Toast.makeText(SearchFriendActivity.this, "Invalid login response", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
