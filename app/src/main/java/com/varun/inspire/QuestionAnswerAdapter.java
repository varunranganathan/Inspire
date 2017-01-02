package com.varun.inspire;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vvvro on 1/2/2017.
 */

public class QuestionAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<QuestionAnswer> solutionPosts;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public TextView answer;
        public ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.cbtResultQuestion);
            answer = (TextView) itemView.findViewById(R.id.cbtResultAnswer);
        }
    }

    public QuestionAnswerAdapter(Context myContext, ArrayList<QuestionAnswer> myDataset) {
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
                .inflate(R.layout.cbt_result_view, parent, false);
        com.varun.inspire.QuestionAnswerAdapter.ViewHolder viewHolder = new com.varun.inspire.QuestionAnswerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).question.setText(solutionPosts.get(position).getQuestion());
        ((ViewHolder) holder).answer.setText(solutionPosts.get(position).getAnswer());
    }
}
