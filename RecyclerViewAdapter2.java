package com.example.thoma.flashcards2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.ArrayList;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {

    private ArrayList<Deck> mDecks = new ArrayList<Deck>();
    private Context mContext;

    public RecyclerViewAdapter2( Context mContext, ArrayList<Deck> mDecks) {
        this.mDecks = mDecks;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.s.setText(mDecks.get(position).getName());
        if(mDecks.get(position).getActive()) holder.s.setChecked(true);
        else holder.s.setChecked(false);
        holder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.Decks.get(position).setActive(true);
                    mDecks.get(position).setActive(true);
                }
                else {
                    MainActivity.Decks.get(position).setActive(false);
                    mDecks.get(position).setActive(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Switch s;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            s = itemView.findViewById(R.id.Settings_Recycler_Switch);
            parentLayout = itemView.findViewById(R.id.Settings_Parent_Layout);
        }
    }
}
