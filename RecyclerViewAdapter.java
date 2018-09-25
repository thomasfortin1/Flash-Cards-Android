package com.example.thoma.SmartStudy;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    private ArrayList<Deck> mDecks = new ArrayList<Deck>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<Deck> decks){
        mDecks = decks;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //grab the layout we made and pass it to ViewHolder() so we can find the Views we need later
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //set the TextView to display the correct deck name
        holder.deckName.setText(mDecks.get(position).getName());
        Log.d("position: ", String.valueOf(position) + " = " + mDecks.get(position).getName());

        //Set an onClick listener for each ViewHolder
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Edit_Deck.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }


    //Object to hold all the views in each row for the recyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView deckName;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.textView2);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
