package com.example.darkoandreev.webservicetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 6/22/2017.
 */

public class MyPaymentsAdapter extends ArrayAdapter<PaymentsInfo> {

    public MyPaymentsAdapter (Context context, ArrayList<PaymentsInfo> paymentsInfo) {
        super(context, 0, paymentsInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PaymentsInfo payments = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payments_list_items, parent, false);
        }


        TextView paymentID = (TextView) convertView.findViewById(R.id.paymentID);
        TextView kreditID = (TextView) convertView.findViewById(R.id.kreditID);
        TextView platenoID = (TextView) convertView.findViewById(R.id.platenoID);
        TextView sumaID = (TextView) convertView.findViewById(R.id.sumaID);

        paymentID.setText(payments.paymentID);
        kreditID.setText(payments.kreditID);
        platenoID.setText(payments.platenoID);
        sumaID.setText(payments.sumaID);

        return convertView;
    }

}
