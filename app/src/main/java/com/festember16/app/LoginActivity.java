package com.festember16.app;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    EditText _emailText;

    EditText _passwordText;

    Button _loginButton;
    SharedPreferences pref;

    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
      final   String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.auth_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int status = 0;
                        try {
                            status = jsonResponse.getInt("status_code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String message = null;
                        try {
                            message = jsonResponse.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        switch (status) {

                            case 200:

                                SharedPreferences.Editor editor = pref.edit();
                                Utilities.status = 1;
                                editor.putInt("Logged_in", Utilities.status);
                                editor.putString("user_email", email);
                                Utilities.username = email;
                                editor.putString("user_pass", password);
                                Utilities.password = password;
                                editor.putString("token", message);
                                Utilities.token = message;
                                editor.apply();


                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(LoginActivity.this, MainMenu.class);
                                startActivity(i);

                                break;

                            default:
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                _emailText.setText("");
                                _passwordText.setText("");
                                onLoginFailed();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        onLoginFailed();
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("user_email",email);
            params.put("user_pass",password);
            return params;
        }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("enter valid password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/festember/";
    public static String FACEBOOK_PAGE_ID = "festember";

    //method to get the right URL to use in the intent
    public static String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }



    public void callFB(View view) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(this);
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);
    }

    public void callInsta(View view)
    {
        Uri uri = Uri.parse("http://instagram.com/_u/festember");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/festember")));
        }
    }

    public void callTweet(View view)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://festember"));
            intent.setPackage("com.twitter.android");
            startActivity(intent);

        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/festember")));
        }

    }

}