package com.example.geoapp.Ranking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoapp.Achievements.AchievementsActivity;
import com.example.geoapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RankingAdapter rankingAdapter;
    private List<UserScore> userScoreList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference rankingReference;
    private Switch sortSwitch;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        recyclerView = findViewById(R.id.recyclerViewRanking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rankingAdapter = new RankingAdapter(userScoreList);
        recyclerView.setAdapter(rankingAdapter);

        sortSwitch = findViewById(R.id.sortSwitch);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuizResults");
        rankingReference = FirebaseDatabase.getInstance().getReference().child("Ranking");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sortAndDisplayRanking(isChecked);
        });

        getRanking();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserScore userScore = userScoreList.get(position);
                Intent intent = new Intent(RankingActivity.this, AchievementsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Optional: handle long click
            }
        }));
    }

    private void getRanking() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> userScores = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RankingQuizResult quizResult = snapshot.getValue(RankingQuizResult.class);
                    if (quizResult != null) {
                        String email = quizResult.getEmail();
                        int correctAnswers = quizResult.getCorrectAnswers();

                        if (userScores.containsKey(email)) {
                            userScores.put(email, userScores.get(email) + correctAnswers);
                        } else {
                            userScores.put(email, correctAnswers);
                        }
                    }
                }

                userScoreList.clear();
                for (Map.Entry<String, Integer> entry : userScores.entrySet()) {
                    userScoreList.add(new UserScore(entry.getKey(), entry.getValue()));
                }

                saveRankingToDatabase();
                sortAndDisplayRanking(sortSwitch.isChecked());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RankingActivity", "Blad z baza danych" + databaseError.getMessage());
                Toast.makeText(RankingActivity.this, "Nie udalo sie zaladowac rankingu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveRankingToDatabase() {
        Map<String, Object> rankingMap = new HashMap<>();
        for (UserScore userScore : userScoreList) {
            rankingMap.put(userScore.getEmail().replace(".", ","), userScore.getCorrectAnswers());
        }
        rankingReference.setValue(rankingMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("", "");
            } else {
                Log.e("", "", task.getException());
            }
        });
    }

    private void sortAndDisplayRanking(boolean ascending) {
        if (ascending) {
            Collections.sort(userScoreList, new Comparator<UserScore>() {
                @Override
                public int compare(UserScore u1, UserScore u2) {
                    return Integer.compare(u1.getCorrectAnswers(), u2.getCorrectAnswers());
                }
            });
        } else {
            Collections.sort(userScoreList, new Comparator<UserScore>() {
                @Override
                public int compare(UserScore u1, UserScore u2) {
                    return Integer.compare(u2.getCorrectAnswers(), u1.getCorrectAnswers());
                }
            });
        }
        rankingAdapter.notifyDataSetChanged();


    }
}
