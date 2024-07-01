package com.example.geoapp.Ranking;

import java.util.Date;

public class RankingQuizResult {
    private String email;
    private String difficulty;
    private String category;
    private int correctAnswers;
    private Date date;

    public RankingQuizResult() {

    }

    public RankingQuizResult(String email, String difficulty, String category, int correctAnswers, Date date) {
        this.email = email;
        this.difficulty = difficulty;
        this.category = category;
        this.correctAnswers = correctAnswers;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public Date getDate() {
        return date;
    }
}