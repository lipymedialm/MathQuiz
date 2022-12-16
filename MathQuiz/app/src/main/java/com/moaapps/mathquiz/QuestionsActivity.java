package com.moaapps.mathquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.moaapps.mathquiz.adapters.MyReturnClass;
import com.moaapps.mathquiz.adapters.Utils;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.DecimalFormat;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class QuestionsActivity extends Activity implements View.OnClickListener {

    private AdView mAdView;
    int adsCounter = 0;
    private InterstitialAd mInterstitialAd;
    private RewardedAd rewardedAd;
    boolean userRewarded = false;
    boolean gotChance = false;


    String[] signs = {" + ", " - ", " X ", " / "};

    TextView mEquationHeader, mAnsOne, mAnsTwo, mAnsThree, mAnsFour, mCounter, mProgressText, mScoreText, mHighScoreText;
    CardView mCardOne, mCardTwo, mCardThree, mCardFour;
    ProgressBar QuestionProgress;

    int currentView = 0;
    int highScore;


    Double trueAnswer = 0.0;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");

    int counterTime = 0;
    int tempCounter = 0;
    boolean countDown = true;
    EasyFlipView flipView;


    int correctAnswers = 0;

    int currentLevel = 1;

    boolean isMusicOn;

    int progress = 0;

    String mGameType = null;

    int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };


        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                counterTime = tempCounter;
                countDown = true;
                new MyThread().start();
            }
        });

        rewardedAd = new RewardedAd(this,
                getString(R.string.admob_reward_id));
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        isMusicOn = getSharedPreferences("PRE", MODE_PRIVATE).getBoolean("sound", true);


        currentLevel = getIntent().getIntExtra("level", 1);

        mGameType = getIntent().getStringExtra("type");
        counterTime = 10;

        highScore = getSharedPreferences("PRE", MODE_PRIVATE).getInt("top_score", 0);


        flipView = findViewById(R.id.flip_view);
//        flipView.flipTheView();
        flipView.setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
            @Override
            public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                setActiveView(newCurrentSide);
                initViews();
                GenerateEquation();

            }
        });

        initViews();
        GenerateEquation();

