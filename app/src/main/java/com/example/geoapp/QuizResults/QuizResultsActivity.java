package com.example.geoapp.QuizResults;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class QuizResultsActivity extends AppCompatActivity {

    private static final String TAG = "QuizResultsActivity";
    private RecyclerView recyclerView;
    private QuizResultsAdapter adapter;
    private DatabaseReference databaseReference;
    private LinkedList<QuizResult> quizResultsList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuizResultsAdapter();
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizResults");
        fetchQuizResults();
    }

    private void fetchQuizResults() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                quizResultsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Pobierz dane z obiektu QuizResult
                    QuizResult quizResult = snapshot.getValue(QuizResult.class);
                    if (quizResult != null) {
                        quizResultsList.addFirst(quizResult);//od najnowszych
                    }
                }
                adapter.setQuizResults(quizResultsList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "", databaseError.toException());
            }
        });
    }
}
