package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TicketsActivity extends Activity {

    private EditText eventIDEditText;
    private EditText titleEditText;
    private EditText genreEditText;
    private EditText ticketCountEditText;
    private Button addButton;
    private DBConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        eventIDEditText = findViewById(R.id.eventIDEditText);
        titleEditText = findViewById(R.id.titleEditText);
        genreEditText = findViewById(R.id.genreEditText);
        ticketCountEditText = findViewById(R.id.ticketCountEditText);
        addButton = findViewById(R.id.addButton);

        dbConnector = new DBConnector(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventID = eventIDEditText.getText().toString();
                String title = titleEditText.getText().toString();
                String genre = genreEditText.getText().toString();
                int ticketCount = Integer.parseInt(ticketCountEditText.getText().toString());

                dbConnector.addBooking(eventID, title, genre, ticketCount);

                // Clear the input fields
                eventIDEditText.setText("");
                titleEditText.setText("");
                genreEditText.setText("");
                ticketCountEditText.setText("");

                Toast.makeText(TicketsActivity.this, "Tickets added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
