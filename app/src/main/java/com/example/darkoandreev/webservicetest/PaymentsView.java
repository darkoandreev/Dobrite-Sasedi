package com.example.darkoandreev.webservicetest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by darko.andreev on 6/22/2017.
 */

public class PaymentsView extends AppCompatActivity {

    private TextView partidaText, titulqrText, plashtaneText, dateText, sumaText, metodText, referenciqText, neusvoeniText, neusvoeniKreditiText, idAndSum, kreditID, platenoID;
    private String partidaNomerJson;
    private ListView paymentsListView;

    ArrayList<PaymentsInfo> arrayOfPayments = new ArrayList<PaymentsInfo>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payments);

        paymentsListView = (ListView) findViewById(R.id.invoicesList);

        MyPaymentsAdapter paymentsAdapter = new MyPaymentsAdapter(this, arrayOfPayments);
        paymentsListView.setAdapter(paymentsAdapter);

        paymentsJSONParse();

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

            PaymentsInfo info = new PaymentsInfo();

            for(int i = 0; i < uID.length()-1; i++) {
                dolar = uID.getJSONObject(i);
                info.setPartida(dolar.getString("$"));
                partidaText.setText(info.getPartida());
                Log.d("PartidaNomer", dolar.getString("$"));
            }

            JSONArray paymentsArray = accountWithUserObject.getJSONArray("cssc:Payment");

            for (int j = 0; j < paymentsArray.length(); j++) {

                JSONObject amount = paymentsArray.getJSONObject(j);
                dolar = amount.getJSONObject("ft:Amount");
                info.setSuma(dolar.getString("$"));
                sumaText.setText(info.getSuma());
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
                neusvoeniKreditiText.setText(info.getNeusvoeniKrediti());
                Log.d("Neusvoeni Krediti", dolar.getString("$"));

                JSONObject unusedPayments = paymentsArray.getJSONObject(j);
                dolar = unusedPayments.getJSONObject("ft:UnusedPayment");
                info.setNeusvoeni(dolar.getString("$"));
                neusvoeniText.setText(info.getNeusvoeni());
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

