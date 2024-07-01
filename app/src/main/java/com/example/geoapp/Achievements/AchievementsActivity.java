package com.example.geoapp.Achievements;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geoapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
public class AchievementsActivity extends AppCompatActivity {
    private TextView tvUserProgress, tvProgressDetails, tvUserTitle;
    private ImageView imageViewAvatar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TextView textViewDisplayName;
    private ProgressBar progressBar;
    private DatabaseReference rankingReference;
    private ValueEventListener rankingListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        textViewDisplayName = findViewById(R.id.textViewDisplayName);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        tvUserProgress = findViewById(R.id.tvUserProgress);
        tvProgressDetails = findViewById(R.id.tvProgressDetails);
        tvUserTitle = findViewById(R.id.tvUserTitle);
        progressBar = findViewById(R.id.progressBar);
        Button buttonAchievements2 = findViewById(R.id.buttonAchievements2);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            Username(userId);
            Avatar(userId);
            startRankingListener(userEmail);
        } else {
            textViewDisplayName.setText("");
            tvProgressDetails.setText("");
        }
        buttonAchievements2.setOnClickListener(view -> {
            Intent intent = new Intent(AchievementsActivity.this, Achievements2Activity.class);
            startActivity(intent);
        });
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            startRankingListener(userEmail);
        }
    }
    private void Avatar(String userId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("avatars/" + userId + ".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageViewAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Błąd podczas pobierania awatara
            }
        });
    }
    private void startRankingListener(String userEmail) {
        if (userEmail != null) {
            String emailKey = userEmail.replace(".", ",");
            rankingReference = FirebaseDatabase.getInstance().getReference().child("Ranking").child(emailKey);

            rankingListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    updateUI(dataSnapshot);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("AchievementsActivity", "Blad" + databaseError.getMessage());
                    Toast.makeText(AchievementsActivity.this, "Blad", Toast.LENGTH_SHORT).show();
                }
            };
            rankingReference.addValueEventListener(rankingListener);
        } else {
            tvProgressDetails.setText("No email provided.");
        }
    }
    private void updateUI(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            int correctAnswers = dataSnapshot.getValue(Integer.class);
            int totalQuestions = 20;
            String title;
            if (correctAnswers >= 60) {
                title = "Profesjonalny zgadywaczu";
                correctAnswers -= 60;
            } else if (correctAnswers >= 40) {
                title = "Doświadczony podróżniku";
                correctAnswers -= 40;
            } else if (correctAnswers >= 20) {
                title = "Aspirujący zgadywaczu";
                correctAnswers -= 20;
            } else {
                title = "Nowicjuszu";
            }
            progressBar.setMax(totalQuestions);
            progressBar.setProgress(correctAnswers);
            tvUserTitle.setText("Witaj! " + title);
        } else {
            tvProgressDetails.setText("");
        }
    }
    private void Username(String userId) {
        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("username");
                                textViewDisplayName.setText(username);
                            } else {
                                textViewDisplayName.setText("");
                            }
                        } else {
                            textViewDisplayName.setText("");
                        }
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rankingReference != null && rankingListener != null) {
            rankingReference.removeEventListener(rankingListener);
        }
    }
}
