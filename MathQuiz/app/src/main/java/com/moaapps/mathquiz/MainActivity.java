package com.moaapps.mathquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.moaapps.mathquiz.adapters.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isMusicOn;
    ImageButton soundSwitch;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        isMusicOn = getSharedPreferences("PRE" , MODE_PRIVATE).getBoolean("sound" , true);


        findViewById(R.id.start_btn).setOnClickListener(this);
        soundSwitch = findViewById(R.id.sound_switch);
        soundSwitch.setOnClickListener(this);
        if (isMusicOn){
            soundSwitch.setImageResource(R.drawable.music_on);
        }else {
            soundSwitch.setImageResource(R.drawable.music_off);
        }

        findViewById(R.id.addition_btn).setOnClickListener(this);
        findViewById(R.id.subtraction_btn).setOnClickListener(this);
        findViewById(R.id.multiplication_btn).setOnClickListener(this);
        findViewById(R.id.division_btn).setOnClickListener(this);
        findViewById(R.id.mix_btn).setOnClickListener(this);

        findViewById(R.id.about).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.start_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                startActivity(new Intent(getApplicationContext() , LevelsActivity.class));
                break;
            case R.id.addition_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Intent intent = new Intent(getApplicationContext() , QuestionsActivity.class);
                intent.putExtra("level" , 0);
                intent.putExtra("type" , "+");
                startActivity(intent);
                break;

            case R.id.subtraction_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Intent intent1 = new Intent(getApplicationContext() , QuestionsActivity.class);
                intent1.putExtra("level" , 0);
                intent1.putExtra("type" , "-");
                startActivity(intent1);
                break;

            case R.id.multiplication_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Intent intent2 = new Intent(getApplicationContext() , QuestionsActivity.class);
                intent2.putExtra("level" , 0);
                intent2.putExtra("type" , "x");
                startActivity(intent2);
                break;


            case R.id.division_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Intent intent3 = new Intent(getApplicationContext() , QuestionsActivity.class);
                intent3.putExtra("level" , 0);
                intent3.putExtra("type" , "÷");
                startActivity(intent3);
                break;

            case R.id.mix_btn:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Intent intent4 = new Intent(getApplicationContext() , QuestionsActivity.class);
                intent4.putExtra("level" , 0);
                intent4.putExtra("type" , "mix");
                startActivity(intent4);
                break;


            case R.id.sound_switch:
                if (isMusicOn){
                    getSharedPreferences("PRE" , MODE_PRIVATE).edit().putBoolean("sound" , false).commit();
                    soundSwitch.setImageResource(R.drawable.music_off);
                    isMusicOn = getSharedPreferences("PRE" , MODE_PRIVATE).getBoolean("sound" , true);
                }else {
                    getSharedPreferences("PRE" , MODE_PRIVATE).edit().putBoolean("sound" , true).commit();
                    soundSwitch.setImageResource(R.drawable.music_on);
                    isMusicOn = getSharedPreferences("PRE" , MODE_PRIVATE).getBoolean("sound" , true);
                    if (isMusicOn){
                        new Utils().clickSound(this);
                    }
                }
                break;

            case R.id.about:
                if (isMusicOn){
                    new Utils().clickSound(this);
                }
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this).setTitle("Exit")
                .setMessage("Do you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel" , null)
                .show();

//        menu code starts from here


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu1,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Aboutus:
                Toast.makeText(this, "About us selected", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),About_usActivity.class);
                startActivity(intent);

        }

        switch (item.getItemId()){
            case R.id.rules:
                Toast.makeText(this, "Rules Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),RuleActivity.class);
                startActivity(intent);
        }

        switch (item.getItemId()){
            case R.id.term_condition:
                Toast.makeText(this, "Term Selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Term_ConditionActivity.class);
                startActivity(intent);
        }
//        start here new activity code here witch define to the part of the MainActivity go to the another activity
        switch (item.getItemId()){
            case R.id.about:
        }

        switch (item.getItemId()){
            case R.id.privacy:
                Toast.makeText(this, "Clicked Privacy Policy", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
