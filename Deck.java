package com.example.thoma.flashcards2;

import android.util.Log;

import java.util.ArrayList;

public class Deck {
    ArrayList<Card> Cards = new ArrayList<Card>();
    String name;
    boolean active;

    public Deck(String name){
        this.name = name;
        active = true;
    }

    public Deck(String name, boolean active){
        this.name = name;
        this.active = active;
    }

    public void editName(String name){
        this.name = name;
    }

    public void addCard(Card card){
        Cards.add(card);
    }

    public void removeCard(int pos){
        Cards.remove(pos);
        Log.d("removeCard", "Removed card successfully" + Cards);
    }

    public Card getCard(int pos){
        return Cards.get(pos);
    }

    public String getName(){
        return name;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean getActive(){
        return active;
    }

    public int getSize(){
        return Cards.size();
    }

}
