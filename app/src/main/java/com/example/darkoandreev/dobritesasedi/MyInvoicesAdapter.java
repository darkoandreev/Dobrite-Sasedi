package com.example.darkoandreev.dobritesasedi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 6/27/2017.
 */

public class MyInvoicesAdapter extends ArrayAdapter<InvoicesInfo> {

    public MyInvoicesAdapter (Context context, ArrayList<InvoicesInfo> invoicesInfo) {
        super(context, 0, invoicesInfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InvoicesInfo invoices = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoices_list_items, parent, false);
        }


        TextView obshtaOhranaID = (TextView) convertView.findViewById(R.id.obshtaOhranaID);
        TextView kolichestvoID = (TextView) convertView.findViewById(R.id.kolichestvoID);
        TextView totalInvoicesID = (TextView) convertView.findViewById(R.id.totalListInvoiceID);
        TextView obshtaOhranaIDinfo = (TextView) convertView.findViewById(R.id.textView20);

        obshtaOhranaID.setText(invoices.obshtaOhranaInvoices);
        kolichestvoID.setText(invoices.kolichestvoInvoices);
        totalInvoicesID.setText(invoices.totalListInvoices);
        obshtaOhranaIDinfo.setText(invoices.obshtaOhranaInfo);

        return convertView;
    }
}
