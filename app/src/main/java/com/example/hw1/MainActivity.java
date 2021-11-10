package com.example.hw1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {

    ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

    //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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FindViews();
        Button b = findViewById(R.id.button_play);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gamePage = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(gamePage);
            }
        });
    }

//    private void FindViews() {
//        background = findViewById(R.id.welcome_page_backGround);
//        Glide.with(this)
//                .load("https://static.vecteezy.com/system/resources/previews/001/849/553/original/modern-gold-background-free-vector.jpg")
//                .into(background);
//    }


}