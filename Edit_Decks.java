package com.example.thoma.SmartStudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class Edit_Decks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__decks);
        initRecyclerView();
    }

    public void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, MainActivity.Decks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void NewDeck(View v){
        //Make a new deck with one default card inside
        Deck deck = new Deck("New Deck");
        deck.addCard(new Card("New card's front", "New card's back"));
        MainActivity.Decks.add(deck);
        //Start Edit_Decks activity so the user can modify the deck's cards
        Intent intent = new Intent(this, Edit_Deck.class);
        intent.putExtra("position", MainActivity.Decks.size() - 1);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        MainActivity.saveDecks(this);
        super.onStop();
    }
}
