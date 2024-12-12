package com.example.androidexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidexample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    public static class MuscleProgress {
        public String muscle;
        public double percentage;
        public int tier;
        public double total_progress;
        public double amount_to_next_tier;

        public MuscleProgress() {}
    }

    public static class Friend {
        private int id;
        private String username;
        private int age;
        private String bio;
        private double weight;
        private int height;
        private Map<String, MuscleProgress> muscleProgress;

        public Friend(int id, String username, int age) {
            this.id = id;
            this.username = username;
            this.age = age;
            this.muscleProgress = new HashMap<>();
        }

        // Getters
        public int getId() { return id; }
        public String getUsername() { return username; }
        public int getAge() { return age; }
        public String getBio() { return bio; }
        public double getWeight() { return weight; }
        public int getHeight() { return height; }
        public Map<String, MuscleProgress> getMuscleProgress() { return muscleProgress; }

        public void setFullProfile(String bio, double weight, int height,
                                   Map<String, MuscleProgress> muscleProgress) {
            this.bio = bio;
            this.weight = weight;
            this.height = height;
            if (muscleProgress != null) {
                this.muscleProgress = muscleProgress;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Friend friend = (Friend) o;
            return id == friend.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    private List<Friend> friendsList;
    private final Context context;
    private final OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(Friend friend);
        void onRemoveFriend(Friend friend);
    }

    public FriendAdapter(Context context, OnFriendClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.friendsList = new ArrayList<>();
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
        this.friendsList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void addFriend(Friend friend) {
        if (!friendsList.contains(friend)) {
            friendsList.add(friend);
            notifyItemInserted(friendsList.size() - 1);
        }
    }

    public void removeFriend(Friend friend) {
        int position = friendsList.indexOf(friend);
        if (position != -1) {
            friendsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFriend(Friend updatedFriend) {
        int position = -1;
        for (int i = 0; i < friendsList.size(); i++) {
            if (friendsList.get(i).getId() == updatedFriend.getId()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            friendsList.set(position, updatedFriend);
            notifyItemChanged(position);
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView usernameText;
        private final TextView ageText;
        private final ImageButton removeButton;
        private final View itemContainer;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.friend_username);
            ageText = itemView.findViewById(R.id.friend_age);
            removeButton = itemView.findViewById(R.id.remove_friend_button);
            itemContainer = itemView.findViewById(R.id.friend_item_container);
        }

        @SuppressLint("ResourceType")
        public void bind(Friend friend) {
            // Set username
            usernameText.setText(friend.getUsername());

            // Set age with proper formatting
            ageText.setText(context.getString(R.string.age_format, friend.getAge()));

            // Set click listeners
            itemContainer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFriendClick(friend);
                }
            });

            removeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveFriend(friend);
                }
            });

            // Add ripple effect for better touch feedback

        }
    }
}