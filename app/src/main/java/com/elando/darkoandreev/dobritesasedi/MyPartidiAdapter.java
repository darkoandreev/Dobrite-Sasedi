package com.elando.darkoandreev.dobritesasedi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by darko.andreev on 6/2/2017.
 */

public class MyPartidiAdapter extends ArrayAdapter<PartidiInfo> {

    public MyPartidiAdapter (Context context, ArrayList<PartidiInfo> partidiInfo) {
        super(context, 0, partidiInfo);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PartidiInfo partidi = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_items, parent, false);
        }


        TextView partidaNomer = (TextView) convertView.findViewById(R.id.partidaNomer);
        TextView partidaBalance = (TextView) convertView.findViewById(R.id.partidaBalance);
        TextView partidaPropertyRefs = (TextView) convertView.findViewById(R.id.partidaPropertyRefs);
        TextView holderAccount = (TextView) convertView.findViewById(R.id.inovice);

        partidaNomer.setText(partidi.partidaNomer);
        partidaBalance.setText(partidi.partidaBalance + "лв.");
        partidaPropertyRefs.setText(partidi.partidaPropertyRefs);
        holderAccount.setText(partidi.holderAccount);

        return convertView;
    }


}
