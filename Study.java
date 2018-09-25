package com.example.thoma.SmartStudy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class Study extends AppCompatActivity {

    private int side = 0;
    int randomness;
    TextView cardView;
    TextView sideView;
    RatingBar ratingBar;
    private static final String front = "Front";
    private static final String back = "Back";
    Dealer dealer;
    Card currentCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        cardView = findViewById(R.id.Study_Card_Text);
        sideView = findViewById(R.id.Study_Side_Text);
        ratingBar = findViewById(R.id.ratingBar);

        SharedPreferences sharedPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        randomness = sharedPref.getInt("Randomness", 0);

        dealer = new Dealer();

        currentCard = dealer.next();
        cardView.setText(currentCard.getFront());
        sideView.setText(front);
        ratingBar.setRating(currentCard.getSaturation() / 2);
        ratingBar.setIsIndicator(true);
    }

    public void flip(View v) {
        if (dealer.empty()) return;
        side = (side + 1)%2;
        if(side == 0){
            cardView.setText(currentCard.getFront());
            sideView.setText(front);
        }
        else{
            cardView.setText(currentCard.getBack());
            sideView.setText(back);
        }
        next();
    }

    public void gotIt(View v){
        currentCard.gotIt();
        //Tell Dealer to put card into right bucket
        if(currentCard.getSaturation() == 2) dealer.makeFocused(currentCard);
        if(currentCard.getSaturation() == 8) dealer.makeUnfocused(currentCard);
        currentCard = dealer.next();
        side = 0;
        next();
    }

    public void didNotGetIt(View v){
        currentCard.didnotgetIT();
        //Tell Dealer to put card into right bucket
        if(currentCard.getSaturation() == 1) dealer.makeUnfocused(currentCard);
        if(currentCard.getSaturation() == 7) dealer.makeFocused(currentCard);
        currentCard = dealer.next();
        side = 0;
        next();
    }

    public void knowIt(View v){
        currentCard.knowIt();
        //Make sure this card goes into the unfocused bucket
        dealer.makeUnfocused(currentCard);
        currentCard = dealer.next();
        side = 0;
        next();
    }

    public void next(){
        //update Views
        if(side == 0){
            cardView.setText(currentCard.getFront());
            sideView.setText(front);
        }
        else{
            cardView.setText(currentCard.getBack());
            sideView.setText(back);
        }
        ratingBar.setRating(currentCard.getSaturation() / 2);
    }

    private class Dealer {

        ArrayList<Card> cards = new ArrayList<Card>();
        ArrayList<Card> focusedCards = new ArrayList<Card>();
        ArrayList<Card> unfocusedCards = new ArrayList<Card>();

        public Dealer(){
            //Sort all the cards into focused and unfocused buckets
            //Focused cards have saturation between 2 & 7, inclusively
            for(int x = 0; x < MainActivity.Decks.size(); x++){
                if(MainActivity.Decks.get(x).getActive()){
                    for(int y = 0; y < MainActivity.Decks.get(x).getSize(); y++){
                        cards.add(MainActivity.Decks.get(x).getCard(y));
                        if (MainActivity.Decks.get(x).getCard(y).getSaturation() > 1 && MainActivity.Decks.get(x).getCard(y).getSaturation() < 8){
                            focusedCards.add(MainActivity.Decks.get(x).getCard(y));
                        }
                        else unfocusedCards.add(MainActivity.Decks.get(x).getCard(y));
                    }
                }
            }
        }

        public boolean empty(){
            if (cards.size() == 0) return true;
            else return false;
        }

        public boolean makeFocused(Card card){
            if(focusedCards.contains(card)) return false;
            focusedCards.add(card);
            unfocusedCards.remove(card);
            return true;
        }

        public boolean makeUnfocused(Card card){
            if(unfocusedCards.contains(card)) return false;
            unfocusedCards.add(card);
            focusedCards.remove(card);
            return true;
        }

        public Card next(){
            if (empty()) return new Card("There currently are no active decks\nGo to Edit Decks to make a new one or go to Settings to activate one.", "There currently are no active decks\nGo to Edit Decks to make a new one or go to Settings to activate one.");

            Random random = new Random();

            //If either focused or unfocused card buckets are empty just return a random card from the other
            if(focusedCards.size() == 0) return unfocusedCards.get(random.nextInt(unfocusedCards.size()));
            if(unfocusedCards.size() == 0) return focusedCards.get(random.nextInt(focusedCards.size()));

            //If we have 7 or more focused cards then we want to focus on those ones
            if(focusedCards.size() >= 7){
                if(random.nextInt(100) + 1 > randomness){
                    return focusedCards.get(random.nextInt(focusedCards.size()));
                }
                //if randomness is high enough we may still just return a random card
                else {
                    return cards.get(random.nextInt(cards.size()));
                }
            }
            //If we have less than 7 focused cards then we want to get more
            else{
                if(random.nextInt(100) + 1 > randomness){
                    //We would prefer to show a card that has a saturation less
                    //than 2, so we make a new bucket just for this case
                    //called unknown cards
                    ArrayList<Card> unknownCards = new ArrayList<Card>();
                    for(int x = 0; x < unfocusedCards.size(); x++){
                        if(unfocusedCards.get(x).getSaturation() < 2) unknownCards.add(unfocusedCards.get(x));
                    }
                    //If there are any unknown cards then return one of those
                    //Otherwise just return a random card
                    if(unknownCards.size() > 0) return unknownCards.get(random.nextInt(unknownCards.size()));
                    else return cards.get(random.nextInt(cards.size()));
                }
                // If randomness is high enough we may just return a random card anyways
                else{
                    return cards.get(random.nextInt(cards.size()));
                }
            }
        }
    }

    @Override
    protected void onPause() {
        MainActivity.saveDecks(this);
        super.onPause();
    }
}