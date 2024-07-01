package com.example.geoapp.Ranking;

public class UserScore {
    private String email;
    private int correctAnswers;

    public UserScore(String email, int correctAnswers) {
        this.email = email;
        this.correctAnswers = correctAnswers;
    }

    public String getEmail() {
        return email;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }
}
