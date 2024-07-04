package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.SharedPref;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button[][] buttons = new Button[4][4];
    private List<String> values = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();
    private SharedPref sharedPref;


    {
        for (int i = 1; i < 16; i++) {
            numbers.add("" + i);
        }
    }

    private int x = 3;
    private int y = 3;

    private int sound = 0;


    private Chronometer chronometer;

    private int count = 0;

    MediaPlayer mediaController;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = SharedPref.getInstance(this);

        initData();
        shuffle();
        initViews();
        loadData();
        mediaController = MediaPlayer.create(this, R.raw.vois);

        if (sharedPref.getMus()){
            mediaController.start();
            Log.d("TTT", "onCreate if: " + sharedPref.getMus());
        }

        Log.d("TTT", "onCreate: " + sharedPref.getMus());


        ImageView imageView1 = findViewById(R.id.mut);
        ImageView imageView2 = findViewById(R.id.speaker);

        imageView1.setOnClickListener(view -> {
            imageView2.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            mediaController.start();
            sharedPref.saveMus(true);
            Log.d("TTT", "onCreate if2: " + sharedPref.getMus());
        });

        imageView2.setOnClickListener(view -> {
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            mediaController.pause();
            sharedPref.saveMus(false);
            Log.d("TTT", "onCreateif3: " + sharedPref.getMus());

        });

        mediaController = MediaPlayer.create(this, R.raw.vois);



        ImageView imageView = (ImageView) findViewById(R.id.ref);
        imageView.setOnClickListener(view1 -> {
            buttons[x][y].setVisibility(View.VISIBLE);
            x = 3;
            y = 3;
            initData();
            shuffle();
            loadData();
            count = 0;


            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();

            ((TextView) findViewById(R.id.moveId)).setText(String.valueOf(count));
        });

        findViewById(R.id.back).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, First.class);
            startActivity(intent);
            finish();
        });


        chronometer = findViewById(R.id.tmId);
        chronometer.start();

        TextView textView = findViewById(R.id.btId);
        textView.setText(sharedPref.getBestMove1().toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaController.stop();
    }


    private void initViews() {
        RelativeLayout containButtons = findViewById(R.id.container);

        for (int i = 0; i < containButtons.getChildCount(); i++) {

            int currentX = i / 4;
            int currentY = i % 4;

            Button currentButton = (Button) containButtons.getChildAt(i);

            buttons[currentX][currentY] = currentButton;
            System.out.println(i + " " + values.get(i));
            buttons[currentX][currentY].setText(values.get(i));
            currentButton.setOnClickListener(this::onClick);
            currentButton.setTag(new Point(currentX, currentY));
        }
    }

    private void loadData() {
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4].setText(values.get(i));
        }

        if (sharedPref.getState().isEmpty()) {
            buttons[x][y].setVisibility(View.INVISIBLE);
        }
    }

    private void shuffle() {
        int[][] arr = new int[4][4];
        int index;
        int templ = 0;
        do {
            Collections.shuffle(numbers);
            index = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (!(i == 3 && j == 3)) arr[i][j] = Integer.parseInt(numbers.get(index++));
                    else arr[i][j] = 0;
                }
            }
        } while (!isSolvable(arr));
        values = new ArrayList<>(numbers);
        values.add(" ");
    }


    private void initData() {
        for (int i = 1; i < 16; i++) {
            values.add(String.valueOf(i));
        }
    }


    private void onClick(View view) {
        Button clickButton = (Button) view;
        Point currentPoint = (Point) clickButton.getTag();
        boolean canMove = false;
        if (x == currentPoint.getX() && Math.abs(y - currentPoint.getY()) == 1) canMove = true;
        else if (y == currentPoint.getY() && Math.abs(x - currentPoint.getX()) == 1) canMove = true;

        if (canMove) {
            buttons[x][y].setVisibility(View.VISIBLE);
            buttons[x][y].setText(clickButton.getText());
            clickButton.setText("");
            x = currentPoint.getX();
            y = currentPoint.getY();
            count++;
            buttons[x][y].setVisibility(View.INVISIBLE);
            TextView textView = findViewById(R.id.moveId);
            textView.setText(String.valueOf(count));
        }

        if (x == 3 && y == 3) checkWin();
    }


    private void checkWin() {
        RelativeLayout containerButton = findViewById(R.id.container);

        for (int i = 0; i < 15; i++) {
            Button currentButton = (Button) containerButton.getChildAt(i);

            if (!currentButton.getText().equals(String.valueOf(i + 1))) return;

        }


        chronometer.stop();
        mediaController.stop();


        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_win);
        dialog.setCancelable(false);
        Chronometer chronometer1 = dialog.findViewById(R.id.timeIdWiner);

        chronometer1.setBase(chronometer.getBase());

        ((TextView) dialog.findViewById(R.id.moveId)).setText(String.valueOf(count));

        dialog.findViewById(R.id.ref).setOnClickListener(view1 -> {
            x = 3;
            y = 3;
            initData();
            shuffle();
            loadData();
            count = 0;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            if (sharedPref.getMus()){

                mediaController.start();
            }
            dialog.cancel();
        });


        dialog.show();

        x = 3;
        y = 3;


        if (sharedPref.getBestMove1() == 0 || sharedPref.getBestMove1() > count) {
            sharedPref.saveBestMove1(count);
        } else if (sharedPref.getBestMove2() == 0 || sharedPref.getBestMove1() < count && sharedPref.getBestMove2() > count) {
            sharedPref.saveBestMove2(count);
        } else if (sharedPref.getBestMove3() == 0 || sharedPref.getBestMove2() < count && sharedPref.getBestMove3() > count)

            count = 0;

        TextView textView = findViewById(R.id.btId);

        textView.setText(String.valueOf(sharedPref.getBestMove1()));

        chronometer.setBase(0L);
    }


    static final int N = 4;


    static int getInvCount(int[] arr) {
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                // count pairs(arr[i], arr[j]) such that
                // i < j but arr[i] > arr[j]
                if (arr[j] != 0 && arr[i] != 0 && arr[i] > arr[j]) inv_count++;
            }
        }
        return inv_count;
    }

    // find Position of blank from bottom
    static int findXPosition(int[][] puzzle) {
        // start from bottom-right corner of matrix
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (puzzle[i][j] == 0) return N - i;
        return -1;
    }

    // This function returns true if given
    // instance of N*N - 1 puzzle is solvable
    static boolean isSolvable(int[][] puzzle) {
        // Count inversions in given puzzle
        int[] arr = new int[N * N];
        int k = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[k++] = puzzle[i][j];

        int invCount = getInvCount(arr);

        // If grid is odd, return true if inversion
        // count is even.
        if (N % 2 == 1) return invCount % 2 == 0;
        else // grid is even
        {
            int pos = findXPosition(puzzle);
            if (pos % 2 == 1) return invCount % 2 == 0;
            else return invCount % 2 == 1;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("TIMER", chronometer.getBase());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        long timer = savedInstanceState.getLong("TIMER");
        chronometer.setBase(timer);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (buttons[i / 4][i % 4].getText().equals("")) sb.append("0").append("#");
            else sb.append(buttons[i / 4][i % 4].getText()).append("#");
        }
        sharedPref.saveState(sb.toString());
        sharedPref.setScore(count);
        chronometer.stop();
        mediaController.pause();
        sharedPref.saveCurrentTime(SystemClock.elapsedRealtime() - chronometer.getBase());

    }

    @Override
    protected void onResume() {
        String[] str = sharedPref.getState().split("#");

        if (str.length == 16) {
            for (int i = 0; i < 16; i++) {
                if (str[i].equals("0")) {
                    Log.d("TTT", "onResume:" + str[i]);
                    buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
                    buttons[i / 4][i % 4].setText("");
                    x = i / 4;
                    y = i % 4;
                } else {
                    buttons[i / 4][i % 4].setText(str[i]);
                }
            }
            if (x != 3 && y != 3) {
                buttons[3][3].setVisibility(View.VISIBLE);
            }
        }

        count = sharedPref.getScore();
        ((TextView) findViewById(R.id.moveId)).setText(String.valueOf(count));

        chronometer.setBase(SystemClock.elapsedRealtime() - sharedPref.getTime());
        chronometer.start();


        ImageView volume = findViewById(R.id.speaker);
        ImageView mute = findViewById(R.id.mut);

        if (sharedPref.getMus()) {
            mute.setVisibility(View.INVISIBLE);
            volume.setVisibility(View.VISIBLE);
            mediaController.start();
        } else {
            volume.setVisibility(View.INVISIBLE);
            mute.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }
}