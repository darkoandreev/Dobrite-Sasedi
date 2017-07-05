package com.example.darkoandreev.webservicetest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import static com.example.darkoandreev.webservicetest.R.layout.client_documents;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class ClientDocuments extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private String [] type;
    private String [] documentArray;
    private double [] testArray;
    private String partidaNum;
    public Documents doc;
    private String fromDateString;
    private String toDateString;
    private String [] issueDateArray;


    TextView tekushtoSaldo, nachalnaData, krainaData, saldoNachalo, saldoKraq, textView12, userIDText, textView11;

    private List<Documents> documentsList = new ArrayList<>();

    public List<Documents> getAndroid() {
        return documentsList;
    }
    ArrayList<Documents> arrayOfDocuments = new ArrayList<Documents>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(client_documents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.documents_toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.documentsList);
        MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, arrayOfDocuments);
        listView.setAdapter(adapter);

        documentJSONParse();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentsTask task = new PaymentsTask(ClientDocuments.this);
                InvoicesTask invoiceTask = new InvoicesTask(ClientDocuments.this);
                if (type[position].equals("Payment")) {
                    task.execute(new String[]{"http://vrod.dobritesasedi.bg/rest/accounts/" + partidaNum + "/payments/" + documentArray[position]});
                } else {
                    invoiceTask.execute(new String[]{"http://vrod.dobritesasedi.bg//rest/accounts/" + partidaNum + "/invoices/" + documentArray[position]});
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logout_id) {
            Intent intent = new Intent(ClientDocuments.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == R.id.calendar_icon) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
            final EditText fromDate = (EditText) mView.findViewById(R.id.fromDateEdt);
            final EditText toDate = (EditText) mView.findViewById(R.id.toDateEdt);

            builder.setView(mView);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    fromDateString = fromDate.getText().toString();
                    toDateString = toDate.getText().toString();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void documentJSONParse () {

        tekushtoSaldo = (TextView) findViewById(R.id.tekushtoSaldo);
        nachalnaData = (TextView) findViewById(R.id.nachalnaData);
        krainaData = (TextView) findViewById(R.id.krainaData);
        saldoNachalo = (TextView) findViewById(R.id.saldoNachalo);
        saldoKraq = (TextView) findViewById(R.id.saldoVKraq);
        textView12 = (TextView) findViewById(R.id.grupa);
        userIDText = (TextView) findViewById(R.id.userID);
        textView11 = (TextView) findViewById(R.id.textView11);


        JSONObject dolar;

        String extras = getIntent().getStringExtra("finalPartidiJson");
        String user = getIntent().getStringExtra("username");
        userIDText.setText(user);

        Documents documents = new Documents();
        String partidaId = null;

        try {
            JSONObject parentObject = new JSONObject(extras);
            JSONObject accountObject = parentObject.getJSONObject("cssc:AccountStatement");

            dolar = accountObject.getJSONObject("ct:StartDate");
            documents.setNachalnaData(dolar.getString("$"));
            nachalnaData.setText(dolar.getString("$"));

            dolar = accountObject.getJSONObject("ct:EndDate");
            documents.setKrainaData(dolar.getString("$"));
            krainaData.setText(dolar.getString("$"));

            JSONArray parentArray = accountObject.getJSONArray("cssc:Documents");
            JSONObject Accounts = accountObject.getJSONObject("cssc:Account");


            JSONArray partidaID = Accounts.getJSONArray("cssc:Uid");

            for (int k = 0; k < partidaID.length(); k++) {
                dolar = partidaID.getJSONObject(k);
                documents.setPartidaID(dolar.getString("$"));
                partidaId = dolar.getString("$");
                partidaNum = partidaId;
                Log.d("partidaID", dolar.getString("$"));

            }

            type = new String [parentArray.length()];
            documentArray = new String [parentArray.length()];
            testArray = new double [parentArray.length()];
            issueDateArray = new String [parentArray.length()];

            for (int i = 0; i < parentArray.length(); i++) {
                doc = new Documents();


                JSONObject issueDate = parentArray.getJSONObject(i);
                dolar = issueDate.getJSONObject("ft:IssueDate");
                doc.setIssueDate(dolar.getString("$"));
                issueDateArray[i] = doc.getIssueDate().toString();

                // nachalnaData.setText(doc.getIssueDate());
                Log.d("IssueDate", dolar.getString("$"));

                JSONObject dueDate = parentArray.getJSONObject(i);
                dolar = dueDate.getJSONObject("ft:DueDate");
                doc.setDueDate(dolar.getString("$"));
                // krainaData.setText(doc.getDueDate());
                Log.d("dueDate", dolar.getString("$"));

                JSONObject documentNumber = parentArray.getJSONObject(i);
                dolar = documentNumber.getJSONObject("ft:DocumentNumber");
                doc.setDocumentNumber(dolar.getString("$"));
                String documentNum = dolar.getString("$");
                Log.d("documentNumber", dolar.getString("$"));

                JSONObject amount = parentArray.getJSONObject(i);
                dolar = amount.getJSONObject("ft:Amount");
                doc.setAmount(dolar.getString("$"));
                //saldoNachalo.setText(doc.getAmount());

                Log.d("amount", dolar.getString("$"));


                JSONObject statusType = parentArray.getJSONObject(i);
                if(!statusType.has("ft:Applied")) {
                    doc.setStatusType("No status");
                } else {
                    dolar = statusType.getJSONObject("ft:Applied");
                    doc.setStatusType(dolar.getString("$"));
                    Log.d("statusType", dolar.getString("$"));
                }

                JSONObject vat = parentArray.getJSONObject(i);
                dolar = vat.getJSONObject("ft:VAT");
                doc.setVat(dolar.getString("$"));
                Log.d("vat", dolar.getString("$"));

                JSONObject totalAmount = parentArray.getJSONObject(i);
                dolar = totalAmount.getJSONObject("ft:TotalAmount");
                doc.setTotalAmount(dolar.getString("$"));
                Log.d("totalAmount", dolar.getString("$"));

                JSONObject balance = parentArray.getJSONObject(i);
                dolar = balance.getJSONObject("ct:Balance");
                doc.setBalance(dolar.getString("$"));
                testArray[i] = Double.parseDouble(dolar.getString("$"));
                tekushtoSaldo.setText(doc.getBalance());
                Log.d("balance", dolar.getString("$"));
                Log.d("Double Balance", String.valueOf(testArray[i]));

                JSONObject totalDiscount = parentArray.getJSONObject(i);
                dolar = totalDiscount.getJSONObject("ft:TotalDiscount");
                doc.setTotalDiscount(dolar.getString("$"));
                Log.d("totalDiscount", dolar.getString("$"));


                JSONObject forwardBalance = parentArray.getJSONObject(i);
                dolar = forwardBalance.getJSONObject("ft:Type");
                doc.setForwardBalance(dolar.getString("$"));
                String test = dolar.getString("$");
                Log.d("forwardBalance", dolar.getString("$"));


                if(test.equals("ForwardBalance")) {
                    saldoNachalo.setText(doc.getBalance());
                    textView11.setText(doc.getBalance());
                }
                double maxBalance = Double.MIN_VALUE;

                for (int counter = 1; counter < testArray.length; counter++)
                {
                    if (testArray[counter] > maxBalance)
                    {
                        maxBalance = testArray[counter];
                    }
                }
                saldoKraq.setText(String.valueOf(maxBalance));



                type [i] = dolar.getString("$");
                documentArray[i] = documentNum;
                arrayOfDocuments.add(doc);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, arrayOfDocuments.get(position).toString(), Toast.LENGTH_LONG).show();

    }
}

class PaymentsTask extends AsyncTask<String, String, ArrayList<PaymentsInfo>> {
    private Context context;
    public String finalJsonPayments;
    public PaymentsTask (Context context) {
        this.context = context.getApplicationContext();

    }

    String unm, pass;
    @Override
    protected ArrayList<PaymentsInfo> doInBackground(String... urls) {

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

                    finalJsonPayments = buffer.toString();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<PaymentsInfo> payments) {

        Intent intent = new Intent(this.context, PaymentsView.class);
        intent.putExtra("finalPaymentsJson", finalJsonPayments);
        intent.putExtra("username", unm);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);

        super.onPostExecute(payments);
    }
}

class InvoicesTask extends AsyncTask<String, String, ArrayList<InvoicesInfo>> {
    private Context context;
    public String finalJsonInvoices;
    public InvoicesTask (Context context) {
        this.context = context.getApplicationContext();

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

                    finalJsonInvoices = buffer.toString();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<InvoicesInfo> invoices) {

        Intent intent = new Intent(this.context, InvoicesView.class);
        intent.putExtra("finalInvoicesJson", finalJsonInvoices);
        intent.putExtra("username", unm);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        super.onPostExecute(invoices);
    }
}








