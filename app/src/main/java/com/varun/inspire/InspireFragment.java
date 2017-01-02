package com.varun.inspire;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InspireFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InspireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspireFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    InspireAdapter inspireAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    EditText inspirePostText;
    Button makeInspirePost;

    FloatingActionButton floatingActionButton;

    ArrayList<InspirePost> mDataSet;

    public InspireFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InspireFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InspireFragment newInstance(String param1, String param2) {
        InspireFragment fragment = new InspireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_inspire, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.inspireFeedRecylerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mDataSet = new ArrayList<InspirePost>();
        loadFeedData();
        inspireAdapter = new InspireAdapter(getContext(), mDataSet);
        recyclerView.setAdapter(inspireAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.inspireFeedSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeedData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        inspirePostText = (EditText) rootView.findViewById(R.id.makeInspirePost);
        makeInspirePost = (Button) rootView.findViewById(R.id.makeInspirePostButton);
        makeInspirePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inspirePostText.getEditableText().toString().matches("")){
                    return;
                }else {
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/makecheerpost", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                            inspirePostText.setText("");
                            loadFeedData();
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
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                            params.put("username",sharedPreferences.getString("user_name",""));
                            params.put("email",sharedPreferences.getString("user_email",""));
                            params.put("post_text", inspirePostText.getEditableText().toString());
                            params.put("type","1");
                            params.put("date", Calendar.getInstance().getTime().toString());
                            params.put("image_url", "null");

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
            }
        });
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.uploadImageFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDataFragment uploadDataFragment = new UploadDataFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, uploadDataFragment).commit();
            }
        });
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

    void loadFeedData(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/cheerfeed", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mDataSet.clear();
                //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray  = jsonObject.getJSONArray("result");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if(jsonObject1.getString("type").matches("1")) {
                            mDataSet.add(new InspirePost(jsonObject1.getInt("chid"), jsonObject1.getString("username"), jsonObject1.getString("email"), jsonObject1.getString("post_text"),jsonObject1.getString("date")));
                        }else{
                            mDataSet.add(new InspirePost(jsonObject1.getInt("chid"), jsonObject1.getString("username"), jsonObject1.getString("email"), jsonObject1.getString("post_text"),jsonObject1.getString("date"), jsonObject1.getString("image_url")));
                        }
                    }
                    inspireAdapter.notifyDataSetChanged();
                }catch(Exception ex) {
                    Log.v("JSONError",ex.toString());
                }
                inspireAdapter.notifyDataSetChanged();
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
}
