package com.example.geoapp.QuizResults;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class QuizResultManager {
    private DatabaseReference databaseReference;

    public QuizResultManager() {
        // Inicjalizacja odniesienia do bazy danych Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizResults");
    }

    public void saveQuizResult(String userEmail, String difficulty, String category, int correctAnswers) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String email = user.getEmail(); // email użytkownika
          //nowy obiek z wynikami quizu
            QuizResult quizResult = new QuizResult(email, difficulty, category, correctAnswers, new Date());
            String resultId = databaseReference.push().getKey();
            databaseReference.child(resultId).setValue(quizResult);
        }
    }
}
