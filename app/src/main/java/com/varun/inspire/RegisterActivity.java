package com.varun.inspire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout username, password, name, confirmPassword;
    private Button register;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private String base64;
    private ProgressDialog dialog;
    ImageView registerImage;

    private boolean validEmailId(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPassword(String password) {
        return password.length() >= 6 && password.length() <= 32;
    }

    private boolean validPasswordConfirm(String confirm) {
        return confirm.equals(password.getEditText().getText().toString());
    }

    private boolean validName(String name) {
        return !name.isEmpty();
    }

    private boolean validLogin() {
        boolean e = username.getError() == null;
        boolean p = password.getError() == null;
        boolean cp = confirmPassword.getError() == null;
        boolean n = name.getError() == null;
        return e && p && cp && n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linkViews();
        prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        /*Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.stock);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        base64 = Base64.encodeToString(ba, Base64.DEFAULT);*/
        queue = Volley.newRequestQueue(this);
        username.setErrorEnabled(true);
        password.setErrorEnabled(true);
        name.setErrorEnabled(true);
        confirmPassword.setErrorEnabled(true);
        registerImage = (ImageView) findViewById(R.id.registerImage);
        Glide.with(getApplicationContext()).load(R.drawable.landscape2).into(registerImage);
        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validEmailId(charSequence.toString())) {
                    username.setError(null);
                } else {
                    username.setError("Invalid email id");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validPassword(charSequence.toString())) {
                    password.setError(null);
                } else {
                    password.setError("Password must be between 6 and 32 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validPasswordConfirm(charSequence.toString())) {
                    confirmPassword.setError(null);
                } else {
                    confirmPassword.setError("Passwords must match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validName(charSequence.toString())) {
                    name.setError(null);
                } else {
                    name.setError("Name cannot be empty");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validLogin()) {
                    //TODO Logic to register user
                    registerUser();
                } else {
                    Snackbar.make(view, "Enter valid email id and password!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void linkViews() {
        username = (TextInputLayout) findViewById(R.id.email_reg);
        password = (TextInputLayout) findViewById(R.id.password_reg);
        confirmPassword = (TextInputLayout) findViewById(R.id.password_confirm);
        name = (TextInputLayout) findViewById(R.id.name_reg);
        register = (Button) findViewById(R.id.email_register);
    }

    private void registerUser() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account");
        dialog.show();


        final String URL = "https://inspire.unplayed32.hasura-app.io/signup";
        queue.add(new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("validity")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                        sharedPreferences.edit().putString("user_name", name.getEditText().getText().toString())
                                .putString("user_email", username.getEditText().getText().toString())
                                .putBoolean("auth", true)
                                .apply();
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else {
                        //Toast.makeText(getApplicationContext(),"Error Signing up!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception ex){
                    Log.e("JSONError", ex.toString());
                }
                /*dialog.dismiss();
                Log.d("Register Response", response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.has("status") && res.getBoolean("status")) {
                        //TODO Register the user
                        prefs.edit()
                                .putBoolean("auth", true)
                                .putString("name", name.getEditText().getText().toString())
                                .putString("username", username.getEditText().getText().toString())
                                .putString("image", res.getString("picture"))
                                .apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.d("Register error", error.toString());
                if (error.getMessage() != null)
                    Log.d("Register error body", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("email", username.getEditText().getText().toString());
                params.put("password", password.getEditText().getText().toString());
                params.put("username", name.getEditText().getText().toString());
                return params;
            }
        });
    }
}
