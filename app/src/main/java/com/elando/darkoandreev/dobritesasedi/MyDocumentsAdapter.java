package com.elando.darkoandreev.dobritesasedi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.elando.darkoandreev.dobritesasedi.DocumentsModel.Documents;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 5/29/2017.
 */

public class MyDocumentsAdapter extends ArrayAdapter<Documents> {

   public MyDocumentsAdapter (Context context, ArrayList<Documents> documents) {
       super(context, 0, documents);
   }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Documents documents = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_list_items, parent, false);
        }


        TextView documentNumber = (TextView) convertView.findViewById(R.id.documentNumber);
        TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);
        TextView balance = (TextView) convertView.findViewById(R.id.balance);
        TextView status = (TextView) convertView.findViewById(R.id.statusType);
        TextView invoice = (TextView) convertView.findViewById(R.id.inovice);
        TextView data = (TextView) convertView.findViewById(R.id.data);

        documentNumber.setText(documents.documentNumber);
        totalAmount.setText(documents.totalAmount + "лв.");
        balance.setText(documents.balance + "лв.");
        status.setText(documents.statusType);


        if(status.getText().toString().equals("NotPaid") || status.getText().toString().equals("No status")) {
            status.setTextColor(Color.RED);
        } else {
            status.setTextColor(Color.GREEN);
        }


        invoice.setText(documents.forwardBalance);
        data.setText(documents.issueDate);

        if(invoice.getText().toString().equals("ForwardBalance")) {
            invoice.setText("");
        }


        return convertView;
    }


}
