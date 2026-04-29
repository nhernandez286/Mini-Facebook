package com.example.facebook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FriendListActivity extends Activity {

    protected String hostAddress = "192.168.1.6:8080";
    protected String servletName = "/GetFriendListServlet";

    private String username;
    private String password;

    private ListView friendListView;
    private ArrayList<FriendItem> friendList;
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        friendListView = findViewById(R.id.friendListView);
        friendList = new ArrayList<>();
        adapter = new FriendAdapter(this, friendList);
        friendListView.setAdapter(adapter);

        new LoadFriends().execute();

        Button buttonBack = findViewById(R.id.buttonBackFriendList);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FriendListActivity.this, UserMenuActivity.class);
                i.putExtra("username", username);
                i.putExtra("password", password);
                startActivity(i);
                finish();
            }
        });
    }

    private class LoadFriends extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String fullURL = "http://" + hostAddress + servletName;
            return handler.makeServiceCallPost(fullURL, username, password);
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject json = new JSONObject(response);
                JSONArray array = json.getJSONArray("friends");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject f = array.getJSONObject(i);

                    String name = f.getString("name");
                    String school = f.getString("school");
                    String degree = f.getString("degree");
                    String location = f.getString("town") + ", " + f.getString("state") + ", " + f.getString("country");
                    String imagePath = f.getString("profilePicture");

                    String fullURL = "http://" + hostAddress + "/" + imagePath;
                    Drawable image = loadImage(fullURL);

                    friendList.add(new FriendItem(name, degree, school, location, fullURL));
                }

                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(FriendListActivity.this, "Failed to load friends", Toast.LENGTH_LONG).show();
            }
        }

        private Drawable loadImage(String url) {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                return Drawable.createFromStream(is, "src");
            } catch (Exception e) {
                return null;
            }
        }
    }
}
