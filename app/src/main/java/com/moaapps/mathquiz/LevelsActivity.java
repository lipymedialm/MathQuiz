package com.moaapps.mathquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.moaapps.mathquiz.adapters.LevelsAdapter;
import com.moaapps.mathquiz.adapters.Utils;

import java.util.ArrayList;

public class LevelsActivity extends AppCompatActivity {


    public static Activity mLevelsActivity;
    boolean isMusicOn;
    private AdView mAdView;


    ArrayList<Integer> levels;
    LevelsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        AddView Code is here this is a test add till now

        mLevelsActivity = this;
        isMusicOn = getSharedPreferences("PRE" , MODE_PRIVATE).getBoolean("sound" , true);

        levels = new ArrayList<>();

        for (int i = 1; i <= 100 ; i++){
            levels.add(i);
        }

        adapter = new LevelsAdapter(levels , getApplicationContext());
        RecyclerView levelsRecycler = findViewById(R.id.level_recycler);
        levelsRecycler.setHasFixedSize(false);
        levelsRecycler.setLayoutManager(new GridLayoutManager(this , 3));
        levelsRecycler.setAdapter(adapter);

        adapter.setOnCleckListener(new LevelsAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                int lastLevel = getSharedPreferences("PRE" , Context.MODE_PRIVATE).getInt("last_level" ,1);
                int levelNum = position+1;
                if (levelNum <= lastLevel){
                    if (isMusicOn){
                        new Utils().clickSound(getApplicationContext());
                    }
                    Intent intent = new Intent(getApplicationContext() , QuestionsActivity.class);
                    intent.putExtra("type" , "levels_type");
                    intent.putExtra("level" , levelNum);
                    startActivity(intent);
                }else {
                    Snackbar.make(findViewById(android.R.id.content) , "This level is locked" , Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
//       Finished Code Adapter Here
    }
}
