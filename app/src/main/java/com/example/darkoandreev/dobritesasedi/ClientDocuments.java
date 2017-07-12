package com.example.darkoandreev.dobritesasedi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darkoandreev.dobritesasedi.DocumentsModel.Documents;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.darkoandreev.dobritesasedi.R.layout.client_documents;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class ClientDocuments extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private String[] type;
    private String[] documentArray;
    private double[] testArray;
    private String partidaNum;
    public Documents doc;
    private String fromDateString;
    private String toDateString;
    private String[] issueDateArray;
    private int fromYear, fromMonth, fromDay, toYear, toMonth, toDay;
    private DatePickerDialog to_date, from_date;

    // private DatePickerDialog.OnDateSetListener from_date, to_date;


    TextView tekushtoSaldo, nachalnaData, krainaData, saldoNachalo, saldoKraq, textView12, userIDText, textView11, statusTypeForward;

    private List<Documents> documentsList = new ArrayList<>();
    private Documents documents = new Documents();

    public List<Documents> getAndroid() {
        return documentsList;
    }

    ArrayList<Documents> arrayOfDocuments = new ArrayList<Documents>();
    final ArrayList<Documents> filteredDates = new ArrayList<Documents>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(client_documents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.documents_toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.documentsList);
        MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, arrayOfDocuments);
        listView.setHeaderDividersEnabled(true);
        listView.setAdapter(adapter);


        documentJSONParse();

        clickItemHandler(listView, arrayOfDocuments);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setContentView(client_documents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.documents_toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.documentsList);

        if (filteredDates.isEmpty()) {
            orienationJSONParse();
            MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, arrayOfDocuments);
            listView.setAdapter(adapter);
            clickItemHandler(listView, arrayOfDocuments);
        } else {
            orienationJSONParse();
            MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, filteredDates);
            listView.setAdapter(adapter);
            clickItemHandler(listView, filteredDates);
        }

    }

    public static boolean isValidDate(String inDate) throws java.text.ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        inflater.inflate(R.menu.logout_menu, menu);
        inflater.inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.refresh_id) {
            MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, arrayOfDocuments);
            listView.setAdapter(adapter);

            clickItemHandler(listView, arrayOfDocuments);
        }

        if (item.getItemId() == R.id.logout_id) {
            Intent intent = new Intent(ClientDocuments.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            finish();
        }


        Calendar c = Calendar.getInstance();
        fromYear = c.get(Calendar.YEAR);
        fromMonth = c.get(Calendar.MONTH);
        fromDay = c.get(Calendar.DAY_OF_MONTH);
        toYear = c.get(Calendar.YEAR);
        toMonth = c.get(Calendar.MONTH);
        toDay = c.get(Calendar.DAY_OF_MONTH);

        if (item.getItemId() == R.id.calendar_icon) {

           to_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    toYear = year;
                    toMonth = month + 1;
                    toDay = dayOfMonth;

                    if (toDay < 10) {
                        fromDateString = toYear + "-" + toMonth + "-" + "0" + toDay;
                        Log.d("toDay < 10", fromDateString);
                    }

                    if (toMonth < 10) {
                        fromDateString = toYear + "-" + "0" + toMonth + "-" + toDay;
                        Log.d("toMonth < 10", fromDateString);
                    }

                    if (toMonth < 10 && toDay < 10) {
                        fromDateString = toYear + "-" + "0" + toMonth + "-" + "0" + toDay;
                        Log.d("toMonth && toDay < 10", fromDateString);
                    }

                    if(toMonth > 10 && toDay > 10) {
                        fromDateString = toYear + "-" + toMonth + "-" + toDay;
                    }


                    Log.d("From date: ", fromDateString);

                }
            }, toYear, toMonth, toDay);

                from_date = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        fromYear = year;
                        fromMonth = month + 1;
                        fromDay = dayOfMonth;

                        if (fromDay < 10) {
                            toDateString = fromYear + "-" + fromMonth + "-" + "0" + fromDay;
                            Log.d("fromDay < 10", toDateString);
                        } if (fromMonth < 10) {
                            toDateString = fromYear + "-" + "0" + fromMonth + "-" + fromDay;
                            Log.d("fromMonth < 10", toDateString);
                        } if (fromDay < 10 && fromMonth < 10) {
                            toDateString = fromYear + "-" + "0" + fromMonth + "-" + "0" + fromDay;
                            Log.d("fromDay && fromMonth<10", toDateString);
                        }

                        if(toMonth > 10 && toDay > 10) {
                            toDateString = fromYear + "-" + fromMonth + "-" + fromDay;
                        }

                        Log.d("To date: ", toDateString);


                        try {

                            if (!(fromDateString.compareTo(toDateString) <= 0)) {
                                Toast.makeText(ClientDocuments.this, "Въведете коректен период", Toast.LENGTH_SHORT).show();
                            } else {
                                ArrayList<String> issueArrayList = new ArrayList<>(Arrays.asList(issueDateArray));
                                int start = issueArrayList.indexOf(fromDateString);
                                int end = issueArrayList.lastIndexOf(toDateString);

                                for (int j = 0; j < issueDateArray.length; j++) {
                                    if (fromDateString.compareTo(issueDateArray[j]) <= 0) {
                                        start = j;
                                        break;
                                    }
                                }

                                for (int k = 0; k < issueDateArray.length; k++) {
                                    if (toDateString.compareTo(issueDateArray[k]) >= 0) {
                                        end = k;
                                    }
                                }

                                for (int i = start; i <= end; i++) {
                                    filteredDates.add(arrayOfDocuments.get(i));
                                }

                                MyDocumentsAdapter adapter = new MyDocumentsAdapter(ClientDocuments.this, filteredDates);
                                listView.setAdapter(adapter);

                                clickItemHandler(listView, filteredDates);
                            }

                        } catch (Exception e) {
                            Toast.makeText(ClientDocuments.this, "Не фигурират такива дати", Toast.LENGTH_LONG).show();
                        }
                    }
                }, fromYear, fromMonth, fromDay);

            from_date.setMessage("Изберете крайна дата");
            to_date.setMessage("Изберете начална дата");
            from_date.show();
            to_date.show();


        }

        return super.onOptionsItemSelected(item);
    }

    public void clickItemHandler (ListView listView, final ArrayList<Documents> list) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PaymentsTask task = new PaymentsTask(ClientDocuments.this);
                InvoicesTask invoiceTask = new InvoicesTask(ClientDocuments.this);
                if (list.get(position).getForwardBalance().equals("Payment")) {
                    task.execute(new String[]{"http://vrod.dobritesasedi.bg/rest/accounts/" + documents.getPartidaID() + "/payments/" + list.get(position).getDocumentNumber()});
                } else {
                    invoiceTask.execute(new String[]{"http://vrod.dobritesasedi.bg//rest/accounts/" + documents.getPartidaID() + "/invoices/" + list.get(position).getDocumentNumber()});
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void orienationJSONParse () {

        tekushtoSaldo = (TextView) findViewById(R.id.tekushtoSaldo);
        nachalnaData = (TextView) findViewById(R.id.nachalnaData);
        krainaData = (TextView) findViewById(R.id.krainaData);
        saldoNachalo = (TextView) findViewById(R.id.saldoNachalo);
        saldoKraq = (TextView) findViewById(R.id.saldoVKraq);
        textView11 = (TextView) findViewById(R.id.textView11);
        textView12.setText("Няма");

        JSONObject dolar;

        String extras = getIntent().getStringExtra("finalPartidiJson");
        String user = getIntent().getStringExtra("username");
        userIDText.setText(user);


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

            userIDText.setText(user);

            for (int k = 0; k < partidaID.length(); k++) {
                dolar = partidaID.getJSONObject(k);
                documents.setPartidaID(dolar.getString("$"));
                partidaId = dolar.getString("$");
                partidaNum = partidaId;
                Log.d("partidaID", dolar.getString("$"));

            }

            for (int i = 0; i < parentArray.length(); i++) {
                doc = new Documents();

                JSONObject balance = parentArray.getJSONObject(i);
                dolar = balance.getJSONObject("ct:Balance");
                doc.setBalance(dolar.getString("$"));
                testArray[i] = Double.parseDouble(dolar.getString("$"));
                Double posledno = testArray[parentArray.length()-1];
                tekushtoSaldo.setText(doc.getBalance());
                Log.d("balance", dolar.getString("$"));
                Log.d("Double Balance", String.valueOf(testArray[i]));

                JSONObject amount = parentArray.getJSONObject(i);
                dolar = amount.getJSONObject("ft:Amount");
                doc.setAmount(dolar.getString("$"));


                JSONObject forwardBalance = parentArray.getJSONObject(i);
                dolar = forwardBalance.getJSONObject("ft:Type");
                doc.setForwardBalance(dolar.getString("$"));
                String test = dolar.getString("$");
                Log.d("forwardBalance", dolar.getString("$"));

                if(test.equals("ForwardBalance")) {
                    saldoNachalo.setText(doc.getBalance());
                    textView11.setText(doc.getBalance());
                }

                saldoKraq.setText(posledno.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        statusTypeForward = (TextView) findViewById(R.id.inovice);

        textView12.setText("Няма");
        JSONObject dolar;

        String extras = getIntent().getStringExtra("finalPartidiJson");
        String user = getIntent().getStringExtra("username");
        userIDText.setText(user);


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
                //saldoNachalo.setText(dolar.getString("$"));

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
                Double posledno = testArray[parentArray.length()-1];
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

                saldoKraq.setText(posledno.toString());



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
    private ProgressDialog progressDialog;

    public PaymentsTask (Context context) {
        this.context = context.getApplicationContext();
        progressDialog = new ProgressDialog(context);

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
    protected void onPreExecute() {
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<PaymentsInfo> payments) {

        progressDialog.dismiss();

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
    private ProgressDialog progressDialog;

    public InvoicesTask (Context context) {
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







