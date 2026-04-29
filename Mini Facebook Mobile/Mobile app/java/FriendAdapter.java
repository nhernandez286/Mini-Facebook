package com.example.facebook;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<FriendItem> {
    public FriendAdapter(Activity context, ArrayList<FriendItem> friends) {
        super(context, 0, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendItem friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.textMenuTitle);
        TextView subtitle = convertView.findViewById(R.id.textMenuSubtitle);
        ImageView profilePic = convertView.findViewById(R.id.imageView);

        title.setText(friend.name);
        subtitle.setText(friend.degree + " - " + friend.school + "\n" + friend.location);

        if (friend.profileImage != null) {
            profilePic.setImageDrawable(friend.profileImage);
        } else {
            profilePic.setImageResource(android.R.drawable.sym_def_app_icon);
            new LoadFriendImageTask(profilePic, friend).execute(friend.imageURL);
        }

        return convertView;
    }

    private static class LoadFriendImageTask extends AsyncTask<String, Void, Drawable> {
        private final ImageView imageView;
        private final FriendItem friend;

        public LoadFriendImageTask(ImageView imageView, FriendItem friend) {
            this.imageView = imageView;
            this.friend = friend;
        }

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

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                friend.profileImage = drawable;
                imageView.setImageDrawable(drawable);
            }
        }
    }
}