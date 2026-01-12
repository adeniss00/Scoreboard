package com.scoreboard.model;

public record Match(String homeTeam, String awayTeam, int homeScore, int awayScore, long startTime) {

    public Match withScore(int newHomeScore, int newAwayScore) {
        return new Match(homeTeam, awayTeam, newHomeScore, newAwayScore, startTime);
    }
    public int getTotalScore(){return homeScore+awayScore; }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore;
    }

}