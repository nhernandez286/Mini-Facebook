package com.example.facebook;

/***
 * Authors: Nicholas D. Hernandez & Nathalia N. Cotto Figueroa
 * CPEN 410 - Mobile, Web, and Internet Programming
 *
 * This adapter populates a ListView with potential friends returned from a search.
 * It supports downloading and displaying profile pictures, and sending friend requests.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchFriendAdapter extends ArrayAdapter<FriendItem> {

    private final Activity context; // parent activity context
    private final ArrayList<FriendItem> friends; // list of friend items to show
    private final String currentUser; // current logged-in user
    private final String currentPass; // current user's password

    protected String hostAddress = "192.168.1.6:8080"; // local server address
    protected String servletName = "/AddFriendServlet"; // servlet to add friends

    /**
     * Constructor to initialize the adapter with data and credentials.
     *
     * @param context      the parent activity
     * @param friends      the list of FriendItem objects
     * @param currentUser  the current user's username
     * @param currentPass  the current user's password
     */
    public SearchFriendAdapter(Activity context, ArrayList<FriendItem> friends, String currentUser, String currentPass) {
        super(context, 0, friends);
        this.context = context;
        this.friends = friends;
        this.currentUser = currentUser;
        this.currentPass = currentPass;
    }

    /**
     * Builds and returns each row of the friend list view.
     *
     * @param position     position of the current item
     * @param convertView  reusable view
     * @param parent       parent view group
     * @return the fully configured list item view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendItem friend = getItem(position); // get the current friend

        // if no view is available to reuse, inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_search_friend, parent, false);
        }

        // grab UI components from the layout
        TextView title = convertView.findViewById(R.id.textMenuTitle);
        TextView subtitle = convertView.findViewById(R.id.textMenuSubtitle);
        ImageView profilePic = convertView.findViewById(R.id.imageView);
        Button actionButton = convertView.findViewById(R.id.actionButton);

        // set name and basic info
        title.setText(friend.name);
        subtitle.setText(friend.location + "\n" +
                "Gender: " + friend.gender + " | Age: " + friend.age);

        // show profile image if already downloaded
        if (friend.profileImage != null) {
            profilePic.setImageDrawable(friend.profileImage);
        } else {
            // use default icon and try to download if URL is available
            profilePic.setImageResource(android.R.drawable.sym_def_app_icon);
            if (friend.imageURL != null) {
                new LoadFriendImageTask(profilePic, friend).execute(
                        "http://" + hostAddress + "/" + friend.imageURL.replace(" ", "%20")
                );
            }
        }

        // if already friends, disable button and show "Friend"
        if (friend.isFriend) {
            actionButton.setText("Friend");
            actionButton.setEnabled(false);
            actionButton.setTextColor(android.graphics.Color.BLACK);
            actionButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#AAAAAA")));
        } else {
            // if not friends, allow adding
            actionButton.setText("Add Friend");
            actionButton.setEnabled(true);
            actionButton.setTextColor(android.graphics.Color.WHITE);
            actionButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor("#1877F2")));

            // trigger add friend task on button click
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AddFriendTask(friend.userName, actionButton, friend).execute();
                }
            });
        }

        return convertView; // return the complete view
    }

    /**
     * AsyncTask that loads a friend's profile image from the internet.
     */
    private static class LoadFriendImageTask extends AsyncTask<String, Void, Drawable> {
        private final ImageView imageView;
        private final FriendItem friend;

        /**
         * Constructor for image loading task.
         *
         * @param imageView the ImageView to set the image on
         * @param friend    the FriendItem to update
         */
        public LoadFriendImageTask(ImageView imageView, FriendItem friend) {
            this.imageView = imageView;
            this.friend = friend;
        }

        /**
         * Downloads the image in the background.
         *
         * @param urls array of URLs (only the first one is used)
         * @return downloaded Drawable image or null if failed
         */
        @Override
        protected Drawable doInBackground(String... urls) {
            try {
                String encodedUrl = urls[0].replace(" ", "%20");
                InputStream is = new URL(encodedUrl).openStream();
                return Drawable.createFromStream(is, "src");
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Updates the UI with the loaded image.
         *
         * @param drawable the downloaded image
         */
        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                friend.profileImage = drawable;
                imageView.setImageDrawable(drawable);
            }
        }
    }

    /**
     * AsyncTask to send a friend request to the server.
     */
    private class AddFriendTask extends AsyncTask<Void, Void, String> {
        private final String friendUser; // target friend username
        private final Button button; // button to update UI
        private final FriendItem friend; // friend object to update state

        /**
         * Constructor for the friend request task.
         *
         * @param friendUser username of the friend to add
         * @param button     the button to disable on success
         * @param friend     the FriendItem to mark as added
         */
        public AddFriendTask(String friendUser, Button button, FriendItem friend) {
            this.friendUser = friendUser;
            this.button = button;
            this.friend = friend;
        }

        /**
         * Sends the POST request to the AddFriendServlet.
         *
         * @param voids unused
         * @return server response as string
         */
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String fullURL = "http://" + hostAddress + servletName + "?friend=" + URLEncoder.encode(friendUser, "UTF-8");
                HttpHandler handler = new HttpHandler();
                return handler.makeServiceCallPost(fullURL, currentUser, currentPass);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * Handles the server response and updates the UI accordingly.
         *
         * @param result the raw JSON response string
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                if ("OK".equalsIgnoreCase(obj.optString("status"))) {
                    // friend added successfully
                    Toast.makeText(context, "Friend added!", Toast.LENGTH_SHORT).show();
                    button.setText("Friend");
                    button.setEnabled(false);
                    friend.isFriend = true;
                } else if ("INVALID_CREDENTIALS".equalsIgnoreCase(obj.optString("status"))) {
                    // user authentication failed
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                } else {
                    // unknown failure
                    Toast.makeText(context, "Could not add friend.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // JSON or network error
                Toast.makeText(context, "Error adding friend.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
