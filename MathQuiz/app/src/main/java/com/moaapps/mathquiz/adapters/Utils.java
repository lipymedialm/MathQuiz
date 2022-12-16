package com.moaapps.mathquiz.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.cardview.widget.CardView;

import com.moaapps.mathquiz.R;

public class Utils {
    public AlertDialog dialog;




    public void clickSound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
//        mp.stop();
//        mp.release();
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void correctSound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.correct);
//        mp.stop();
//        mp.release();
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void wrongSound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.wrong);
//        mp.stop();
//        mp.release();
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void completionSound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.win);
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public void gameOverSound(Context context){
        MediaPlayer mp = MediaPlayer.create(context, R.raw.game_over);
        mp.start();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    public MyReturnClass showCompletionDialog(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.completion_layout, null);
        CardView btn1 = view.findViewById(R.id.positive_button);
        CardView btn2 = view.findViewById(R.id.negative_button);
        dialog = new AlertDialog.Builder(context).setView(view)
                .setCancelable(false).create();

        CardView[] list = {btn1 , btn2};
        dialog.show();

        MyReturnClass returnClass = new MyReturnClass(list , dialog);
        return returnClass;

    }

    public MyReturnClass showGameOverDialog(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.game_over_layout, null);
        CardView btn1 = view.findViewById(R.id.positive_button);
        CardView btn2 = view.findViewById(R.id.negative_button);
        dialog = new AlertDialog.Builder(context).setView(view)
                .setCancelable(false).create();

        CardView[] list = {btn1 , btn2};

        dialog.show();

        MyReturnClass returnClass = new MyReturnClass(list , dialog);
        return returnClass;
    }

    public void dismissDialog(AlertDialog myDialog){

        myDialog.dismiss();
    }

}
