package com.example.geoapp.QuizResults;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuizResultsAdapter extends RecyclerView.Adapter<QuizResultsAdapter.QuizResultViewHolder> {

    private List<QuizResult> quizResults = new ArrayList<>();

    @NonNull
    @Override
    public QuizResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_result, parent, false);
        return new QuizResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizResultViewHolder holder, int position) {
        QuizResult quizResult = quizResults.get(position);
        holder.bind(quizResult);
    }

    @Override
    public int getItemCount() {
        return quizResults.size();
    }

    public void setQuizResults(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
        notifyDataSetChanged();
    }

    public static class QuizResultViewHolder extends RecyclerView.ViewHolder {
        private final TextView userNameTextView;
        private final TextView dateTextView;
        private final TextView difficultyTextView;
        private final TextView categoryTextView;
        private final TextView correctAnswersTextView;

        public QuizResultViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            difficultyTextView = itemView.findViewById(R.id.difficultyTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            correctAnswersTextView = itemView.findViewById(R.id.correctAnswersTextView);
        }

        public void bind(QuizResult quizResult) {
            userNameTextView.setText("Email: " + quizResult.getEmail());
            dateTextView.setText("Data: " + formatDate(quizResult.getDate()));
            difficultyTextView.setText("Poziom trudnosci: " + quizResult.getDifficulty());
            categoryTextView.setText("Kategoria: " + quizResult.getCategory());
            correctAnswersTextView.setText("Ilość poprawnych odpowiedzi: " + quizResult.getCorrectAnswers() + "/3");
        }

        private String formatDate(java.util.Date date) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            return dateFormat.format(date);
        }
    }
}