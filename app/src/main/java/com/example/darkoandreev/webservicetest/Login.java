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
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Login extends AppCompatActivity {
    public TextView username, password;
    public String user, pass;
    private ImageView logo;
    private Button loginButton;
    private CheckBox checkBox;
    private Boolean saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loginPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        saveLogin = loginPreferences.getBoolean("hasLoggedIn", false);

        if (saveLogin) {
            Intent intent = new Intent(Login.this, PartidiView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        setContentView(R.layout.login);
        super.onCreate(savedInstanceState);

        loginButton = (Button) findViewById(R.id.button);
        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        logo = (ImageView) findViewById(R.id.logo);
        checkBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        checkBox.setChecked(true);

        logo.setImageResource(R.drawable.logo_eng);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = username.getText().toString();
                pass = password.getText().toString();
                if (user.trim().length() > 0 && pass.trim().length() > 0) {
                    RetrieveFeedTask task = new RetrieveFeedTask(Login.this, user, pass);
                    task.execute(new String[]{"http://vrod.dobritesasedi.bg/rest/accounts"});
                }

            }

        });
    }
}


class RetrieveFeedTask extends AsyncTask<String, String, ArrayList<PartidiInfo>> {
    public String user, pass;
    private Context context;
    public String finalJson;
    public int statusCode;
    private SharedPreferences loginPreferences;

    public RetrieveFeedTask(Context context, String username, String password) {
        this.user = username;
        this.pass = password;
        this.context = context.getApplicationContext();
    }


    @Override
    protected ArrayList<PartidiInfo> doInBackground(String... urls) {

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
            httpGet.setHeader("Content-Type", "application/json");

            try {
                HttpResponse execute = client.execute(httpGet);
                StatusLine statusLine = execute.getStatusLine();
                statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    InputStream content = execute.getEntity().getContent();
                    String status = execute.getStatusLine().toString();
                    Log.d("Status", status);

                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(content));
                    StringBuffer buffer = new StringBuffer();
                    while ((s = bufferReader.readLine()) != null) {
                        buffer.append(s);
                        Log.d("Response", String.valueOf(builder));
                    }

                    finalJson = buffer.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }


    @Override
    protected void onPostExecute(ArrayList<PartidiInfo> partidiInfos) {

        if (statusCode == 200) {
            super.onPostExecute(partidiInfos);
            loginPreferences = context.getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
            loginPrefsEditor.putBoolean("hasLoggedIn", true);
            loginPrefsEditor.putString("username", user);
            loginPrefsEditor.putString("password", pass);
            loginPrefsEditor.putString("finalJson", finalJson);
            loginPrefsEditor.commit();

            Intent intent = new Intent(this.context, PartidiView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(this.context, "Invalid username/password", Toast.LENGTH_LONG).show();
        }
    }
}