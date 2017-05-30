package com.example.darkoandreev.webservicetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.darkoandreev.webservicetest.DocumentsModel.Documents;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.darkoandreev.webservicetest.R.layout.client_documents;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class Property extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
    ArrayList<Documents> arrayOfDocuments = new ArrayList<Documents>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(client_documents);
        listView = (ListView) findViewById(R.id.documentsList);

        MyAdapter adapter = new MyAdapter(this, arrayOfDocuments);
        listView.setAdapter(adapter);


        documentJSONParse();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    HashMap<String, String> map = new HashMap<>();

    public void documentJSONParse () {

        String extras = getIntent().getStringExtra("finalJson");

        try {
            JSONObject parentObject = new JSONObject(extras);
            JSONObject accountObject = parentObject.getJSONObject("cssc:AccountStatement");
            JSONArray parentArray = accountObject.getJSONArray("cssc:Documents");

            for (int i = 1; i < parentArray.length(); i++) {

                JSONObject dolar;
                Documents doc = new Documents();

                JSONObject issueDate = parentArray.getJSONObject(i);
                dolar = issueDate.getJSONObject("ft:IssueDate");
                doc.setIssueDate(dolar.getString("$"));
                map.put("IssueDate", doc.getIssueDate());
                Log.d("IssueDate", dolar.getString("$"));

                JSONObject dueDate = parentArray.getJSONObject(i);
                dolar = dueDate.getJSONObject("ft:DueDate");
                doc.setDueDate(dolar.getString("$"));
                map.put("DueDate", doc.getDueDate());
                Log.d("dueDate", dolar.getString("$"));

                JSONObject documentNumber = parentArray.getJSONObject(i);
                dolar = documentNumber.getJSONObject("ft:DocumentNumber");
                doc.setDocumentNumber(dolar.getString("$"));
                map.put("DocumentNumber", doc.getDocumentNumber());
                Log.d("documentNumber", dolar.getString("$"));

                JSONObject amount = parentArray.getJSONObject(i);
                dolar = amount.getJSONObject("ft:Amount");
                doc.setAmount(dolar.getString("$"));
                map.put("Amount", doc.getAmount());
                Log.d("amount", dolar.getString("$"));


                JSONObject statusType = parentArray.getJSONObject(i);
                dolar = statusType.getJSONObject("ft:Applied");
                doc.setStatusType(dolar.getString("$"));
                map.put("StatusType", doc.getStatusType());
                Log.d("statusType", dolar.getString("$"));

                JSONObject vat = parentArray.getJSONObject(i);
                dolar = vat.getJSONObject("ft:VAT");
                doc.setVat(dolar.getString("$"));
                map.put("VAT", doc.getVat());
                Log.d("vat", dolar.getString("$"));

                JSONObject totalAmount = parentArray.getJSONObject(i);
                dolar = totalAmount.getJSONObject("ft:TotalAmount");
                doc.setTotalAmount(dolar.getString("$"));
                map.put("TotalAmount", doc.getTotalAmount());
                Log.d("totalAmount", dolar.getString("$"));

                JSONObject balance = parentArray.getJSONObject(i);
                dolar = balance.getJSONObject("ct:Balance");
                doc.setBalance(dolar.getString("$"));
                map.put("Balance", doc.getBalance());
                Log.d("balance", dolar.getString("$"));

                JSONObject totalDiscount = parentArray.getJSONObject(i);
                dolar = totalDiscount.getJSONObject("ft:TotalDiscount");
                doc.setTotalDiscount(dolar.getString("$"));
                map.put("TotalDiscount", doc.getTotalDiscount());
                Log.d("totalDiscount", dolar.getString("$"));


                JSONObject forwardBalance = parentArray.getJSONObject(i);
                dolar = forwardBalance.getJSONObject("ft:Type");
                doc.setForwardBalance(dolar.getString("$"));
                map.put("ForwardBalance", doc.getForwardBalance());
                Log.d("forwardBalance", dolar.getString("$"));

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



