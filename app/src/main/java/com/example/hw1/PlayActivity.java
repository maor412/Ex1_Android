package com.example.hw1;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {


    private ImageView rightButton,leftButton, car;
    private ImageView[] hearts;
    private RelativeLayout.LayoutParams carPosition;
    private int carPositionNum;
    private TableLayout rocks;
    private Timer playTimer;
    private Timer speedTimer;
    private int counter;
    private int accidentCount;
    private Random random ;
    private final int HEARTS_NUM = 3;
    private final int COLS = 3; // number of cols
    private final int RATE = 3; // rate to add rocks in screen
    private final int ROWS = 8; // rows number
    private int fallSpeed = 500; // speed of rocks move
    boolean toCreate; //true = creating new rock
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_play);
        InitializeVariables();
        FindViews();
        InitVeiws();

    }

    private void SpeedController(){
        speedTimer = new Timer();
        speedTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                fallSpeed -= 10;
            }

        }, 0, 2000);
    }


    private void GenerateRocks() {
        playTimer = new Timer();
        playTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, fallSpeed);
    }


    private void TimerMethod() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                //This method runs in the same thread as the UI.

                if(counter % RATE == 0 ) {

                    showRock(0, getRandomRockPos());

                }

                updateRocks();
                checkCrash();
                counter++;
            }
        });
    }

    private void updateRocks() {
        //update rocks location
        for(int i = counter%RATE; i < rocks.getChildCount(); i+=RATE){
            TableRow row = (TableRow) rocks.getChildAt(i);
            for(int j = 0 ; j < row.getChildCount(); j++){
                ImageView img = (ImageView) row.getChildAt(j);
                //image visible then invisible and visible the image below it
                if(img.getVisibility() == View.VISIBLE){
                    img.setVisibility(View.INVISIBLE);
                    if(i + 1 < rocks.getChildCount())
                        showRock(i+1, j);
                }
            }
        }
    }

    private void checkCrash() {
        //check if car crashed
        TableRow row = (TableRow) rocks.getChildAt(ROWS-2);

        for(int i = 0; i < row.getChildCount(); i++){
            ImageView img = (ImageView) row.getChildAt(i);
            //car is crashed
            if(img.getVisibility() == View.VISIBLE && carPositionNum == i+1){
                accidentCount += 1;
                // if still have hears decrement by one
                if(accidentCount < HEARTS_NUM) {
                    playSound(R.raw.crash_sound);// play crash sound
                    hearts[HEARTS_NUM - accidentCount].setVisibility(View.INVISIBLE);
                }
                else{
                    // no hears left then game over
                    Toast.makeText(getApplicationContext(),"Game Over", Toast.LENGTH_SHORT).show();
                    playTimer.cancel();
                    playSound(R.raw.game_over_sound);//play game over sound
                    goBack();//finish the game
                }
            }
        }

    }

    public void playSound(int soundId){
        //play sound effect
        MediaPlayer mPlayer = MediaPlayer.create(PlayActivity.this, soundId);
        mPlayer.start();
    }

    private void goBack() {
        // finish the activity screen when the game is over
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }


    private void showRock(int i, int j) {
        // visible rock image in i,j index
        TableRow row = (TableRow) rocks.getChildAt(i);
        ImageView img  = (ImageView) row.getChildAt(j);
        img.setVisibility(View.VISIBLE);
    }


    public int getRandomRockPos(){
        //get random number of cols
        return random.nextInt(COLS);
    }

    private void InitializeVariables() {
        carPositionNum = 2; // 1 - leftSide, 2 - center, 3 - rightSide
        toCreate = true;
        counter = 0;
        random = new Random();
        hearts = new ImageView[HEARTS_NUM];
        accidentCount = 0;

    }

    private void InitVeiws() {
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(carPositionNum != 3) {
                    JumpRight();
                }
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(carPositionNum != 1) {
                    JumpLeft();
                }
            }
        });

    }

    private void JumpLeft() {
        if(carPositionNum == 2){
            carPosition = new RelativeLayout.LayoutParams(210,250);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            carPosition.leftMargin = 100;
            car.setLayoutParams(carPosition);
            carPositionNum = 1;

        }else if(carPositionNum == 3){
            carPosition = new RelativeLayout.LayoutParams(210,250);
            carPosition.addRule(RelativeLayout.CENTER_HORIZONTAL);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            car.setLayoutParams(carPosition);
            carPositionNum = 2;
        }
    }

    private void JumpRight() {
        if(carPositionNum == 2){
            carPosition = new RelativeLayout.LayoutParams(210,250);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            carPosition.rightMargin = 100;
            car.setLayoutParams(carPosition);
            carPositionNum = 3;

        }else if(carPositionNum == 1){
            carPosition = new RelativeLayout.LayoutParams(210,250);
            carPosition.addRule(RelativeLayout.CENTER_HORIZONTAL);
            carPosition.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            car.setLayoutParams(carPosition);
            carPositionNum = 2;
        }
    }

    private void FindViews() {
        rightButton = (ImageView)findViewById(R.id.Button_right);// Initialize right button
        leftButton = (ImageView)findViewById(R.id.Button_left);// Initialize left button
        car = (ImageView)findViewById(R.id.car);//Initialize car
        rocks = findViewById(R.id.Table_rocks);
        hearts[0] = findViewById(R.id.heart1);
        hearts[1] = findViewById(R.id.heart2);
        hearts[2] = findViewById(R.id.heart3);

    }

    @Override
    protected void onPause() {
        super.onPause();
        playTimer.cancel();
        speedTimer.cancel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        GenerateRocks();
        SpeedController();
    }
}