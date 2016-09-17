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
import android.view.WindowManager;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String USER_AUTH = "user_auth";
    public static final String TIME_STAMP = "Time_stamp";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PASS = "user_pass";
    public static final String TOKEN = "token";
    public static final String USER_ID = "user_id";


    EditText _emailText;

    EditText _passwordText;

    Button _loginButton;
    SharedPreferences pref,prefs;

    TextView _signupLink;
    Retrofit retrofit, retrofit1;
    Observable<LoginRegister> loginObservable;
    Observable<Data> eventsObservable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_login);
        DBHandler db = new DBHandler(this);
        pref = getSharedPreferences(USER_AUTH, Context.MODE_PRIVATE);
        prefs = getSharedPreferences(TIME_STAMP, Context.MODE_PRIVATE);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(v -> login());

        _signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
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
        final String password = _passwordText.getText().toString();

        String defaultValue = "Not yet updated";
        String time = prefs.getString("time", defaultValue);
        if(time.equals(defaultValue)) {
            callDB(this);
        }

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Utilities.base_url)
                .build();

        LoginService loginService = retrofit.create(LoginService.class);

        loginObservable = loginService.authenticate(email,password);

        loginObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(login -> {
                    if(login.getStatusCode()==200) {
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = pref.edit();
                        Utilities.status = 2;
                        editor.putInt("Logged_in", Utilities.status);
                        editor.putString(USER_EMAIL, email);
                        Utilities.username = email;
                        editor.putString("user_pass", password);
                        editor.putInt("login_status",2);
                        Utilities.password = password;
                        String[] parts = email.split("(?=@)");
                        String part1 = parts[0]; // 004
                        editor.putString("token", login.getMessage());
                        editor.putString("user_name",part1);
                        Utilities.user_profile_name = part1;
                        editor.putString(USER_PASS, password);
                        Utilities.password = password;
                        editor.putString(TOKEN, login.getMessage());
                        Utilities.token = login.getMessage();
                        editor.putString(USER_ID, login.getUserId());
                        Utilities.user_id = login.getUserId();
                        editor.apply();

                        callCheck_auth();
                       Log.e("fck ", Utilities.username + " " + Utilities.user_profile_name);
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this, MainMenu.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        onLoginFailed();
                    }
                });
    }

    public void callDB(Context context) {
        DBHandler db = new DBHandler(this);
        new DBHandler(context);
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching data...");
        dialog.show();

        retrofit1 = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Utilities.base_url)
                .build();

        EventsInterface eventsInterface = retrofit1.create(EventsInterface.class);

        eventsObservable = eventsInterface.getEvents();

        eventsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.getStatusCode()== 200) {
                        for (Events event : data.getEvents()) {
                            db.addEvent(event);

                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
                        String formattedDate = sdf.format(date);
                        //System.out.println(formattedDate);
                        editor.putString("time",""+formattedDate);
                        editor.apply();
                    } else {
                        String defaultValue = "Not yet updated";
                        String time = prefs.getString("time", defaultValue);
                        Toast.makeText(LoginActivity.this," Events Last updated at "+ time,Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                _emailText.setText(data.getExtras().getString("username"));
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
    public void callCheck_auth()
    {
        String temp2 = "";
        if (Utilities.username.contains("@")) {
            // Split it.
            String[] temp = Utilities.username.split("@");
            temp2 = temp[1];

        }
        if(temp2.equals("nitt.edu"))
            Utilities.user_qr = Utilities.base_url + "/tshirt/qr";
        else
            Utilities.user_qr = Utilities.base_url + "/pr/qr";

    }

}
