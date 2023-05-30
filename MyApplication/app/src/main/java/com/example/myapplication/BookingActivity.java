package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private EditText eventIDEditText, titleEditText, genreEditText;
    private TextView resultTextView;

    private DBConnector dbConnector;
    private List<String> bookingsList; // List to store all bookings
    private int ticketCount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking); // Update the layout file name

        dbConnector = new DBConnector(this);

        eventIDEditText = findViewById(R.id.eventIDEditText);
        titleEditText = findViewById(R.id.titleEditText);
        genreEditText = findViewById(R.id.genreEditText);
        resultTextView = findViewById(R.id.resultTextView);

        Button addButton = findViewById(R.id.addButton);
        Button searchButton = findViewById(R.id.searchButton);
        Button showAllButton = findViewById(R.id.showAllButton);
        Button updateButton = findViewById(R.id.updateButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        // Initialize the bookings list
        bookingsList = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventID = eventIDEditText.getText().toString();
                String title = genreEditText.getText().toString(); // Swap genreEditText and titleEditText
                String genre = titleEditText.getText().toString(); // Swap titleEditText and genreEditText

                if (eventID.isEmpty() || title.isEmpty() || genre.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    dbConnector.addBooking(eventID, title, genre, getTicketCount());

                    Toast.makeText(BookingActivity.this, "Booking added successfully", Toast.LENGTH_SHORT).show();
                    // Clear the input fields after adding the booking
                    eventIDEditText.setText("");
                    titleEditText.setText("");
                    genreEditText.setText("");
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventID = eventIDEditText.getText().toString();
                if (eventID.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Please enter an Event ID", Toast.LENGTH_SHORT).show();
                } else {
                    displayTicketsForEvent(eventID);
                }
            }
        });

        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookingsList = dbConnector.getAllBookings();
                if (bookingsList.isEmpty()) {
                    resultTextView.setText("No bookings available");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String booking : bookingsList) {
                        sb.append(booking).append("\n\n");
                    }
                    resultTextView.setText(sb.toString());
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventID = eventIDEditText.getText().toString();
                String newTitle = titleEditText.getText().toString();
                String newGenre = genreEditText.getText().toString();

                dbConnector.updateBooking(eventID, newTitle, newGenre);
                Toast.makeText(BookingActivity.this, "Booking updated successfully", Toast.LENGTH_SHORT).show();
                // Clear the input fields after updating the booking
                eventIDEditText.setText("");
                titleEditText.setText("");
                genreEditText.setText("");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventID = eventIDEditText.getText().toString();

                dbConnector.deleteBooking(eventID);
                Toast.makeText(BookingActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                // Clear the input field after deleting the booking
                eventIDEditText.setText("");
            }
        });

        EditText filterEditText = findViewById(R.id.filterEditText);
        Button filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = filterEditText.getText().toString();
                filterBookings(keyword);
            }
        });
    }

    // Additional method to handle filter/search functionality
    private void filterBookings(String keyword) {
        List<String> filteredList = new ArrayList<>();
        for (String booking : bookingsList) {
            // Check if the booking genre contains the keyword
            if (booking.toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(booking);
            }
        }

        if (filteredList.isEmpty()) {
            resultTextView.setText("No matching bookings found");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String booking : filteredList) {
                sb.append(booking).append("\n\n");
            }
            resultTextView.setText(sb.toString());
        }
    }
    private void displayTicketsForEvent(String eventID) {
        List<String> ticketsList = dbConnector.getTicketsForEvent(eventID);
        if (ticketsList.isEmpty()) {
            resultTextView.setText("No tickets available for this event");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String ticket : ticketsList) {
                sb.append(ticket).append("\n\n");
            }
            resultTextView.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbConnector.close();
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