//        mHighScoreText.setText("High Score\n" + highScore);


    }

    private void GenerateEquation() {

        counterTime = 10;
        countDown = true;
        gotChance = false;


        String equation = null;
        String finalEquation = null;
        if (mGameType.equals("levels_type")) {
            if (currentLevel < 5) {
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                equation = firstNum + signs[0] + nextNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (5 <= currentLevel && currentLevel < 10) {
                int firstNum = new Random().nextInt(99 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[0] + nextNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (10 <= currentLevel && currentLevel < 20) {
                int firstNum = new Random().nextInt(99 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[1] + nextNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (20 <= currentLevel && currentLevel < 25) {
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(9 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (25 <= currentLevel && currentLevel < 30) {
                counterTime = 15;
                int firstNum = new Random().nextInt(99 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum;
                finalEquation = equation.replaceAll("X", "*");


            } else if (30 <= currentLevel && currentLevel < 35) {
                counterTime = 15;
                int firstNum = new Random().nextInt(999 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(999 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(999 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum;
                finalEquation = equation.replaceAll("X", "*");


            } else if (35 <= currentLevel && currentLevel < 40) {
                counterTime = 15;
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                equation = firstNum + signs[2] + nextNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (40 <= currentLevel && currentLevel < 50) {
                counterTime = 15;
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[2] + nextNum + signs[0] + thirdNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (50 <= currentLevel && currentLevel < 55) {
                counterTime = 15;
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                equation = firstNum + signs[3] + nextNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (55 <= currentLevel && currentLevel < 65) {
                counterTime = 20;
                int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(3 - 2 + 1) + 2] + nextNum + signs[new Random().nextInt(1)] + thirdNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (65 <= currentLevel && currentLevel < 75) {
                counterTime = 20;
                int firstNum = new Random().nextInt(99 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(99 - 1 + 1) + 1;
                int fourthNum = new Random().nextInt(999 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum + signs[new Random().nextInt(1)] + fourthNum;
                finalEquation = equation.replaceAll("X", "*");

            } else if (75 <= currentLevel && currentLevel < 85) {
                counterTime = 30;
                int firstNum = new Random().nextInt(999 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(9 - 1 + 1) + 1;
                int fourthNum = new Random().nextInt(9 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum + signs[new Random().nextInt(3 - 2 + 1) + 2] + fourthNum;
                finalEquation = equation.replaceAll("X", "*");
            } else if (85 <= currentLevel && currentLevel <= 100) {
                counterTime = 35;
                int firstNum = new Random().nextInt(999 - 1 + 1) + 1;
                int nextNum = new Random().nextInt(999 - 1 + 1) + 1;
                int thirdNum = new Random().nextInt(99 - 1 + 1) + 1;
                int fourthNum = new Random().nextInt(99 - 1 + 1) + 1;
                equation = firstNum + signs[new Random().nextInt(1)] + nextNum + signs[new Random().nextInt(1)] + thirdNum + signs[new Random().nextInt(3 - 2 + 1) + 2] + fourthNum;
                finalEquation = equation.replaceAll("X", "*");
            }
        } else if (mGameType.equals("+")) {
            counterTime = 15;
            int firstNum = new Random().nextInt(20 - 1 + 1) + 1;
            int nextNum = new Random().nextInt(20 - 1 + 1) + 1;
            equation = firstNum + signs[0] + nextNum;
            finalEquation = equation.replaceAll("X", "*");
        } else if (mGameType.equals("-")) {
            counterTime = 15;
            int firstNum = new Random().nextInt(20 - 1 + 1) + 1;
            int nextNum = new Random().nextInt(20 - 1 + 1) + 1;
            equation = firstNum + signs[1] + nextNum;
            finalEquation = equation.replaceAll("X", "*");
        } else if (mGameType.equals("x")) {
            counterTime = 15;
            int firstNum = new Random().nextInt(12 - 1 + 1) + 1;
            int nextNum = new Random().nextInt(12 - 1 + 1) + 1;
            equation = firstNum + signs[2] + nextNum;
            finalEquation = equation.replaceAll("X", "*");
        } else if (mGameType.equals("÷")) {
            counterTime = 15;
            int firstNum = new Random().nextInt(9 - 1 + 1) + 1;
            int nextNum = new Random().nextInt(9 - 1 + 1) + 1;
            int startNum = firstNum * nextNum;
            equation = startNum + signs[3] + nextNum;
            finalEquation = equation.replaceAll("X", "*");
        } else if (mGameType.equals("mix")) {
            counterTime = 20;
            int firstNum = 0;
            int nextNum = 0;
            String sign = signs[new Random().nextInt(3)];
            Log.e("SIGN", "sign: " + sign);

            String sign0 = signs[0];
            String sign1 = signs[1];
            String sign2 = signs[2];
            String sign3 = signs[3];


            if (sign == sign0 || sign == sign1) {
                Log.e("SIGN IS + OR -", "sign: " + sign);
                firstNum = new Random().nextInt(99 - 1 + 1) + 1;
                nextNum = new Random().nextInt(99 - 1 + 1) + 1;
            } else if (sign == sign2) {
                Log.e("SIGN IS X", "sign: " + sign);
                firstNum = new Random().nextInt(12 - 1 + 1) + 1;
                nextNum = new Random().nextInt(12 - 1 + 1) + 1;
            } else if (sign == sign3) {
                Log.e("SIGN IS /", "sign: " + sign);
                int temp = new Random().nextInt(99 - 1 + 1) + 1;
                nextNum = new Random().nextInt(99 - 1 + 1) + 1;
                firstNum = temp * nextNum;
            }

            equation = firstNum + sign + nextNum;
            finalEquation = equation.replaceAll("X", "*");
        }
        tempCounter = counterTime;


        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("rhino");
//        String foo = "40+2";
        try {
            Double d = Double.parseDouble(engine.eval(finalEquation).toString());
            trueAnswer = Double.parseDouble(decimalFormat.format(d));
            Log.e("EVALUATION", "num: " + trueAnswer);
        } catch (ScriptException e) {
            e.printStackTrace();
        }


        Double[] myAnswers = getAnswers(trueAnswer);


        mEquationHeader.setText(equation);
        mAnsOne.setText(decimalFormat.format(myAnswers[0]));
        mAnsTwo.setText(decimalFormat.format(myAnswers[1]));
        mAnsThree.setText(decimalFormat.format(myAnswers[2]));
        mAnsFour.setText(decimalFormat.format(myAnswers[3]));

        mCardOne.setOnClickListener(this);
        mCardTwo.setOnClickListener(this);
        mCardThree.setOnClickListener(this);
        mCardFour.setOnClickListener(this);

        if (adsCounter >= 10) {
            if (mInterstitialAd.isLoaded()) {
                tempCounter = counterTime;
                countDown = false;
                adsCounter = 1;
                mInterstitialAd.show();
            }

        } else {
            adsCounter = adsCounter + 1;
            new MyThread().start();
            System.out.println("Counter: " + adsCounter);
        }


    }


    private void setActiveView(EasyFlipView.FlipState state) {
        if (state == EasyFlipView.FlipState.FRONT_SIDE) {
            currentView = 0;
        } else if (state == EasyFlipView.FlipState.BACK_SIDE) {
            currentView = 1;
        }
    }

    private void initViews() {
        if (currentView == 0) {
            mEquationHeader = findViewById(R.id.equation);
            mAnsOne = findViewById(R.id.ans_one);
            mAnsTwo = findViewById(R.id.ans_two);
            mAnsThree = findViewById(R.id.ans_three);
            mAnsFour = findViewById(R.id.ans_four);
            mCounter = findViewById(R.id.counter_text);

            mCardOne = findViewById(R.id.card_one);
            mCardTwo = findViewById(R.id.card_two);
            mCardThree = findViewById(R.id.card_three);
            mCardFour = findViewById(R.id.card_four);

            QuestionProgress = findViewById(R.id.progressBar);
            mProgressText = findViewById(R.id.progress_text);

            mScoreText = findViewById(R.id.score);

            mHighScoreText = findViewById(R.id.high_score);


        } else if (currentView == 1) {
            mEquationHeader = findViewById(R.id.back_equation);
            mAnsOne = findViewById(R.id.back_ans_one);
            mAnsTwo = findViewById(R.id.back_ans_two);
            mAnsThree = findViewById(R.id.back_ans_three);
            mAnsFour = findViewById(R.id.back_ans_four);
            mCounter = findViewById(R.id.back_counter_text);

            mCardOne = findViewById(R.id.back_card_one);
            mCardTwo = findViewById(R.id.back_card_two);
            mCardThree = findViewById(R.id.back_card_three);
            mCardFour = findViewById(R.id.back_card_four);

            QuestionProgress = findViewById(R.id.back_progressBar);
            mProgressText = findViewById(R.id.back_progress_text);

            mScoreText = findViewById(R.id.back_score);

            mHighScoreText = findViewById(R.id.back_high_score);
        }

        resetButtons();


        if (!mGameType.equals("levels_type")) {
            mProgressText.setVisibility(View.GONE);
            QuestionProgress.setVisibility(View.GONE);
            mScoreText.setVisibility(View.VISIBLE);
            mHighScoreText.setVisibility(View.VISIBLE);
        } else {
            mProgressText.setVisibility(View.VISIBLE);
            QuestionProgress.setVisibility(View.VISIBLE);
            mScoreText.setVisibility(View.GONE);
            mHighScoreText.setVisibility(View.GONE);
        }

        if (mGameType.equals("levels_type")) {
            QuestionProgress.setProgress(progress);
            mProgressText.setText("" + progress);
        } else {
            mScoreText.setText("score: " + points);
        }

        if (points > highScore){
            mHighScoreText.setText("best\n"+ points);
            mHighScoreText.setTextColor(Color.GREEN);
        }else {
            mHighScoreText.setText("best\n"+ highScore);
            mHighScoreText.setTextColor(Color.BLACK);
        }

        userRewarded = false;

    }

    private Double[] getAnswers(double answer) {
        String tempEnd = "9";
        String tempStart = "1";

        int answerLength = 1;
        if (decimalFormat.format(answer).contains(".")) {
            answerLength = decimalFormat.format(answer).length() - 2;
        } else {
            answerLength = decimalFormat.format(answer).length();
        }

        for (int i = 1; i < answerLength; i++) {
            tempEnd = tempEnd + "9";
            tempStart = tempStart + "0";
        }

        int max = Integer.parseInt(tempEnd);
        int min = Integer.parseInt(tempStart);


        Double a1 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        while (a1.equals(answer)) {
            a1 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        }
        Double a2 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        while (a1.equals(a2) || a2.equals(answer)) {
            a2 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        }

        Double a3 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        while (a3.equals(a1) || a3.equals(a2) || a3.equals(answer)) {
            a3 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        }

        Double a4 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        while (a4.equals(a3) || a4.equals(a1) || a4.equals(a2) || a4.equals(answer)) {
            a4 = Double.valueOf(new Random().nextInt(max - min + 1) + min);
        }


        if (decimalFormat.format(answer).contains(".")) {
            a1 = min + (max - min) * new Random().nextDouble();
            while (a1.equals(answer)) {
                a1 = min + (max - min) * new Random().nextDouble();
            }
            a2 = min + (max - min) * new Random().nextDouble();
            while (a1.equals(a2) || a2.equals(answer)) {
                a2 = min + (max - min) * new Random().nextDouble();
            }

            a3 = min + (max - min) * new Random().nextDouble();
            while (a3.equals(a1) || a3.equals(a2) || a3.equals(answer)) {
                a3 = min + (max - min) * new Random().nextDouble();
            }

            a4 = min + (max - min) * new Random().nextDouble();
            while (a4.equals(a3) || a4.equals(a1) || a4.equals(a2) || a4.equals(answer)) {
                a4 = min + (max - min) * new Random().nextDouble();
            }

        }

        Double[] answersList = {Double.valueOf(a1), Double.valueOf(a2), Double.valueOf(a3), Double.valueOf(a4)};

        int ansPlace = new Random().nextInt(3);
        answersList[ansPlace] = answer;

        return answersList;
    }


    @Override
    public void onClick(View view) {
        TextView myText = null;
        final CardView pressedButton = (CardView) view;
        switch (view.getId()) {
            case R.id.card_one:
                myText = findViewById(R.id.ans_one);
                break;
            case R.id.back_card_one:
                myText = findViewById(R.id.back_ans_one);
                break;
            case R.id.card_two:
                myText = findViewById(R.id.ans_two);
                break;
            case R.id.back_card_two:
                myText = findViewById(R.id.back_ans_two);
                break;
            case R.id.card_three:
                myText = findViewById(R.id.ans_three);
                break;
            case R.id.back_card_three:
                myText = findViewById(R.id.back_ans_three);
                break;
            case R.id.card_four:
                myText = findViewById(R.id.ans_four);
                break;
            case R.id.back_card_four:
                myText = findViewById(R.id.back_ans_four);
                break;
        }

        Double currentAns = Double.parseDouble(myText.getText().toString());

        Log.e("Answer Comparison", "True:" + trueAnswer + " , Current: " + currentAns);

        if (currentAns.equals(trueAnswer)) {
            pressedButton.setCardBackgroundColor(Color.GREEN);
//            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
            if (isMusicOn) {
                new Utils().correctSound(getApplicationContext());
            }
            countDown = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedButton.setCardBackgroundColor(Color.WHITE);
                    if (mGameType.equals("levels_type")) {
                        if (correctAnswers < 5) {
                            flipView.flipTheView();
                            correctAnswers++;
                            progress = progress + 1;
                            QuestionProgress.setProgress(progress);
                            mProgressText.setText("" + progress);
                        } else {
                            PlayerWon();
//                        startActivity(new Intent(getApplicationContext() , LevelsActivity.class));
//                        finish();
                        }
                    } else {
                        points = points + 1;
                        flipView.flipTheView();
                    }

                }
            }, 1000);
        } else {
            if (isMusicOn) {
                new Utils().wrongSound(this);
            }
            pressedButton.setCardBackgroundColor(Color.RED);
            if (!gotChance){
                startReward();
                return;
            }

            if (points >= 2){
                int tempPoints = points;
                points = points - 2;
                mScoreText.setText("score: "+ tempPoints + "-2");
                mScoreText.setTextColor(Color.RED);
            }
            getTheCorrectAnswer();
            if (!mGameType.equals("levels_type")) {
//                Snackbar.make(findViewById(android.R.id.content) , "Answer is: " + decimalFormat.format(trueAnswer) , Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pressedButton.setCardBackgroundColor(Color.WHITE);
                        progress = 0;
                        correctAnswers = 0;
                        mScoreText.setTextColor(Color.BLACK);
                        mScoreText.setText("score: "+points);
                        flipView.flipTheView();
                    }
                }, 3000);
                return;
            }
            GameOver();
        }

    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while (countDown) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCounter.setText("" + counterTime);
                    }
                });

                SystemClock.sleep(1000);
                if (counterTime > 0) {
                    counterTime--;
                } else {
                    countDown = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!gotChance){
                                startReward();
                                return;
                            }
                            if (points >= 2){
                                int tempPoints = points;
                                points = points - 2;
                                mScoreText.setText("score: "+ tempPoints + "-2");
                                mScoreText.setTextColor(Color.RED);
                            }
                            getTheCorrectAnswer();
                            if (!mGameType.equals("levels_type")) {
//                                Snackbar.make(findViewById(android.R.id.content) , "Answer is: " + decimalFormat.format(trueAnswer) , Snackbar.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        pressedButton.setCardBackgroundColor(Color.WHITE);
                                        progress = 0;
                                        correctAnswers = 0;
                                        mScoreText.setTextColor(Color.BLACK);
                                        mScoreText.setText("score: "+ points);
                                        flipView.flipTheView();
                                    }
                                }, 3000);
                                return;
                            }
                            GameOver();

                        }
                    });

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDown = false;
        int highScore = getSharedPreferences("PRE" , MODE_PRIVATE).getInt("top_score" , 0);
        if (points > highScore){
            getSharedPreferences("PRE" ,MODE_PRIVATE).edit().putInt("top_score" , points).commit();
        }

    }

    private void GameOver() {
        if (isMusicOn) {
            new Utils().gameOverSound(getApplicationContext());
        }

        final MyReturnClass returnClass = new Utils().showGameOverDialog(QuestionsActivity.this);
        CardView[] list = returnClass.getmMyButtons();
        CardView btn1 = list[0];
        CardView btn2 = list[1];
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = 0;
                correctAnswers = 0;
                if (isMusicOn) {
                    new Utils().clickSound(getApplicationContext());
                }
                new Utils().dismissDialog(returnClass.getmMyDialog());
                flipView.flipTheView();

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMusicOn) {
                    new Utils().clickSound(getApplicationContext());
                }
                new Utils().dismissDialog(returnClass.getmMyDialog());
                finish();
            }
        });
    }

    private void PlayerWon() {
        if (isMusicOn) {
            new Utils().completionSound(QuestionsActivity.this);
        }
        progress = 0;
        correctAnswers = 0;
        int lastLevel = getSharedPreferences("PRE", Context.MODE_PRIVATE).getInt("last_level", 1);
        if (currentLevel == lastLevel) {
            int newLevel = lastLevel + 1;
            getSharedPreferences("PRE", MODE_PRIVATE).edit().putInt("last_level", newLevel).commit();
            int lastLevelF = getSharedPreferences("PRE", Context.MODE_PRIVATE).getInt("last_level", 1);
            Log.e("LAST LEVEL", "" + lastLevelF);
        }

//        LevelsActivity.mLevelsActivity.finish();
        currentLevel = currentLevel + 1;
        final MyReturnClass returnClass = new Utils().showCompletionDialog(QuestionsActivity.this);
        CardView[] list = returnClass.getmMyButtons();
        CardView btn1 = list[0];
        CardView btn2 = list[1];
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMusicOn) {
                    new Utils().clickSound(getApplicationContext());
                }
                new Utils().dismissDialog(returnClass.getmMyDialog());
                flipView.flipTheView();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMusicOn) {
                    new Utils().clickSound(getApplicationContext());
                }
                new Utils().dismissDialog(returnClass.getmMyDialog());
                finish();
            }
        });
    }


    private void getTheCorrectAnswer() {
        TextView[] answers = {mAnsOne, mAnsTwo, mAnsThree, mAnsFour};
        final CardView[] cards = {mCardOne, mCardTwo, mCardThree, mCardFour};

        for (int i = 0; i < answers.length; i++) {
            TextView myText = answers[i];
            Double currentAns = Double.parseDouble(myText.getText().toString());
            if (currentAns.equals(trueAnswer)) {
                CardView cardView = cards[i];
                cardView.setCardBackgroundColor(Color.GREEN);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < cards.length; i++) {
                    CardView cardView = cards[i];
                    cardView.setCardBackgroundColor(Color.WHITE);
                }
            }
        }, 2000);
    }

    private void startReward() {
        countDown = false;
        gotChance = true;
        AlertDialog dialog = new AlertDialog.Builder(QuestionsActivity.this)
                .setTitle("Second Chance").setMessage("Watch ad for second chance ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (rewardedAd.isLoaded()) {
                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                @Override
                                public void onRewardedAdOpened() {
                                    // Ad opened.
                                }

                                @Override
                                public void onRewardedAdClosed() {
                                    // Ad closed.
                                    rewardedAd = createAndLoadRewardedAd();
                                    if (userRewarded){
//                                        userRewarded = false;
                                    }else {
                                        resetButtons();
                                        if (points >= 2){
                                            int tempPoints = points;
                                            points = points - 2;
                                            mScoreText.setText("score: "+ tempPoints + "-2");
                                            mScoreText.setTextColor(Color.RED);
                                        }

                                        getTheCorrectAnswer();
                                        if (!mGameType.equals("levels_type")) {
//                                Snackbar.make(findViewById(android.R.id.content) , "Answer is: " + decimalFormat.format(trueAnswer) , Snackbar.LENGTH_SHORT).show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
//                                        pressedButton.setCardBackgroundColor(Color.WHITE);
                                                    progress = 0;
                                                    correctAnswers = 0;
                                                    mScoreText.setTextColor(Color.BLACK);
                                                    mScoreText.setText("score: "+ points);
                                                    flipView.flipTheView();
                                                }
                                            }, 3000);
                                            return;
                                        }
                                        GameOver();
                                    }

                                }

                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem reward) {
                                    // User earned reward.
                                    userRewarded = true;


                                }

                                @Override
                                public void onRewardedAdFailedToShow(int errorCode) {
                                    // Ad failed to display
                                }
                            };
                            rewardedAd.show(QuestionsActivity.this, adCallback);
                        } else {
                            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (points >= 2){
                            points = points - 2;
                            mScoreText.setText("score: "+ points);
                            mScoreText.setTextColor(Color.RED);
                        }

                        getTheCorrectAnswer();
                        if (!mGameType.equals("levels_type")) {
//                                Snackbar.make(findViewById(android.R.id.content) , "Answer is: " + decimalFormat.format(trueAnswer) , Snackbar.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                        pressedButton.setCardBackgroundColor(Color.WHITE);
                                    progress = 0;
                                    correctAnswers = 0;
                                    mScoreText.setTextColor(Color.BLACK);
                                    flipView.flipTheView();
                                }
                            }, 3000);
                            return;
                        }
                        GameOver();
                    }
                })
                .setCancelable(false).create();

        if (rewardedAd.isLoaded()){
            try{
                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            if (points >= 2){
                points = points - 2;
                mScoreText.setText("score: "+ points);
                mScoreText.setTextColor(Color.RED);
            }

            getTheCorrectAnswer();
            if (!mGameType.equals("levels_type")) {
//                                Snackbar.make(findViewById(android.R.id.content) , "Answer is: " + decimalFormat.format(trueAnswer) , Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                                        pressedButton.setCardBackgroundColor(Color.WHITE);
                        progress = 0;
                        correctAnswers = 0;
                        mScoreText.setTextColor(Color.BLACK);
                        flipView.flipTheView();
                    }
                }, 3000);
                return;
            }
            GameOver();
        }
    }


    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
                getString(R.string.admob_reward_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }

    private void resetButtons(){
        final CardView[] cards = {mCardOne, mCardTwo, mCardThree, mCardFour};
        for (int i = 0; i < cards.length; i++) {
            CardView cardView = cards[i];
            cardView.setCardBackgroundColor(Color.WHITE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (countDown){
            countDown = false;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!countDown && !userRewarded){
            countDown = true;
            new MyThread().start();
        }

    }


    @Override
    public void onBackPressed() {
        if (countDown){
            countDown = false;
        }
        new android.app.AlertDialog.Builder(QuestionsActivity.this).setTitle("Are you sure !")
                .setMessage("You want to exit in game play ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!countDown){
                            countDown = true;
                            new MyThread().start();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }
}
