package com.example.geoapp.Quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.QuizResults.QuizResultManager;
import com.example.geoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private TextView correctAnswerTextView;
    private TextView correctAnswersCountTextView;
    private TextView countdownTextView;
    private DatabaseReference databaseReference;
    private List<DataSnapshot> questionSnapshots = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private int totalQuestions = 0;
    private String selectedDifficulty;
    private String selectedCategory;
    private CountDownTimer countDownTimer;
    private long startTime;
    private List<Long> timeTakenList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        correctAnswerTextView = findViewById(R.id.correctAnswerTextView);
        countdownTextView = findViewById(R.id.countdownTextView);

        selectedDifficulty = getIntent().getStringExtra("difficulty");
        selectedCategory = getIntent().getStringExtra("category");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("QUIZS");

        Query query = databaseReference.orderByChild("difficulty").equalTo(selectedDifficulty);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DataSnapshot> availableQuestions = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.child("category").getValue().toString();
                    if (category.equals(selectedCategory)) {
                        availableQuestions.add(snapshot);
                    }
                }
                totalQuestions = availableQuestions.size();
                if (totalQuestions > 0) {
                    for (int i = 0; i < 3; i++) { // Get 3 random questions
                        int randomIndex = new Random().nextInt(availableQuestions.size());
                        questionSnapshots.add(availableQuestions.get(randomIndex));
                        availableQuestions.remove(randomIndex);
                    }
                    displayQuestion(currentQuestionIndex);
                    startTimer();
                } else {
                    Toast.makeText(QuizActivity.this, "Sprwadź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizActivity.this, "Sprwadź połączenie z internetem." + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(1);
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(2);
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(3);
            }
        });
    }
    private void displayQuestion(int index) {
        DataSnapshot questionSnapshot = questionSnapshots.get(index);
        String question = questionSnapshot.child("question").getValue().toString();
        String option1 = questionSnapshot.child("option1").getValue().toString();
        String option2 = questionSnapshot.child("option2").getValue().toString();
        String option3 = questionSnapshot.child("option3").getValue().toString();
        String correctAnswer = questionSnapshot.child("answer_nr").getValue().toString();
        questionTextView.setText(question);
        OptionToButton(option1Button, option1, 1);
        OptionToButton(option2Button, option2, 2);
        OptionToButton(option3Button, option3, 3);
        correctAnswerTextView.setText("Poprawne odp: " + correctAnswer);
        correctAnswerTextView.setVisibility(View.GONE);

        startTime = System.currentTimeMillis();
    }
    private void checkAnswer(int selectedOption) {
        long timeTaken = System.currentTimeMillis() - startTime;
        timeTakenList.add(timeTaken);
        String correctAnswer = correctAnswerTextView.getText().toString().split(": ")[1];
        int correctOption = Integer.parseInt(correctAnswer);
        if (selectedOption == correctOption) {
            Toast.makeText(QuizActivity.this, "Poprawna odpowiedź!", Toast.LENGTH_SHORT).show();
            correctAnswersCount++;
        } else {
            Toast.makeText(QuizActivity.this, "Niestety, odpowiedź błędna.", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questionSnapshots.size()) {
            displayQuestion(currentQuestionIndex);
            startTimer(); // Restart the countdown timer for the next question
        } else {
            showSummaryDialog();
        }
    }

    private void OptionToButton(Button button, String option, int optionNumber) {
        button.setText(optionNumber + ". " + option);
    }

    private void showSummaryDialog() {
        float percentage = (float) correctAnswersCount / totalQuestions * 100;
        StringBuilder message = new StringBuilder();
        message.append("Udało Ci się odpowiedzieć na ").append(correctAnswersCount).append(" z 3.\n\n");
        message.append("Czas odpowiedzi na pytania:\n");
        for (int i = 0; i < timeTakenList.size(); i++) {
            long timeTakenMillis = timeTakenList.get(i);
            long seconds = (timeTakenMillis / 1000) % 60;
            long minutes = (timeTakenMillis / 1000) / 60;
            message.append(String.format(Locale.getDefault(), "Pytanie %d: %02d:%02d\n", i + 1, minutes, seconds));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Podsumowanie Quizu");
        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        finishQuiz();
    }
    private void finishQuiz() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String difficulty = selectedDifficulty;
        String category = selectedCategory;

        QuizResultManager quizResultManager = new QuizResultManager();
        quizResultManager.saveQuizResult(userId, difficulty, category, correctAnswersCount);
    }
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the previous timer if it was running
        }
        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the displayed time
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d",
                        millisUntilFinished / 1000 / 60, // minutes
                        millisUntilFinished / 1000 % 60); // seconds
                countdownTextView.setText(timeLeftFormatted);
            }
            public void onFinish() {
                checkAnswer(0);
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
