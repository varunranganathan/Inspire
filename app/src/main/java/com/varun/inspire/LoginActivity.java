package com.varun.inspire;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public TextView email;
    public TextView password;
    public Button signInButton;
    public Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);

        final SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("auth", false)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final View tempView = v;
                final String emailAddress = email.getEditableText().toString();
                final String userPassword = password.getEditableText().toString();
                if(emailAddress==null || emailAddress.matches("")){
                    Snackbar.make(v, "Please Enter an email address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if(userPassword==null || userPassword.matches("")){
                    Snackbar.make(v, "Please enter a password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    return;
                }
                if(!isValidEmail(emailAddress)){
                    Snackbar.make(v, "Please enter a valid email address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    return;
                }
                if(!isValidPassword(userPassword)){
                    Snackbar.make(v, "The password must be between 6 and 32 characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest sr = new StringRequest(Request.Method.POST,"https://inspire.unplayed32.hasura-app.io/login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        if(validResponse(response)) {
                            sharedPreferences.edit().putString("user_name", getUserNameFromResponse(response))
                                    .putString("user_email", emailAddress)
                                    .putBoolean("auth", true)
                                    .apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else {
                            Snackbar.make(tempView, "Invalid Credentials", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
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
                        params.put("email",emailAddress);
                        params.put("password",userPassword);

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
                /*
                queue.add(new StringRequest(Request.Method.GET, "https://inspire.unplayed32.hasura-app.io/login/", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Validating", Toast.LENGTH_SHORT).show();
                        if(validResponse(response)){
                            sharedPreferences.edit().putString("user_name", getUserNameFromResponse(response))
                                    .putString("user_email", emailAddress)
                                    .putBoolean("auth", true)
                                    .apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Inavlid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email", "vvvrock@gmail.com");
                        hashMap.put("password","vrocktest");
                        return hashMap;
                    }
                });*/
                //Try to sign in
                //Go to main activity
            }
        });
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Register Activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public  Boolean isValidPassword(String password){
        return password.length() > 6 && password.length() < 32;
    }

    public boolean validResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getBoolean("validity")==true) return true;
            else return false;
        }catch(Exception ex){
            Log.v("LoginActivity", ex.toString());
            return false;
        }
    }

    public String getUserNameFromResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("username");
        }catch(Exception ex){
            Log.v("LoginActivity", ex.toString());
            return "";
        }
    }
}

