package com.rescale.platform.rescalenotifier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Authenticator;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Login extends Activity {
    private ProgressBar spinner;
    private final String baseUrl = "http://192.168.128.236:8000";
    private final String USER_AGENT = "Mozilla/5.0";
    private final String SHARED_PREF_NAME = "rescale_notifier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
    }

    public void logIn(View view) throws Exception {
        final Activity context = this;

        spinner.setVisibility(View.VISIBLE);

        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        final String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        ApiCommunication ac = new ApiCommunication();
        ApiCommunication.LogInInterface logInService = ac.rescaleAdapter.create(ApiCommunication.LogInInterface.class);

        User user = new User(email, password);

        logInService.getToken(user, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                spinner.setVisibility(View.GONE);
                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.commit();

                Intent intent = new Intent(context, ActiveJobStatuses.class);
                intent.putExtra("token", token.token);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

