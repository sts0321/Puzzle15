package com.example.myapplication;

import static com.example.myapplication.R.id;
import static com.example.myapplication.R.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.SharedPref;

public class Statistic extends AppCompatActivity {
    private SharedPref sharedPref;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_statistic);
        sharedPref = SharedPref.getInstance(this);

        ImageView imageView = findViewById(R.id.backStatistic);
        imageView.setOnClickListener(view -> {
            finish();
        });

        findViewById(id.new_game).setOnClickListener(view -> {
            Intent main = new Intent(Statistic.this, MainActivity.class);
            startActivity(main);
            finish();
        });

        TextView textView = findViewById(id.move_1);
        textView.setText(sharedPref.getBestMove1().toString());
        TextView textView2 = findViewById(id.move_2);
        textView2.setText(sharedPref.getBestMove2().toString());
        TextView textView3 = findViewById(id.move_3);
        textView3.setText(sharedPref.getBestMove3().toString());
    }
}