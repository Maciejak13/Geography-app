package com.example.geoapp.Ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoapp.R;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<UserScore> userScoreList;

    public RankingAdapter(List<UserScore> userScoreList) {
        this.userScoreList = userScoreList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserScore userScore = userScoreList.get(position);
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.emailTextView.setText(userScore.getEmail());
        holder.scoreTextView.setText(String.valueOf(userScore.getCorrectAnswers()));
    }

    @Override
    public int getItemCount() {
        return userScoreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rankTextView;
        public TextView emailTextView;
        public TextView scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}
