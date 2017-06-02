package com.example.darkoandreev.webservicetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.darkoandreev.webservicetest.DocumentsModel.Documents;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.darkoandreev.webservicetest.R.layout.client_documents;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class ClientDocuments extends AppCompatActivity implements AdapterView.OnItemClickListener {

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

    TextView tekushtoSaldo, nachalnaData, krainaData, saldoNachalo, saldoKraq, textView12, userIDText;

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

        MyDocumentsAdapter adapter = new MyDocumentsAdapter(this, arrayOfDocuments);
        listView.setAdapter(adapter);

        documentJSONParse();
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
        textView12 = (TextView) findViewById(R.id.textView12);
        userIDText = (TextView) findViewById(R.id.userID);

        JSONObject dolar;

        String extras = getIntent().getStringExtra("finalPartidiJson");
        String userID = getIntent().getStringExtra("userID");
        userIDText.setText(userID);

        Documents documents = new Documents();
        String partidaId = null;
        try {
            JSONObject parentObject = new JSONObject(extras);
            JSONObject accountObject = parentObject.getJSONObject("cssc:AccountStatement");
            JSONArray parentArray = accountObject.getJSONArray("cssc:Documents");

            JSONObject Accounts = accountObject.getJSONObject("cssc:Account");
            JSONArray partidaID = Accounts.getJSONArray("cssc:Uid");
            for (int k = 0; k < partidaID.length(); k++) {
                dolar = partidaID.getJSONObject(k);
                documents.setPartidaID(dolar.getString("$"));
                partidaId = dolar.getString("$");

                Log.d("partidaID", dolar.getString("$"));

            }

            for (int i = 1; i < parentArray.length(); i++) {


                Documents doc = new Documents();

                JSONObject issueDate = parentArray.getJSONObject(i);
                dolar = issueDate.getJSONObject("ft:IssueDate");
                doc.setIssueDate(dolar.getString("$"));
                nachalnaData.setText(doc.getIssueDate());
                Log.d("IssueDate", dolar.getString("$"));

                JSONObject dueDate = parentArray.getJSONObject(i);
                dolar = dueDate.getJSONObject("ft:DueDate");
                doc.setDueDate(dolar.getString("$"));
                krainaData.setText(doc.getDueDate());
                Log.d("dueDate", dolar.getString("$"));

                JSONObject documentNumber = parentArray.getJSONObject(i);
                dolar = documentNumber.getJSONObject("ft:DocumentNumber");
                doc.setDocumentNumber(dolar.getString("$"));
                Log.d("documentNumber", dolar.getString("$"));

                JSONObject amount = parentArray.getJSONObject(i);
                dolar = amount.getJSONObject("ft:Amount");
                doc.setAmount(dolar.getString("$"));
                saldoNachalo.setText(doc.getAmount());

                Log.d("amount", dolar.getString("$"));


                JSONObject statusType = parentArray.getJSONObject(i);
                dolar = statusType.getJSONObject("ft:Applied");
                doc.setStatusType(dolar.getString("$"));
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
                tekushtoSaldo.setText(doc.getBalance());
                saldoKraq.setText(doc.getBalance());

                Log.d("balance", dolar.getString("$"));

                JSONObject totalDiscount = parentArray.getJSONObject(i);
                dolar = totalDiscount.getJSONObject("ft:TotalDiscount");
                doc.setTotalDiscount(dolar.getString("$"));
                Log.d("totalDiscount", dolar.getString("$"));


                JSONObject forwardBalance = parentArray.getJSONObject(i);
                dolar = forwardBalance.getJSONObject("ft:Type");
                doc.setForwardBalance(dolar.getString("$"));
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



