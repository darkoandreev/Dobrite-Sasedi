package com.example.darkoandreev.webservicetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.darkoandreev.webservicetest.DocumentsModel.Documents;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 5/29/2017.
 */

public class MyAdapter extends ArrayAdapter<Documents> {

   public MyAdapter (Context context, ArrayList<Documents> documents) {
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

        documentNumber.setText(documents.documentNumber);
        totalAmount.setText(documents.totalAmount);
        balance.setText(documents.balance);


        return convertView;
    }
}
