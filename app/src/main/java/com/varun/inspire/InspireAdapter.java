package com.varun.inspire;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vvvro on 1/1/2017.
 */

public class InspireAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<InspirePost> mDataSet;
        private Context mContext;
        private RequestQueue queue;

        InspireAdapter(Context context, ArrayList<InspirePost> myDataSet) {
            mContext = context;
            mDataSet = myDataSet;
            queue = Volley.newRequestQueue(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.inspire_post_view_1, parent, false);
                return new InspireTextViewHolder(v);
            } else if (viewType == 2) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.inspire_post_view_2, parent, false);
                return new InspireImageViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder.getItemViewType() == 1) {
                InspireTextViewHolder inspireTextViewHolder = (InspireTextViewHolder) holder;
                inspireTextViewHolder.userName.setText(mDataSet.get(position).getPostUserName());
                inspireTextViewHolder.description.setText(mDataSet.get(position).getPostDescription());
                inspireTextViewHolder.dateTime.setText(mDataSet.get(position).getPostDate());
            } else if (holder.getItemViewType() == 2) {
                final InspireImageViewHolder inspireImageViewHolder = (InspireImageViewHolder) holder;
                inspireImageViewHolder.userName.setText(mDataSet.get(position).getPostUserName());
                inspireImageViewHolder.description.setText(mDataSet.get(position).getPostDescription());
                inspireImageViewHolder.dateTime.setText(mDataSet.get(position).getPostDate());
                Glide.with(mContext).load(mDataSet.get(position).getPostImageURL()).into(inspireImageViewHolder.image);
            }
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mDataSet.get(position).getPostType();
        }

        class InspireTextViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            TextView description;
            TextView dateTime;

            InspireTextViewHolder(View v) {
                super(v);
                userName = (TextView) v.findViewById(R.id.inspireFeedPostUserName);
                description = (TextView) v.findViewById(R.id.inspireFeedPostDescription);
                dateTime = (TextView) v.findViewById(R.id.inspireFeedPostDate);
            }
        }

        class InspireImageViewHolder extends RecyclerView.ViewHolder {
            TextView userName;
            TextView description;
            TextView dateTime;
            ImageView image;

            InspireImageViewHolder(View v) {
                super(v);
                userName = (TextView) v.findViewById(R.id.inspireFeedPostUserNameImage);
                description = (TextView) v.findViewById(R.id.inspireFeedPostDescriptionImage);
                dateTime = (TextView) v.findViewById(R.id.inspireFeedPostDateImage);
                image = (ImageView) v.findViewById(R.id.inspireFeedPostImage);
            }
        }
}
