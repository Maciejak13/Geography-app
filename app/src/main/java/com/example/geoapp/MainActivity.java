package com.example.geoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.Achievements.AchievementsActivity;
import com.example.geoapp.Fiszki.fiszkiwybor;
import com.example.geoapp.Quiz.ChooseActivity;
import com.example.geoapp.QuizResults.QuizResultsActivity;
import com.example.geoapp.Ranking.RankingActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quizActivity();
        fiszkiActivity();
        AchievActivity();
        StatsActivity();
        quizRanking();
        WylogujActivity();
    }
    private void quizRanking() {
        Button quizButton = findViewById(R.id.btnQuizDodaj);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RankingActivity.class));
            }
        });
    }
    private void quizActivity() {
        Button quizButton = findViewById(R.id.quiz);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChooseActivity.class));
            }
        });
    }
    private void fiszkiActivity() {
        Button quizButton = findViewById(R.id.fiszki);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, fiszkiwybor.class));
            }
        });
    }
    private void AchievActivity() {
        Button quizButton = findViewById(R.id.Profil);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AchievementsActivity.class));
            }
        });
    }
    private void StatsActivity() {
        Button quizButton = findViewById(R.id.Stats);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QuizResultsActivity.class));
            }
        });
    }
    private void WylogujActivity() {
        Button quizButton = findViewById(R.id.Wyjscie);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, loginActivity.class));
            }
        });
    }
}

