package com.example.darkoandreev.webservicetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

//var url = "../rest/accounts/" + $.QueryString["account"] + "/statement";
//var url = "../rest/accounts/" + $.QueryString["account"] + "/invoices/" + $.QueryString["doc"];
//var url = "../rest/accounts/" + $.QueryString["account"] + "/payments/" + $.QueryString["doc"];

public class Login extends AppCompatActivity {
    private TextView username, password;
    private ImageView logo;
    private Button loginButton;
    private CheckBox checkBox;
    private Boolean saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        loginButton = (Button) findViewById(R.id.button);
        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        logo = (ImageView) findViewById(R.id.logo);
        checkBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);

        logo.setImageResource(R.drawable.logo_eng);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", true);

        if (saveLogin == true) {
            username.setText(loginPreferences.getString("username", ""));
            password.setText(loginPreferences.getString("password", ""));
            checkBox.setChecked(true);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if(v == loginButton) {
                    if(checkBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", user);
                        loginPrefsEditor.putString("password", pass);
                        loginPrefsEditor.commit();
                        RetrieveFeedTask task = new RetrieveFeedTask(Login.this, user, pass);
                        task.execute(new String[]{"http://vrod.dobritesasedi.bg/rest/accounts/100010002001/statement"});
                        opetBatches();
                        Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                }

            }
        });
    }

    public void opetBatches () {
        Intent intent = new Intent(Login.this, Property.class);
        startActivity(intent);
    }

}


class RetrieveFeedTask extends AsyncTask<String, Void, String> {
    private String user, pass;
    private Context context;
    private JSONArray jsonArray = null;

    public RetrieveFeedTask (Context context, String username, String password) {
        this.user = username;
        this.pass = password;
        this.context = context;
    }


    @Override
    protected String doInBackground(String... urls) {

        String credentials = this.user + ":" + this.pass;
        Log.d("User: ", this.user);
        Log.d("Pass: ", this.pass);
        String credBase64 = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT).replace("\n", "");

        String s = "";
        for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Basic " + credBase64);
            httpGet.setHeader("Accept", "application/json;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
         //   httpGet.setHeader("Content-Type", "application/json");

            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();
                String status = execute.getStatusLine().toString();
                Log.d("Status", status);

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
               // String s = "";
                while ((s = buffer.readLine()) != null) {
                   builder.append(s);
                    Log.d("Response", String.valueOf(builder));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonArray = new JSONArray(builder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return String.valueOf(jsonArray);
    }

}


