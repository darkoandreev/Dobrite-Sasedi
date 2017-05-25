package com.example.darkoandreev.webservicetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by darko.andreev on 5/23/2017.
 */

public class Property extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.property);

        ListView listView = (ListView) findViewById(R.id.documentsList);
        super.onCreate(savedInstanceState);

    }

}
