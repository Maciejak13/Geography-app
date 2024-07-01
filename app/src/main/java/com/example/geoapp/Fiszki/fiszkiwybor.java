package com.example.geoapp.Fiszki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.geoapp.R;

public class fiszkiwybor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiszkiwybor);
ameryka_pln();

    }

    private void ameryka_pln(){
        Button quizButton = (Button) findViewById(R.id.amerykapln);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fiszkiwybor.this, fiszkiActivity.class));
            }
        });

    }
}