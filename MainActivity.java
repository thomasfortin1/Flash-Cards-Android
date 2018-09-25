package com.example.thoma.SmartStudy;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Deck> Decks = new ArrayList<Deck>();
    public static boolean activate = true;

    SQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up database to save Decks
        helper = new SQLiteOpenHelper(this, "MyDatabase", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db){

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        //We only want to load the Decks if we just restarted the app
        if (activate) loadDecks();
        activate = false;
    }

    @Override
    protected void onStop() {
        saveDecks(this);
        super.onStop();
    }

    public static void saveDecks(Context context){

        //Set up database
        //This function has to be static so it can be called by other activities.
        //So we need to make a new SQLiteOpenHelper because static methods can't use anything outside
        //of the function itself
        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, "MyDatabase", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db){
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };

        boolean foundDecks = true;
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor;

        //Check to see if we've made a table for Decks yet
        try{database.query("Decks", null, null, null, null, null, null);}
        catch(SQLException e){
            foundDecks = false;
        }

        //If we have then get rid of it and all tables for all other decks
        //The database has one table for all the Decks
        //And a table for each deck with all their cards
        if(foundDecks) {
            cursor = database.query("Decks", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                try {
                    database.execSQL("DROP TABLE a" + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("id"))));
                    Log.d("save", "deleted table 0");
                } catch (SQLException e) {
                    Log.d("save", "couldn't delete table 0");
                }
            }
            database.execSQL("DROP TABLE Decks");
            Log.d("save", "deleted Decks");
        }

        //Then make them all again with the new data
        int active;
        ContentValues values  = new ContentValues();
        database.execSQL("CREATE TABLE Decks(id int primary key, name text, active int)");

        //Table with deck names
        for(int x = 0; x < Decks.size(); x++){
            if(Decks.get(x).getActive()) active = 1;
            else active = 0;

            values.put("id", x);
            values.put("name", Decks.get(x).getName());
            values.put("active", active);

            database.insert("Decks", null, values);

            values.clear();
        }

        //Tables with cards for each deck
        for(int x = 0; x < Decks.size(); x++){
            boolean foundTable = true;
            try{database.execSQL("CREATE TABLE a" + String.valueOf(x) + "(id int primary key, front text, back text, saturation int)");}
            catch(SQLException e){
                foundTable = false;
                Log.d("save", "unsuccessfully saved");
            }
            if (foundTable) {
                Log.d("save", "successfully saved");
                for (int y = 0; y < Decks.get(x).getSize(); y++) {
                    Log.d("save", "Decks.get(x).getSize() = " + Decks.get(x).getSize());
                    values.put("id", y);
                    values.put("front", Decks.get(x).getCard(y).getFront());
                    values.put("back", Decks.get(x).getCard(y).getBack());
                    values.put("saturation", Decks.get(x).getCard(y).getSaturation());
                    database.insert("a" + String.valueOf(x), null, values);
                    values.clear();
                }
            }
        }
    }

    private void loadDecks() {
        SQLiteDatabase database = helper.getReadableDatabase();

        //Make sure there is a table for the decks
        //If there isn't then just return
        try{database.query("Decks", null, null, null, null, null, null);}
        catch(SQLException e){
            Log.d("database query", "Didn't find deck");
            return;
        }

        //Go through deck table and create a Deck object for each row
        Log.d("database query", "Did find deck");
        Cursor cursor = database.query("Decks", null, null, null, null, null, null);
        String name;
        boolean active;
        Log.d("before while loop", "got this far");
        while(cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndexOrThrow("active")) == 1) active = true;
            else active = false;
            Log.d("load", "active = " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(("active")))));
            if(!active) Log.d("load", "active = false");
            if(active) Log.d("load", "active = true");
            name = cursor.getString(cursor.getColumnIndexOrThrow(("name")));
            Decks.add(new Deck(name, active));
            Log.d("cursor", "corsor moved to next in load");
        }
        Log.d("after while loop", "got this far");


        //Go through the table for each deck and populate the decks with the appropriate cards
        String front;
        String back;
        int saturation;
        for(int x = 0; x < Decks.size(); x++){
            Log.d("inside forloop", "value of x: " + x + " value of Decks.size(): " + Decks.size());
            cursor = database.query("a" + String.valueOf(x), null, null, null, null, null, null);
            while(cursor.moveToNext()) {
                front = cursor.getString(cursor.getColumnIndexOrThrow("front"));
                back = cursor.getString(cursor.getColumnIndexOrThrow("back"));
                saturation = cursor.getInt(cursor.getColumnIndexOrThrow(("saturation")));
                Decks.get(x).addCard(new Card(front, back, saturation));
            }
        }
        Log.d("end of function", "got this far");
    }

    //For testing purposes
    public void testRecyclerView(){
        for(int x = 0; x < 100; x++){
            MainActivity.Decks.add(new Deck(String.valueOf(x)));
            for(int y = 0; y < 5; y++)
                MainActivity.Decks.get(x).addCard(new Card("hi there" + y, "bye now" + x));
        }
    }

    public void StartEditDecks(View view){
        Intent intent = new Intent(this, Edit_Decks.class);
        startActivity(intent);
    }

    public void StartStudy(View view){
        Intent intent = new Intent(this, Study.class);
        startActivity(intent);
    }

    public void StartSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}