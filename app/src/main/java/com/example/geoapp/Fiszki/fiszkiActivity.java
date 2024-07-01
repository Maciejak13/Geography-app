package com.example.geoapp.Fiszki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.R;

import java.util.ArrayList;

public class fiszkiActivity extends AppCompatActivity {

    private TextView questionTextView, answerTextView;
    private Button showAnswerButton, nextCardButton, addCardButton;

    private ArrayList<Card> cardList = new ArrayList<>();
    private int currentCardIndex = 0;

    private static final int ADD_CARD_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiszki);

        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        nextCardButton = findViewById(R.id.nextCardButton);
        addCardButton = findViewById(R.id.addCardButton);

        initializeCards();

        showCurrentCard();

        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });

        nextCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextCard();
            }
        });

        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddCardActivity();
            }
        });
    }

    private void initializeCards() {
        // Przykładowe początkowe fiszki
        cardList.add(new Card("Jak Alaska znalazła się pod kontrola USA?", "Została kupiona od Rosji w 1867"));
        cardList.add(new Card("Jaka jest stolica Stanów Zjednoczonych Ameryki?", "Waszyngton"));
        cardList.add(new Card("Jaka jest stolica Kanady?", "Ottawa"));

    }

    private void showCurrentCard() {
        if (cardList.isEmpty()) {
            questionTextView.setText("Dodaj własne fiszki!");
            answerTextView.setText("");
        } else {
            Card currentCard = cardList.get(currentCardIndex);
            questionTextView.setText(currentCard.getQuestion());
            answerTextView.setText(""); // Resetuj odpowiedź
        }
    }

    private void showAnswer() {
        if (!cardList.isEmpty()) {
            Card currentCard = cardList.get(currentCardIndex);
            answerTextView.setText(currentCard.getAnswer());
        }
    }

    private void showNextCard() {
        if (!cardList.isEmpty()) {
            currentCardIndex = (currentCardIndex + 1) % cardList.size();
            showCurrentCard();
        }
    }

    private void startAddCardActivity() {
        Intent intent = new Intent(this, AddCardActivity.class);
        startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String question = data.getStringExtra("question");
            String answer = data.getStringExtra("answer");

            if (question != null && answer != null) {
                Card newCard = new Card(question, answer);
                cardList.add(newCard);
                currentCardIndex = cardList.size() - 1;
                showCurrentCard();
            }
        }
    }
}