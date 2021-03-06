package com.example.thoma.SmartStudy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBar;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekBar = findViewById(R.id.seekBar);
        initListView();
        context = this;

        //Get the saved value for Randomness and set the seekbar to match it
        SharedPreferences sharedPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        seekBar.setProgress(sharedPref.getInt("Randomness", 0));
    }

    public void initListView(){
        ListView list = findViewById(R.id.Settings_List_View);
        ArrayAdapter<Deck> adapter = new MyAdapter();
        list.setAdapter(adapter);
    }

    private class MyAdapter extends ArrayAdapter<Deck> {

        public MyAdapter() {
            super(SettingsActivity.this, R.layout.layout_listitem2, MainActivity.Decks);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Take the layout we made and turn it into its own view
            //Set all the views within that layout
            //Return the new view
            View view = getLayoutInflater().inflate(R.layout.layout_listitem2, parent, false);

            Switch s = (Switch) view.findViewById(R.id.Settings_Recycler_Switch);
            s.setText(MainActivity.Decks.get(position).getName());
            if(MainActivity.Decks.get(position).getActive()) s.setChecked(true);
            else s.setChecked(false);

            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.Decks.get(position).setActive(isChecked);
                }
            });

            return view;
        }
    }

    @Override
    protected void onPause() {
        //save everything
        super.onPause();
        SharedPreferences sharedPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Randomness", seekBar.getProgress());
        editor.commit();
        MainActivity.saveDecks(this);
    }
}
