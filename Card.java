package com.example.thoma.flashcards2;

public class Card {
    String front;
    String back;
    int saturation;

    public Card(String front, String back, int saturation){
        this.front = front;
        this.back = back;
        this.saturation = saturation;
    }

    public Card(String front, String back){
        this.front = front;
        this.back = back;
        saturation = 0;
    }

    public void ModifyFront(String text){
        front = text;
    }

    public void ModifyBack(String text){
        back = text;
    }

    public int getSaturation(){
        return saturation;
    }

    public String getBack() {
        return back;
    }

    public String getFront(){
        return front;
    }

    public void gotIt(){
        if (saturation < 10) saturation++;
    }

    public void didnotgetIT(){
        if(saturation > 0) saturation--;
    }

    public void knowIt(){
        saturation = 10;
    }
}
