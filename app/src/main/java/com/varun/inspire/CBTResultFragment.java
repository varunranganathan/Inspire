package com.varun.inspire;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CBTResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CBTResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CBTResultFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    QuestionAnswerAdapter questionAnswerAdapter;

    ArrayList<QuestionAnswer> mDataSet;

    private OnFragmentInteractionListener mListener;

    public CBTResultFragment() {
        // Required empty public constructor
    }

    public static CBTResultFragment newInstance(String param1, String param2) {
        CBTResultFragment fragment = new CBTResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataSet = (ArrayList<QuestionAnswer>) getArguments().getSerializable("cbt_result");
            Log.v("CBTResult","Transfer success!");
        }else{
            mDataSet = new ArrayList<QuestionAnswer>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cbtresult, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cbtResultFeed);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        questionAnswerAdapter = new QuestionAnswerAdapter(getContext(), mDataSet);
        recyclerView.setAdapter(questionAnswerAdapter);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
