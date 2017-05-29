package com.example.darkoandreev.webservicetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.darkoandreev.webservicetest.DocumentsModel.Documents;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class Property extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<HashMap<String, String>> mAndroidMapList = new ArrayList<>();
    private ListView listView;
    private String issueDate;
    private String dueDate;
    private String documentNumber;
    private String amount;
    private String vat;
    private String totalAmount;
    private String statusType;
    private String balance;
    private String totalDiscount;
    private String forwardBalance;

    private List<Documents> documentsList = new ArrayList<>();

    public List<Documents> getAndroid() {
        return documentsList;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property);
        listView = (ListView) findViewById(R.id.documentsList);
        listView.setOnItemClickListener(this);
        onLoaded();
    }

    @Override
    protected void onStart() {
        onLoaded();
        super.onStart();
    }

    protected void onLoaded() {
        documentsList = new ArrayList<>();
        for (Documents docs : documentsList) {

            HashMap<String, String> map = new HashMap<>();

            map.put(issueDate, docs.getIssueDate());
            map.put(dueDate, docs.getDueDate());
            map.put(documentNumber, docs.getDocumentNumber());
            map.put(statusType, docs.getStatusType());
            map.put(amount, docs.getAmount());
            map.put(vat, docs.getVat());
            map.put(totalAmount, docs.getTotalAmount());
            map.put(balance, docs.getBalance());
            map.put(totalDiscount, docs.getTotalDiscount());
            map.put(forwardBalance, docs.getForwardBalance());

            mAndroidMapList.add(map);
        }

        loadListView();
    }


    public void documentJSONParse (String buffer) {
            Documents doc = new Documents();
            String finalJson = buffer.toString();
        try {
            JSONObject parentObject = new JSONObject(finalJson);
            JSONObject accountObject = parentObject.getJSONObject("cssc:AccountStatement");
            JSONArray parentArray = accountObject.getJSONArray("cssc:Documents");

            documentsList = new ArrayList<>();


            JSONObject dolar;
            for (int i = 1; i < parentArray.length(); i++) {
                JSONObject issueDate = parentArray.getJSONObject(i);
                dolar = issueDate.getJSONObject("ft:IssueDate");
                doc.setIssueDate(dolar.getString("$"));
                Log.d("IssueDate", dolar.getString("$"));

                JSONObject dueDate = parentArray.getJSONObject(i);
                dolar = dueDate.getJSONObject("ft:DueDate");
                doc.setDueDate(dolar.getString("$"));
                Log.d("dueDate", dolar.getString("$"));

                JSONObject amount = parentArray.getJSONObject(i);
                dolar = amount.getJSONObject("ft:Amount");
                doc.setAmount(dolar.getString("$"));
                Log.d("amount", dolar.getString("$"));

                JSONObject documentNumber = parentArray.getJSONObject(i);
                dolar = documentNumber.getJSONObject("ft:DocumentNumber");
                doc.setDocumentNumber(dolar.getString("$"));
                Log.d("documentNumber", dolar.getString("$"));

                JSONObject statusType = parentArray.getJSONObject(i);
                dolar = statusType.getJSONObject("ft:Applied");
                doc.setDocumentNumber(dolar.getString("$"));
                Log.d("statusType", dolar.getString("$"));

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
                Log.d("balance", dolar.getString("$"));

                JSONObject totalDiscount = parentArray.getJSONObject(i);
                dolar = totalDiscount.getJSONObject("ft:TotalDiscount");
                doc.setTotalDiscount(dolar.getString("$"));
                Log.d("totalDiscount", dolar.getString("$"));


                JSONObject forwardBalance = parentArray.getJSONObject(i);
                dolar = forwardBalance.getJSONObject("ft:Type");
                doc.setForwardBalance(dolar.getString("$"));
                Log.d("forwardBalance", dolar.getString("$"));

                documentsList.add(doc);

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, mAndroidMapList.get(position).get(issueDate),Toast.LENGTH_LONG).show();

    }


    private void loadListView() {

        ListAdapter adapter = new SimpleAdapter(Property.this, mAndroidMapList, R.layout.property_list_items,
                new String[] { issueDate, dueDate, documentNumber, amount, vat, totalAmount, statusType, balance, totalDiscount, forwardBalance },
                new int[] { R.id.issueDate, R.id.dueDate, R.id.documentNumber, R.id.amount, R.id.vat, R.id.totalAmount, R.id.balance, R.id.totalDiscount, R.id.forwardBalance});

        listView.setAdapter(adapter);
    }


}

