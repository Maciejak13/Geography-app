package com.example.geoapp.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    private Spinner difficultySpinner;
    private Spinner categorySpinner;
    private Button startQuizButton;
    private DatabaseReference databaseReference;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        startQuizButton = findViewById(R.id.startQuizButton);
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
        categories = new ArrayList<>();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QUIZS");
        fetchCategoriesFromDatabase(categoryAdapter);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                startQuiz(selectedDifficulty, selectedCategory);
            }
        });
    }
    private void fetchCategoriesFromDatabase(ArrayAdapter<String> categoryAdapter) {
        databaseReference.orderByChild("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String category = dataSnapshot.child("category").getValue(String.class);
                    if (category != null && !categories.contains(category)) {
                        categories.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChooseActivity.this, "Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void startQuiz(String difficulty, String category) {
        Intent intent = new Intent(ChooseActivity.this, QuizActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}