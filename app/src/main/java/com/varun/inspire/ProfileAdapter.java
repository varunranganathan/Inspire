package com.varun.inspire;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vvvro on 1/2/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private ArrayList<ProfileMessage> solutionPosts;


        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView postDescription;
            public TextView postUserName;
            public ViewHolder(View itemView) {
                super(itemView);
                postUserName = (TextView) itemView.findViewById(R.id.profileMessageName);
                postDescription = (TextView) itemView.findViewById(R.id.profileMessageText);
            }
        }

        public ProfileAdapter(Context myContext, ArrayList<ProfileMessage> myDataset) {
            solutionPosts = myDataset;
            context = myContext;
        }

        @Override
        public int getItemCount() {
            return solutionPosts.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.profile_message_layout, parent, false);
            com.varun.inspire.ProfileAdapter.ViewHolder viewHolder = new com.varun.inspire.ProfileAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ((com.varun.inspire.ProfileAdapter.ViewHolder) holder).postUserName.setText(solutionPosts.get(position).getUsername());
            ((com.varun.inspire.ProfileAdapter.ViewHolder) holder).postDescription.setText(solutionPosts.get(position).getMessage());
        }
}
