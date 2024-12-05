package com.example.androidexample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    public static class Friend {
        private String username;
        private String profileImageUrl;
        private String status;

        public Friend(String username) {
            this.username = username;
            this.status = "Online"; // Default status
        }

        public String getUsername() { return username; }
        public String getProfileImageUrl() { return profileImageUrl; }
        public String getStatus() { return status; }
    }

    private List<Friend> friendsList = new ArrayList<>();
    private final Context context;
    private final OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(Friend friend);
        void onRemoveFriend(Friend friend);
    }

    public FriendAdapter(Context context, OnFriendClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendsList.get(position);
        holder.bind(friend);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void updateFriendsList(List<Friend> newList) {
        this.friendsList = newList;
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ImageView profileImage;
        private final TextView username;
        private final TextView status;
        private final ImageButton removeButton;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.friend_profile_image);
            username = itemView.findViewById(R.id.friend_username);
            status = itemView.findViewById(R.id.friend_status);
            removeButton = itemView.findViewById(R.id.remove_friend_button);
        }

        public void bind(Friend friend) {
            username.setText(friend.getUsername());
            status.setText(friend.getStatus());

            itemView.setOnClickListener(v -> listener.onFriendClick(friend));
            removeButton.setOnClickListener(v -> listener.onRemoveFriend(friend));
        }
    }
}