package com.example.aircraftwar2024.DAO;

import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable {
    private int score;
    private String name;
    private String date;

    public int GetScore() {
        return score;
    }

    public String GetName() {
        return name;
    }

    public String GetDate() {
        return date;
    }

    public void SetScore(int score) {
        this.score = score;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public void SetDate(String date) {
        this.date = date;
    }
}
