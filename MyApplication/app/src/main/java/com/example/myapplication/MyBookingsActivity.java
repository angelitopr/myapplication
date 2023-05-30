package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyBookingsActivity extends AppCompatActivity {

    private ListView bookingsListView;
    private DBConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        bookingsListView = findViewById(R.id.bookings_listview);
        dbConnector = new DBConnector(this);


        ArrayList<String> bookingsList = dbConnector.getAllBookings();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingsList);
        bookingsListView.setAdapter(adapter);
    }
}

