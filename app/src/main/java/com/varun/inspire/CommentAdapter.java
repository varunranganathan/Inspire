package com.varun.inspire;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vvvro on 1/2/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private ArrayList<Comment> solutionPosts;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView postDescription;
            public TextView postUserName;
            public TextView postDate;
            public ImageView thankUserForComment;

            public ViewHolder(View itemView) {
                super(itemView);
                postUserName = (TextView) itemView.findViewById(R.id.commentUserName);
                postDescription = (TextView) itemView.findViewById(R.id.commentDescription);
                postDate = (TextView) itemView.findViewById(R.id.commentDate);
                thankUserForComment = (ImageView) itemView.findViewById(R.id.thankUserForComment);
            }
        }

        public CommentAdapter(Context myContext, ArrayList<Comment> myDataset) {
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
                    .inflate(R.layout.comment_view, parent, false);
            com.varun.inspire.CommentAdapter.ViewHolder viewHolder = new com.varun.inspire.CommentAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ((com.varun.inspire.CommentAdapter.ViewHolder) holder).postUserName.setText(solutionPosts.get(position).getPostUserName());
            ((com.varun.inspire.CommentAdapter.ViewHolder) holder).postDate.setText(solutionPosts.get(position).getPostDate());
            ((com.varun.inspire.CommentAdapter.ViewHolder) holder).postDescription.setText(solutionPosts.get(position).getPostDescription());
            ((ViewHolder) holder).thankUserForComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Thank the person who commented!");
                    // Set up the input
                    final EditText input = new EditText(context);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(context, input.getText().toString(), Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
                            if(input.getText().toString().matches("") || sharedPreferences.getString("user_email","").matches(solutionPosts.get(position).getPostUserEmail())) return;
                            RequestQueue queue = Volley.newRequestQueue(context);
                            StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/postmessage", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Volley Error!",error.toString());
                                }
                            }){

                                @Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();
                                    SharedPreferences sharedPreferences = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
                                    params.put("from_user", sharedPreferences.getString("user_name",""));
                                    params.put("to_user", solutionPosts.get(position).getPostUserName());
                                    params.put("from_email", sharedPreferences.getString("user_email", ""));
                                    params.put("to_email", solutionPosts.get(position).getPostUserEmail());
                                    params.put("msg_text", input.getText().toString());
                                    params.put("pid", String.valueOf(solutionPosts.get(position).getPostID()));
                                    params.put("cid", String.valueOf(solutionPosts.get(position).getCommentID()));
                                    return params;
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Content-Type","application/json");
                                    return params;
                                }
                            };
                            queue.add(sr);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }
}
