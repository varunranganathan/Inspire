package com.varun.inspire;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vvvro on 1/1/2017.
 */

public class SolutionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<SolutionPost> solutionPosts;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView postDescription;
        public TextView postUserName;
        public TextView postDate;
        public Button postComment;
        public ViewHolder(View itemView) {
            super(itemView);
            postUserName = (TextView) itemView.findViewById(R.id.solutionFeedPostUserName);
            postDescription = (TextView) itemView.findViewById(R.id.solutionFeedPostDescription);
            postDate = (TextView) itemView.findViewById(R.id.solutionFeedPostDate);
            postComment = (Button) itemView.findViewById(R.id.solutionFeedPostComment);
        }
    }

    public SolutionAdapter(Context myContext, ArrayList<SolutionPost> myDataset) {
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
                .inflate(R.layout.post_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).postUserName.setText(solutionPosts.get(position).getPostUserName());
        ((ViewHolder) holder).postDate.setText(solutionPosts.get(position).getPostDate());
        ((ViewHolder) holder).postDescription.setText(solutionPosts.get(position).getPostDescription());
        ((ViewHolder) holder).postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load Comment View
                CommentFragment commentFragment = new CommentFragment();
                Bundle args = new Bundle();
                args.putInt("post_id", solutionPosts.get(position).getPostID());
                commentFragment.setArguments(args);
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, commentFragment).commit();
            }
        });
    }
}
