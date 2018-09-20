package com.example.thoma.flashcards2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_Deck extends AppCompatActivity {

    private int position;
    private int cardNum = 0;
    private int side = 0;
    private boolean update = true;
    EditText editText;
    EditText editTextTitle;
    TextView sideView;
    TextView numberView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__deck);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Log.d("Edit_Deck", "Decks.get(position).getSize() = " + MainActivity.Decks.get(position).getSize());
        editText = findViewById(R.id.Edit_Text);
        editTextTitle = findViewById(R.id.Edit_Text_Title);
        sideView = findViewById(R.id.Edit_Deck_Side_View);
        numberView = findViewById(R.id.Edit_Deck_Card_Number);
        cardNum = 0;
        editTextTitle.setText(MainActivity.Decks.get(position).getName());
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
        }
    }

    @Override
    protected void onPause(){
        if(update) UpdateDeck();
        super.onPause();
    }

    public void Next(View v){Next();}
    public void Next(){
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            UpdateDeck();
            cardNum = Mod((cardNum + 1), MainActivity.Decks.get(position).getSize());
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
            side = 0;
        }
    }

    public void Last(View v){
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            UpdateDeck();
            cardNum = Mod((cardNum - 1), MainActivity.Decks.get(position).getSize());
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
            side = 0;
        }
    }

    public void Flip(View v){
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            UpdateDeck();
            if (side == 0)
                editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getBack());
            else editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            side = (side + 1) % 2;
            if(side == 0) sideView.setText("Front");
            else sideView.setText("Back");
        }
    }

    public void UpdateDeck(){
       if(side == 0) MainActivity.Decks.get(position).getCard(cardNum).ModifyFront(editText.getText().toString());
       else MainActivity.Decks.get(position).getCard(cardNum).ModifyBack(editText.getText().toString());
       MainActivity.Decks.get(position).editName(editTextTitle.getText().toString());
    }

    public void New(View v){
        MainActivity.Decks.get(position).addCard(new Card("New card's front", "New card's back"));
        cardNum = MainActivity.Decks.get(position).getSize() - 1;
        editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
        sideView.setText("Front");
        numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
        update = true;
    }

    public void Delete(View v){
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            MainActivity.Decks.get(position).removeCard(cardNum);
            if (MainActivity.Decks.get(position).getSize() != 0){
                cardNum = Mod((cardNum - 1), MainActivity.Decks.get(position).getSize());
                editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
                numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
                sideView.setText("Front");
            }
            else{
                update = false;
                NoCards();
            }

            Log.d("", String.valueOf(cardNum));
            side = 0;
        }
    }

    public int Mod(int x, int y){
        int z = x%y;
        if(z < 0) z += y;
        return z;
    }

    public void deleteDeck(View v){

        MainActivity.Decks.remove(position);
        Toast.makeText(this, String.valueOf(MainActivity.Decks.get(0).getName()) + String.valueOf(MainActivity.Decks.size()), Toast.LENGTH_LONG).show();
        update = false;
        Intent intent = new Intent(this, Edit_Decks.class);
        startActivity(intent);
    }

    public void NoCards(){
        editText.setText("There are no cards in this deck yet");
        sideView.setText("ERROR");
        numberView.setText("ERROR");
    }
}