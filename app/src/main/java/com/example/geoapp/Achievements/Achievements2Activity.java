package com.example.geoapp.Achievements;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.QuizResults.QuizResult;
import com.example.geoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Achievements2Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ProgressBar progressBarAchievement;
    private TextView tvPercentage;
    private TextView tvAchievement;
    private TextView tvAchievementProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference quizResultsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements2);

        progressBar = findViewById(R.id.progressBar2);
        tvPercentage = findViewById(R.id.tvPercentage);
        tvAchievement = findViewById(R.id.tvAchievement);
        progressBarAchievement = findViewById(R.id.progressBarAchievement);
        tvAchievementProgress = findViewById(R.id.tvAchievementProgress);
        mAuth = FirebaseAuth.getInstance();
        quizResultsReference = FirebaseDatabase.getInstance().getReference().child("QuizResults");

        // Pobierz aktualnie zalogowanego użytkownika
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            QuizResults(userEmail);
        } else {
            Toast.makeText(this, "Brak zalogowanego użytkownika", Toast.LENGTH_SHORT).show();
        }
    }

    private void QuizResults(String userEmail) {
        if (userEmail != null) {
            String emailKey = userEmail.replace(".", ",");
            quizResultsReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<QuizResult> quizResults = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        QuizResult quizResult = snapshot.getValue(QuizResult.class);
                        if (quizResult != null) {
                            quizResults.add(quizResult);
                        }
                    }
                    osiagniecieProcentowe(quizResults);
                    sprawdzOsiagniecia(quizResults);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Achievements2Activity.this, "Błąd pobierania wyników", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Nieprawidłowy adres email", Toast.LENGTH_SHORT).show();
        }
    }

    private void osiagniecieProcentowe(List<QuizResult> quizResults) {
        if (quizResults.size() > 10) {
            quizResults = quizResults.subList(quizResults.size() - 10, quizResults.size());
        }

        int totalCorrectAnswers = 0;
        int totalQuestions = quizResults.size() * 3;
        for (QuizResult result : quizResults) {
            totalCorrectAnswers += result.getCorrectAnswers();
        }

        int procent = (int) (((double) totalCorrectAnswers / totalQuestions) * 100);
        progressBar.setProgress(procent);
        tvPercentage.setText("Procent: " + procent + "%");

        // Dodanie osiągnięcia w TextView
        if (procent >= 80) {
            tvAchievement.setText("Osiągnięcie: Ekspert! Osiągnąłeś co najmniej 80% poprawnych odpowiedzi w ostatnich 10 quizach!");
            tvAchievement.setVisibility(TextView.VISIBLE);
        } else {
            tvAchievement.setVisibility(TextView.GONE);
        }
    }

    private void sprawdzOsiagniecia(List<QuizResult> quizResults) {
        int liczbaQuizow = quizResults.size();

        // Aktualizacja progresu osiągnięcia "Uczestnik"
        progressBarAchievement.setProgress(liczbaQuizow);
        tvAchievementProgress.setText("Progres: " + liczbaQuizow + "/20");

        // Dodanie osiągnięcia "Uczestnik"
        if (liczbaQuizow >= 20) {
            tvAchievement.setText("Osiągnięcie: Uczestnik! Ukończyłeś 20 quizów!");
            tvAchievement.setVisibility(TextView.VISIBLE);
        }
    }
}
