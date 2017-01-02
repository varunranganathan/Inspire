package com.varun.inspire;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CBTFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CBTFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CBTFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> questions;
    ArrayList<QuestionAnswer> questionAnswers;
    int start;
    int finish;
    int startSession;
    Button proceedButton;
    TextView currentQuestion;
    EditText currentResponse;


    private OnFragmentInteractionListener mListener;

    public CBTFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CBTFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CBTFragment newInstance(String param1, String param2) {
        CBTFragment fragment = new CBTFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_cbt, container, false);
        questions = new ArrayList<String>();
        questionAnswers = new ArrayList<QuestionAnswer>();
        proceedButton = (Button) rootView.findViewById(R.id.nextQuestion);
        currentQuestion = (TextView) rootView.findViewById(R.id.displayQuestion);
        currentResponse = (EditText) rootView.findViewById(R.id.userAnswer);
        currentQuestion.setText("What would you like to Share?");
        setInitialState();
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start < finish) {
                    if (currentResponse.getEditableText().toString().matches("")) {
                        Snackbar.make(v, "Please enter a response", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else if (currentResponse.getEditableText().toString().length() < 5) {
                        Snackbar.make(v, "Please do be a little more specific", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        makeMove();
                    }
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    CBTResultFragment cbtResultFragment = new CBTResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cbt_result", questionAnswers);
                    cbtResultFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.content_main, cbtResultFragment).commit();
                   // showResult();
                   // Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                }
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

    void setInitialState() {
        start = 0;
        finish = 5;
        startSession = 0;
    }

    void loadQuestions(final String message) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/cbtsession", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                questions.clear();
                proceedButton.setClickable(false);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("validity")){
                        Snackbar.make(currentQuestion.getRootView(), "Please elaborate a little more", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        proceedButton.setClickable(true);
                        return;
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("que");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        questions.add(jsonObject1.getString("q"));
                    }
                }catch(Exception ex){
                    Log.e("JSONError", ex.toString());
                }
                start = 0;
                startSession = 1;
                finish = questions.size();
                Log.v("NumQues", String.valueOf(finish));
                questionAnswers.add(new QuestionAnswer("Inital Statement", currentResponse.getText().toString()));
                currentQuestion.setText(questions.get(start));
                currentResponse.setText("");
                proceedButton.setClickable(true);
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
                params.put("msg",message);
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

    void makeMove() {
        if(startSession == 0){
            loadQuestions(currentResponse.getText().toString());
            //Set Start and Finish
            return;
        }else{
            questionAnswers.add(new QuestionAnswer(questions.get(start), currentResponse.getText().toString()));
            start++;
            if(start<questions.size()) {
                currentQuestion.setText(questions.get(start));
                currentResponse.setText("");
            }else {
                FragmentManager fragmentManager = getFragmentManager();
                CBTResultFragment cbtResultFragment = new CBTResultFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("cbt_result", questionAnswers);
                cbtResultFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_main, cbtResultFragment).commit();
            }
        }
    }


}
