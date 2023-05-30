package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookingDetailsActivity extends AppCompatActivity {

    private TextView eventIDTextView;
    private TextView titleTextView;
    private TextView genreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        eventIDTextView = findViewById(R.id.event_id_textview);
        titleTextView = findViewById(R.id.title_textview);
        genreTextView = findViewById(R.id.genre_textview);

        // Retrieve the event ID from the intent
        String eventID = getIntent().getStringExtra("eventID");

        // Retrieve the booking details from the database
        DBConnector dbConnector = new DBConnector(this);
        String bookingDetails = dbConnector.searchBooking(eventID);

        if (bookingDetails != null) {
            String[] detailsArray = bookingDetails.split("\n");
            if (detailsArray.length == 3) {
                String eventIDText = detailsArray[0].substring(detailsArray[0].indexOf(":") + 2);
                String titleText = detailsArray[1].substring(detailsArray[1].indexOf(":") + 2);
                String genreText = detailsArray[2].substring(detailsArray[2].indexOf(":") + 2);

                eventIDTextView.setText(eventIDText);
                titleTextView.setText(titleText);
                genreTextView.setText(genreText);
            }
        }
    }
}
