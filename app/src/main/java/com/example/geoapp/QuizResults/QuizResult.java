package com.example.geoapp.QuizResults;

import java.util.Date;

public class QuizResult {
    private String email;
    private String difficulty;
    private String category;
    private int correctAnswers;
    private Date date;

    public QuizResult() {

    }

    public QuizResult(String email, String difficulty, String category, int correctAnswers, Date date) {
        this.email = email;
        this.difficulty = difficulty;
        this.category = category;
        this.correctAnswers = correctAnswers;
        this.date = date;
    }

    // Gettery i settery

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}