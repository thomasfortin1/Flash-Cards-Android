package com.example.thoma.flashcards2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

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
        Deck deck = new Deck("New Deck");
        deck.addCard(new Card("New card's front", "New card's back"));
        MainActivity.Decks.add(deck);

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
