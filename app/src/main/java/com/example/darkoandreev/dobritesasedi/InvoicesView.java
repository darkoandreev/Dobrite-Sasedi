package com.example.darkoandreev.dobritesasedi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 6/27/2017.
 */

public class InvoicesView extends AppCompatActivity {

    private ListView invoicesListView;
    private TextView partidaInvoicesText, titulqrInvoiceText, dokumentInvoiceText, izdadenaInvoiceText, statusInvoiceText, stoinostInvoiceText, ddsInvoiceText, totalInvoiceText, platimaInvoiceText;



    ArrayList<InvoicesInfo> arrayOfInvoices = new ArrayList<InvoicesInfo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoices);

        Toolbar toolbar = (Toolbar) findViewById(R.id.invoices_toolbar);
        setSupportActionBar(toolbar);

        invoicesListView = (ListView) findViewById(R.id.invoicesList);

        MyInvoicesAdapter invoicesAdapter = new MyInvoicesAdapter(this, arrayOfInvoices);
        invoicesListView.setAdapter(invoicesAdapter);

        invoicesJSONParse();
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
            Intent intent = new Intent(InvoicesView.this, Login.class);
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

    public void invoicesJSONParse () {
        partidaInvoicesText = (TextView) findViewById(R.id.partidaIdInvoiceText);
        titulqrInvoiceText = (TextView) findViewById(R.id.titulqrInvoiceText);
        dokumentInvoiceText = (TextView) findViewById(R.id.dokumentInvoiceText);
        izdadenaInvoiceText = (TextView) findViewById(R.id.izdadenaInvoiceText);
        statusInvoiceText = (TextView) findViewById(R.id.statusInvoiceText);
        stoinostInvoiceText = (TextView) findViewById(R.id.stoinostInvoiceText);
        ddsInvoiceText = (TextView) findViewById(R.id.ddsInvoiceText);
        totalInvoiceText = (TextView) findViewById(R.id.totalInvoiceText);
        platimaInvoiceText = (TextView) findViewById(R.id.platimaInvoiceText);

        JSONObject dolar;


        String extras = getIntent().getStringExtra("finalInvoicesJson");

        try {
            JSONObject parentObject = new JSONObject(extras);
            JSONObject accountWithUserObject = parentObject.getJSONObject("cssc:AccountWithInvoices");
            JSONObject accountObject = accountWithUserObject.getJSONObject("cssc:Account");
            JSONArray uID = accountObject.getJSONArray("cssc:Uid");

            JSONObject holderAccount = accountObject.getJSONObject("cssc:Holder");
            String holderString = holderAccount.getString("$");
            titulqrInvoiceText.setText(holderString);

            InvoicesInfo info = new InvoicesInfo();

            for(int i = 0; i < uID.length()-1; i++) {
                dolar = uID.getJSONObject(i);
                if(dolar.has("@ct:default")) {
                    info.setPartidaInvoices(dolar.getString("$"));
                    partidaInvoicesText.setText(info.getPartidaInvoices());
                }
                Log.d("PartidaInvoices", dolar.getString("$"));
            }

            JSONArray invoicesArray = accountWithUserObject.getJSONArray("cssc:Invoice");

            for (int j = 0; j < invoicesArray.length(); j++) {

                JSONObject issueDate = invoicesArray.getJSONObject(j);
                dolar = issueDate.getJSONObject("ft:IssueDate");
                info.setIzdadenaInvoices(dolar.getString("$"));
                izdadenaInvoiceText.setText(info.getIzdadenaInvoices());
                Log.d("Izdadena", dolar.getString("$"));

                JSONObject dueDate = invoicesArray.getJSONObject(j);
                dolar = dueDate.getJSONObject("ft:DueDate");
                info.setPlatimaInvoices(dolar.getString("$"));
                platimaInvoiceText.setText(info.getPlatimaInvoices());
                Log.d("Platima do", dolar.getString("$"));

                JSONObject invoiceNumber = invoicesArray.getJSONObject(j);
                dolar = invoiceNumber.getJSONObject("ft:InvoiceNumber");
                info.setDokumentInvoices(dolar.getString("$"));
                dokumentInvoiceText.setText(info.getDokumentInvoices());
                Log.d("Dokument", dolar.getString("$"));

                JSONObject applied = invoicesArray.getJSONObject(j);
                dolar = applied.getJSONObject("ft:Applied");
                info.setStatusInvoices(dolar.getString("$"));
                statusInvoiceText.setText(info.getStatusInvoices());
                Log.d("Status", dolar.getString("$"));

                JSONObject amountInvoices = invoicesArray.getJSONObject(j);
                dolar = amountInvoices.getJSONObject("ft:Amount");
                info.setStoinostInvoices(dolar.getString("$"));
                stoinostInvoiceText.setText(info.getStoinostInvoices() + "лв.");
                Log.d("Stoinost", dolar.getString("$"));

                JSONObject VAT = invoicesArray.getJSONObject(j);
                dolar = VAT.getJSONObject("ft:VAT");
                info.setDdsInvoices(dolar.getString("$"));
                ddsInvoiceText.setText(info.getDdsInvoices() + "лв.");
                Log.d("DDS", dolar.getString("$"));

                JSONObject totalAmount = invoicesArray.getJSONObject(j);
                dolar = totalAmount.getJSONObject("ft:TotalAmount");
                info.setTotalInvoices(dolar.getString("$"));
                totalInvoiceText.setText(info.getTotalInvoices() + "лв.");
                Log.d("Total", dolar.getString("$"));


                JSONObject invoicesArrayObject = invoicesArray.getJSONObject(j);
                JSONArray lineArray = invoicesArrayObject.getJSONArray("ft:Line");
                for (int k = 0; k < lineArray.length(); k++) {
                    info = new InvoicesInfo();

                    JSONObject nameInvoices = lineArray.getJSONObject(k);
                    dolar = nameInvoices.getJSONObject("ft:Name");
                    info.setObshtaOhranaInvoices(dolar.getString("$"));
                    Log.d("Name", dolar.getString("$"));

                    JSONObject descriptionInvoices = lineArray.getJSONObject(k);
                    dolar = descriptionInvoices.getJSONObject("ft:Description");
                    info.setObshtaOhranaInfo(dolar.getString("$"));
                    Log.d("Description", dolar.getString("$"));

                    JSONObject quantityInvoices = lineArray.getJSONObject(k);
                    dolar = quantityInvoices.getJSONObject("ft:Quantity");
                    info.setKolichestvoInvoices(dolar.getString("$"));
                    Log.d("Kolichestvo", dolar.getString("$"));


                    JSONObject priceInvoices = lineArray.getJSONObject(k);
                    dolar = priceInvoices.getJSONObject("ft:Price");
                    info.setTotalListInvoices("0");
                    Log.d("TotalList", dolar.getString("$"));


             //       JSONObject ddsInvoices = lineArray.getJSONObject(k);
             //       dolar = ddsInvoices.getJSONObject("ft:VAT");


                    JSONObject amount = lineArray.getJSONObject(k);
                    dolar = amount.getJSONObject("ft:Amount");
                    info.setTotalListInvoices(dolar.getString("$"));
                    Log.d("Price", dolar.getString("$"));

                    arrayOfInvoices.add(info);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
