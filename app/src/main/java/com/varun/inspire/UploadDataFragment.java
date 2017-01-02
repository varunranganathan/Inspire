package com.varun.inspire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
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
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView inspireFeedUploadImage;
    private Uri imageURI = null;
    private InputStream inputStream = null;
    
    private TextView imageDescription;

    private Button uploadDataButton;

    private OnFragmentInteractionListener mListener;

    public UploadDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadDataFragment newInstance(String param1, String param2) {
        UploadDataFragment fragment = new UploadDataFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_upload_data, container, false);
        inspireFeedUploadImage = (ImageView) rootView.findViewById(R.id.inspireFeedUploadImage);
        Glide.with(getContext()).load(R.drawable.stock_upload_image).into(inspireFeedUploadImage);
        imageDescription = (TextView) rootView.findViewById(R.id.imageDescription); 
        inspireFeedUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7000);
            }
        });
        uploadDataButton = (Button) rootView.findViewById(R.id.uploadData);
        uploadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputStream==null) {
                    Snackbar.make(v, "Please choose an Image", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
                downloadFilesTask.execute(inputStream);
            }
        });
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    FragmentManager fragmentManager = getFragmentManager();
                    InspireFragment inspireFragment = new InspireFragment();
                    fragmentManager.beginTransaction().replace(R.id.content_main, inspireFragment).commit();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==7000&&resultCode== Activity.RESULT_OK){
            if(data==null){
                return;
            }else{
                try {
                    inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    imageURI = data.getData();
                    Glide.with(this).load(data.getData()).into(inspireFeedUploadImage);
                }catch (Exception ex){
                    Log.v("MainActivity","Image not Found!");
                }
            }
        }
    }
    private class DownloadFilesTask extends AsyncTask<InputStream, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(InputStream... params) {
            Map config = new HashMap();
            config.put("cloud_name", "dhjgpncfa");
            config.put("api_key", "834952152399412");
            config.put("api_secret", "tMDWR-9vYdmBIvu6d3SFfgnyX9c");
            final Cloudinary cloudinary = new Cloudinary(config);
            try {
                Map uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                return url;
            }catch (Exception ex){
                Log.e("Image Upload Error", ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Upload to Server 
            final String imageURL = s;
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/makecheerpost", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    imageDescription.setText("");
                    FragmentManager fragmentManager = getFragmentManager();
                    InspireFragment inspireFragment = new InspireFragment();
                    fragmentManager.beginTransaction().replace(R.id.content_main, inspireFragment).commit();
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
                    params.put("post_text", imageDescription.getEditableText().toString());
                    params.put("type","2"); 
                    params.put("date", Calendar.getInstance().getTime().toString());
                    params.put("image_url", imageURL);

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

}
