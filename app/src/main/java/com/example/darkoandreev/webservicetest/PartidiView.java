package com.example.darkoandreev.webservicetest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.darkoandreev.webservicetest.DocumentsModel.Documents;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.darkoandreev.webservicetest.R.layout.property;

/**
 * Created by darko.andreev on 6/2/2017.
 */

public class PartidiView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public TextView partidaNomer, partidaBalance, partidaProperyRefs;
    private ListView partidaList;
    private String partidaNomerJson;
    private String partidaBalanceJson;
    private String partidaPropertyRefsJson;
    private String [] partidi;
    private boolean hasLoggedIn;

    ArrayList<PartidiInfo> arrayOfDocuments = new ArrayList<PartidiInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(property);
            partidaList = (ListView) findViewById(R.id.partidaList);
            MyPartidiAdapter adapter = new MyPartidiAdapter(this, arrayOfDocuments);
            partidaList.setAdapter(adapter);

            partidiJSONParse();

            partidaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DocumentsTask task = new DocumentsTask(PartidiView.this);
                    task.execute(new String[]{"http://vrod.dobritesasedi.bg/rest/accounts/" + partidi[position] + "/statement"});
                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout_id) {

            Intent intent = new Intent(PartidiView.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public void partidiJSONParse () {
        
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        hasLoggedIn = sp.getBoolean("hasLoggedIn", false);
        String extras = null;
        if(hasLoggedIn) {
            extras = sp.getString("finalJson", null);
        }

            partidaNomer = (TextView) findViewById(R.id.partidaNomer);
            partidaBalance = (TextView) findViewById(R.id.partidaBalance);
            partidaProperyRefs = (TextView) findViewById(R.id.partidaPropertyRefs);

            try {
                JSONObject parentObject = new JSONObject(extras);
                JSONObject accountObject = parentObject.getJSONObject("cssc:AccountsWithUser");
                JSONArray parentArray = accountObject.getJSONArray("cssc:Account");
                partidi = new String [parentArray.length()];

                for (int i = 0; i < parentArray.length(); i++) {

                    JSONObject dolar;
                    JSONArray dolarArray;
                    PartidiInfo info = new PartidiInfo();


                    JSONObject partidaNomerObject = parentArray.getJSONObject(i);
                    dolarArray = partidaNomerObject.getJSONArray("cssc:Uid");

                    for (int j = 0; j < dolarArray.length(); j++) {
                        dolar = dolarArray.getJSONObject(j);
                        info.setPartidaNomer(dolar.getString("$"));
                        partidaNomerJson = dolar.getString("$");
                        partidi[i] = partidaNomerJson;
                        //partidaNomer.setText(info.getPartidaNomer());
                        Log.d("NomerPartida", dolar.getString("$"));
                    }


                    JSONObject partidaBalanceObject = parentArray.getJSONObject(i);
                    dolar = partidaBalanceObject.getJSONObject("ct:Balance");
                    info.setPartidaBalance(dolar.getString("$"));
                    // partidaBalance.setText(info.getPartidaBalance());
                    Log.d("Balance", dolar.getString("$"));

                /*
                JSONObject partidaPropertyRefsObject = parentArray.getJSONObject(i);
                JSONArray refArray = partidaPropertyRefsObject.getJSONArray("cssc:PropertyRefs");
                dolar = refArray.getJSONObject(i);
                JSONObject refObject = dolar.getJSONObject("ct:Ref");
                JSONObject uIDObject = refObject.getJSONObject("ct:Uid");
                info.setPartidaPropertyRefs(uIDObject.getString("$"));
                partidaProperyRefs.setText(info.getPartidaPropertyRefs());
                Log.d("Refs:", dolar.getString("$"));
                */


                    arrayOfDocuments.add(info);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

class DocumentsTask extends AsyncTask<String, String, ArrayList<Documents>> {
    private Context context;
    public String finalJsonDocuments;
    public DocumentsTask (Context context) {
        this.context = context.getApplicationContext();

    }

    String unm, pass;
    @Override
    protected ArrayList<Documents> doInBackground(String... urls) {

        SharedPreferences sp1 = context.getSharedPreferences("Login", Context.MODE_PRIVATE);

         unm = sp1.getString("username", null);
         pass = sp1.getString("password", null);

        String credentials = unm + ":" + pass;


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
                int statusCode = statusLine.getStatusCode();

                if(statusCode == 200) {
                    InputStream content = execute.getEntity().getContent();

                    String status = execute.getStatusLine().toString();
                    Log.d("Status", status);

                    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(content));
                    StringBuffer buffer = new StringBuffer();


                    while ((s = bufferReader.readLine()) != null) {
                        buffer.append(s);
                        Log.d("Response", String.valueOf(builder));
                    }

                    finalJsonDocuments = buffer.toString();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Documents> documents) {

        Intent intent = new Intent(this.context, ClientDocuments.class);
        intent.putExtra("finalPartidiJson", finalJsonDocuments);
        intent.putExtra("username", unm);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        super.onPostExecute(documents);
    }
}
