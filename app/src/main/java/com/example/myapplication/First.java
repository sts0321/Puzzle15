package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class First extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        LinearLayoutCompat layout = findViewById(R.id.ply);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(First.this, MainActivity.class);
                startActivity(main);
            }
        });


        findViewById(R.id.statistic).setOnClickListener(view -> {
            Intent intent = new Intent(this, Statistic.class);
            startActivity(intent);
        });

        LinearLayout layout1 = findViewById(R.id.info);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(First.this, Info.class);
                startActivity(main);
            }
        });
        findViewById(R.id.exit).setOnClickListener(view -> {


            finish();

        });


    }


}