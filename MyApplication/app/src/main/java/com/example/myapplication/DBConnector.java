package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBConnector extends SQLiteOpenHelper {

    // Users table
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_UID = "uid";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Booking table
    private static final String TABLE_BOOKING = "Booking";
    private static final String COLUMN_EVENT_ID = "eventID";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_TICKET_COUNT = "ticketCount";

    // Database constants
    private static final String DB_NAME = "MyApp.db";
    private static final int DB_VERSION = 2;

    private Context context;

    public DBConnector(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Booking table
        String createBookingTable = "CREATE TABLE " + TABLE_BOOKING + "(" +
                COLUMN_EVENT_ID + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_TICKET_COUNT + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_EVENT_ID + "))";
        db.execSQL(createBookingTable);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing Booking table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);

        // Create a new Booking table with the updated schema
        String createBookingTable = "CREATE TABLE " + TABLE_BOOKING + "(" +
                COLUMN_EVENT_ID + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_TICKET_COUNT + " INTEGER, " +
                "PRIMARY KEY (" + COLUMN_EVENT_ID + "))";
        db.execSQL(createBookingTable);
    }


    // User methods

    public void addNewUser(String name, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Booking methods

    public void addBooking(String eventID, String genre, String title, int ticketCount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_ID, eventID);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_TICKET_COUNT, ticketCount);

        int rowsAffected = db.update(TABLE_BOOKING, values, COLUMN_EVENT_ID + " = ?", new String[]{eventID});

        if (rowsAffected == 0) {
            // No existing row found with the provided eventID
            db.insert(TABLE_BOOKING, null, values);
        }

        db.close();
    }

    public String searchBooking(String eventID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKING +
                " WHERE " + COLUMN_EVENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{eventID});
        String result = null;
        if (cursor.moveToFirst()) {
            result = "Event ID: " + cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID)) + "\n" +
                    "Title: " + cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)) + "\n" +
                    "Genre: " + cursor.getString(cursor.getColumnIndex(COLUMN_GENRE));
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<String> getAllBookings() {
        ArrayList<String> bookingsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKING;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String bookingInfo = "Event ID: " + cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID)) + "\n" +
                        "Title: " + cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)) + "\n" +
                        "Genre: " + cursor.getString(cursor.getColumnIndex(COLUMN_GENRE));
                bookingsList.add(bookingInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookingsList;
    }
    public List<String> getTicketsForEvent(String eventID) {
        List<String> ticketsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKING +
                " WHERE " + COLUMN_EVENT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{eventID});
        if (cursor.moveToFirst()) {
            do {
                String ticketInfo = "Event ID: " + cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_ID)) + "\n" +
                        "Title: " + cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)) + "\n" +
                        "Genre: " + cursor.getString(cursor.getColumnIndex(COLUMN_GENRE)) + "\n" +
                        "Ticket Count: " + cursor.getInt(cursor.getColumnIndex(COLUMN_TICKET_COUNT));
                ticketsList.add(ticketInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ticketsList;
    }

    public void updateBooking(String eventID, String newTitle, String newGenre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_GENRE, newGenre);
        db.update(TABLE_BOOKING, values, COLUMN_EVENT_ID + " = ?", new String[]{eventID});
        db.close();
    }

    public void deleteBooking(String eventID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKING, COLUMN_EVENT_ID + " = ?", new String[]{eventID});
        db.close();
    }

}
