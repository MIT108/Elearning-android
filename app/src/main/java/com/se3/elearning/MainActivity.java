package com.se3.elearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.se3.elearning.student.StudentDashboard;

import java.util.Random;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class MainActivity extends AppCompatActivity {

    ImageView JoinToChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();

        Random generator = new Random();

        JoinToChat=(ImageView)findViewById(R.id.join);

        JoinToChat.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this, StudentDashboard.class);
            intent.putExtra("Name","Anonymous-"+generator.nextInt(9999));
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
            pulsator.stop();
        });
    }
}