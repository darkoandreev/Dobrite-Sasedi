package com.example.darkoandreev.dobritesasedi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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


/**
 * Created by darko.andreev on 6/22/2017.
 */

public class PaymentsView extends AppCompatActivity {

    private TextView partidaText, titulqrText, plashtaneText, dateText, sumaText, metodText, referenciqText, neusvoeniText, neusvoeniKreditiText, idAndSum, kreditID, platenoID;
    private String partidaNomerJson;
    private ListView paymentsListView;
    private String partidaTask;

    ArrayList<PaymentsInfo> arrayOfPayments = new ArrayList<PaymentsInfo>();
    PaymentsInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.payments_toolbar);
        setSupportActionBar(toolbar);

        paymentsListView = (ListView) findViewById(R.id.invoicesList);

        MyPaymentsAdapter paymentsAdapter = new MyPaymentsAdapter(this, arrayOfPayments);
        paymentsListView.setAdapter(paymentsAdapter);

        paymentsJSONParse();

        clickItemHandler(paymentsListView, arrayOfPayments);

    }

    public void clickItemHandler (ListView listView, final ArrayList<PaymentsInfo> list) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InvoicesTaskFromPayments invoiceTask = new InvoicesTaskFromPayments(PaymentsView.this);
                invoiceTask.execute(new String[]{"http://vrod.dobritesasedi.bg//rest/accounts/" + partidaTask + "/invoices/" + list.get(position).getPaymentID()});

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
            Intent intent = new Intent(PaymentsView.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            SharedPreferences preferences =getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void paymentsJSONParse() {

        partidaText = (TextView) findViewById(R.id.partidiText);
        titulqrText = (TextView) findViewById(R.id.titulqrText);
        plashtaneText = (TextView) findViewById(R.id.plashtaneText);
        dateText = (TextView) findViewById(R.id.dataText);
        sumaText = (TextView) findViewById(R.id.sumaText);
        metodText = (TextView) findViewById(R.id.metodText);
        referenciqText = (TextView) findViewById(R.id.referenciqText);
        neusvoeniText = (TextView) findViewById(R.id.neusvoeniText);
        neusvoeniKreditiText = (TextView) findViewById(R.id.neusvKreditText);
        idAndSum = (TextView) findViewById(R.id.idAndSum);
        kreditID = (TextView) findViewById(R.id.kreditID);
        platenoID = (TextView) findViewById(R.id.platenoID);

        JSONObject dolar;


        String extras = getIntent().getStringExtra("finalPaymentsJson");

        try {
            JSONObject parentObject = new JSONObject(extras);
            JSONObject accountWithUserObject = parentObject.getJSONObject("cssc:AccountWithPayments");
            JSONObject accountObject = accountWithUserObject.getJSONObject("cssc:Account");
            JSONArray uID = accountObject.getJSONArray("cssc:Uid");

            JSONObject holderAccount = accountObject.getJSONObject("cssc:Holder");
            String holderString = holderAccount.getString("$");
            titulqrText.setText(holderString);

           info = new PaymentsInfo();

            for(int i = 0; i < uID.length()-1; i++) {
                dolar = uID.getJSONObject(i);
                if(dolar.has("@ct:default")) {
                    info.setPartida(dolar.getString("$"));
                    partidaText.setText(info.getPartida());
                    partidaTask = info.getPartida();
                    Log.d("PartidaNomer", dolar.getString("$"));
                }
            }

            JSONArray paymentsArray = accountWithUserObject.getJSONArray("cssc:Payment");

            for (int j = 0; j < paymentsArray.length(); j++) {

                JSONObject amount = paymentsArray.getJSONObject(j);
                dolar = amount.getJSONObject("ft:Amount");
                info.setSuma(dolar.getString("$"));
                sumaText.setText(info.getSuma() + "лв.");
                Log.d("Suma", dolar.getString("$"));

                JSONObject paymentMethod = paymentsArray.getJSONObject(j);
                dolar = paymentMethod.getJSONObject("ft:PaymentMethod");
                info.setMetod(dolar.getString("$"));
                metodText.setText(info.getMetod());
                Log.d("Metod", dolar.getString("$"));

               // JSONObject reference = paymentsArray.getJSONObject(j);
              //  dolar = reference.getJSONObject("ft:Reference");

              //  JSONObject memo = paymentsArray.getJSONObject(j);
              //  dolar = memo.getJSONObject("ft:Memo");

                JSONObject date = paymentsArray.getJSONObject(j);
                dolar = date.getJSONObject("ft:Date");
                info.setDate(dolar.getString("$"));
                dateText.setText(info.getDate());
                Log.d("Date", dolar.getString("$"));

                JSONObject paymentNumber = paymentsArray.getJSONObject(j);
                dolar = paymentNumber.getJSONObject("ft:PaymentNumber");
                info.setPlashtane(dolar.getString("$"));
                plashtaneText.setText(info.getPlashtane());
                Log.d("Plashtane Nomer", dolar.getString("$"));

                JSONObject unusedCredits = paymentsArray.getJSONObject(j);
                dolar = unusedCredits.getJSONObject("ft:UnusedCredits");
                info.setNeusvoeniKrediti(dolar.getString("$"));
                neusvoeniKreditiText.setText(info.getNeusvoeniKrediti() + "лв.");
                Log.d("Neusvoeni Krediti", dolar.getString("$"));

                JSONObject unusedPayments = paymentsArray.getJSONObject(j);
                dolar = unusedPayments.getJSONObject("ft:UnusedPayment");
                info.setNeusvoeni(dolar.getString("$"));
                neusvoeniText.setText(info.getNeusvoeni() + "лв.");
                Log.d("Neusvoeni", dolar.getString("$"));


                JSONObject paymentsArrayObject = paymentsArray.getJSONObject(j);
                JSONArray lineArray = paymentsArrayObject.getJSONArray("ft:Line");
                for (int k = 0; k < lineArray.length(); k++) {
                    info = new PaymentsInfo();

                    JSONObject invoiceNumber = lineArray.getJSONObject(k);
                    dolar = invoiceNumber.getJSONObject("ft:InvoiceNumber");
                    info.setPaymentID(dolar.getString("$"));
                    Log.d("Payment ID", dolar.getString("$"));

                    JSONObject invoiceAmount = lineArray.getJSONObject(k);
                    dolar = invoiceAmount.getJSONObject("ft:InvoiceAmount");
                    info.setPlatenoID(dolar.getString("$"));
                    Log.d("Plateno ID", dolar.getString("$"));

                    JSONObject appliedCredits = lineArray.getJSONObject(k);
                    dolar = appliedCredits.getJSONObject("ft:AppliedCredits");
                    info.setKreditID(dolar.getString("$"));
                    Log.d("Kredit", dolar.getString("$"));

                    JSONObject paymentAmount = lineArray.getJSONObject(k);
                    dolar = paymentAmount.getJSONObject("ft:Payment");
                    info.setSumaID(dolar.getString("$"));
                    Log.d("Suma ID", dolar.getString("$"));

                    arrayOfPayments.add(info);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class InvoicesTaskFromPayments extends AsyncTask<String, String, ArrayList<InvoicesInfo>> {
    private Context context;
    public String finalJsonInvoices;
    private ProgressDialog progressDialog;

    public InvoicesTaskFromPayments(Context context) {
        this.context = context.getApplicationContext();
        progressDialog = new ProgressDialog(context);

    }

    String unm, pass;

    @Override
    protected ArrayList<InvoicesInfo> doInBackground(String... urls) {

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

                    finalJsonInvoices = buffer.toString();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<InvoicesInfo> invoices) {


        progressDialog.dismiss();

        Intent intent = new Intent(this.context, InvoicesView.class);
        intent.putExtra("finalInvoicesJson", finalJsonInvoices);
        intent.putExtra("username", unm);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


        super.onPostExecute(invoices);
    }
}

