package com.example.myapplication.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.SpreadBuilder;

public class SharedPref {
    private static SharedPreferences prf;
    private static SharedPref myPrf;

    private SharedPref() {
    }

    public static SharedPref getInstance(Context context) {
        if (myPrf == null) {
            prf = context.getSharedPreferences("puzzle_15", Context.MODE_PRIVATE);
            myPrf = new SharedPref();
        }
        return myPrf;
    }

    public void setScore(int countMove) {
        prf.edit().putInt("current_move", countMove).apply();
    }

    public void saveState(String state) {
        // stringBuilder
        prf.edit().putString("current_state", state).apply();
    }


    public String getState(){
        return prf.getString("current_state","");
    }

    public int getScore() {
        return prf.getInt("current_move", 0);
    }

    public void saveCurrentTime(Long time) {
        prf.edit().putLong("time", time).apply();
    }

    public Long getTime() {
        return prf.getLong("time", 0L);
    }


    public void saveBestMove1(Integer best1) {
        prf.edit().putInt("best_move1", best1).apply();
    }

    public Integer getBestMove1() {
        return prf.getInt("best_move1", 0);
    }

    public void saveBestMove2(Integer best2) {
        prf.edit().putInt("best_move2", best2).apply();
    }

    public Integer getBestMove2() {
        return prf.getInt("best_move2", 0);
    }

    public void saveBestMove3(Integer best3) {
        prf.edit().putInt("best_move3", best3).apply();
    }

    public Integer getBestMove3() {
        return prf.getInt("best_move3", 0);
    }

    public void saveMus(Boolean b){
        prf.edit().putBoolean("Music",b).apply();
    }

    public Boolean getMus(){
        return prf.getBoolean("Music",true);
    }

}
