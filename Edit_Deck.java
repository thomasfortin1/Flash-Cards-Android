package com.example.thoma.SmartStudy;

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

        //Deal with no cards case
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
        }
    }

    @Override
    protected void onPause(){
        //If the deck has been deleted or there are no cards left we don't
        //want to save what's currently on the screen
        if(update) UpdateDeck();
        super.onPause();
    }

    //two Next() functions so we can call it from the button and from
    //within code
    public void Next(View v){Next();}
    public void Next(){
        //Deal with no Cards case
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            UpdateDeck();
            //use Mod to go from end to beginning
            cardNum = Mod((cardNum + 1), MainActivity.Decks.get(position).getSize());
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
            side = 0;//we always want to start on the front
        }
    }

    public void Last(View v){
        //Deal with no cards case
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            UpdateDeck();
            //Use Mod to go from beginning to end
            cardNum = Mod((cardNum - 1), MainActivity.Decks.get(position).getSize());
            editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
            sideView.setText("Front");
            numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
            side = 0;//always want to start on the front side
        }
    }

    public void Flip(View v){
        //Deal with no cards case
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
        //Put whatever is currently in the edit text fields into appropriate card
       if(side == 0) MainActivity.Decks.get(position).getCard(cardNum).ModifyFront(editText.getText().toString());
       else MainActivity.Decks.get(position).getCard(cardNum).ModifyBack(editText.getText().toString());
       MainActivity.Decks.get(position).editName(editTextTitle.getText().toString());
    }

    public void New(View v){
        //Make a new card with default front and back
        //Then move to that card so the user can modify it
        MainActivity.Decks.get(position).addCard(new Card("New card's front", "New card's back"));
        cardNum = MainActivity.Decks.get(position).getSize() - 1;
        editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
        sideView.setText("Front");
        numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
        //If we had already said that we shouldn't be updating because there are no cards
        //We need to change that because there are some cards now
        update = true;
    }

    public void Delete(View v){
        //Definitely don't want to delete anything if there isn't anything to delete
        if(MainActivity.Decks.get(position).getSize() == 0) NoCards();
        else {
            MainActivity.Decks.get(position).removeCard(cardNum);
            //if there are still some cards left just move to one of them
            if (MainActivity.Decks.get(position).getSize() != 0){
                cardNum = Mod((cardNum - 1), MainActivity.Decks.get(position).getSize());
                editText.setText(MainActivity.Decks.get(position).getCard(cardNum).getFront());
                numberView.setText((cardNum + 1) + "/" + MainActivity.Decks.get(position).getSize());
                sideView.setText("Front");
            }
            //if there aren't any cards left now then we don't want to
            //be updating anymore and we can just call NoCards()
            else{
                update = false;
                NoCards();
            }

            Log.d("", String.valueOf(cardNum));
            side = 0;//Always want to start on the front
        }
    }

    public int Mod(int x, int y){
        //Java doesn't have an actual Modulo function
        //% is just a remainder opperation so it sometimes gives
        //negative numbers. We don't want that so we made our
        //own Modulo function
        int z = x%y;
        if(z < 0) z += y;
        return z;
    }

    public void deleteDeck(View v){
        Toast.makeText(this, String.valueOf(MainActivity.Decks.get(0).getName()) + "has been deleted", Toast.LENGTH_LONG).show();
        MainActivity.Decks.remove(position);
        update = false;//set update to false wo we don't try to save something into the deck we just deleted
        //Go back to Edit_Decks Activity
        Intent intent = new Intent(this, Edit_Decks.class);
        startActivity(intent);
    }

    public void NoCards(){
        editText.setText("There are no cards in this deck yet");
        sideView.setText("ERROR");
        numberView.setText("ERROR");
    }
}