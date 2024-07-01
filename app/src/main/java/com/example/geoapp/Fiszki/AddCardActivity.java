package com.example.geoapp.Fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.R;

public class AddCardActivity extends AppCompatActivity {

    private EditText questionEditText, answerEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });
    }

    private void saveCard() {
        String question = questionEditText.getText().toString().trim();
        String answer = answerEditText.getText().toString().trim();

        if (!question.isEmpty() && !answer.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("question", question);
            resultIntent.putExtra("answer", answer);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
