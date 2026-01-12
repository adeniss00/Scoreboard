package com.scoreboard;

import com.scoreboard.exception.ScoreBoardException;
import com.scoreboard.model.Match;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreBoardImpl implements IScoreBoard {
    private final Map<String, Match> matches;

    public ScoreBoardImpl() {
        this.matches = new LinkedHashMap<>();
    }

    @Override
    public Match startMatch(String homeTeam, String awayTeam) throws ScoreBoardException {
        validateTeams(homeTeam, awayTeam);

        String key = generateKey(homeTeam, awayTeam);

        if (matches.containsKey(key)) {
            throw new ScoreBoardException("Match " + key + " already exists");
        }

        Match match = new Match(homeTeam, awayTeam, 0, 0,System.currentTimeMillis());
        matches.put(key, match);

        return match;
    }

    @Override
    public Match finishMatch(String homeTeam, String awayTeam) throws ScoreBoardException {
        validateTeams(homeTeam, awayTeam);

        String key = generateKey(homeTeam, awayTeam);
        Match match = matches.remove(key);

        if (match == null) {
            throw new ScoreBoardException("Match " + key + " not found");
        }

        return match;
    }
    private String generateKey(String homeTeam, String awayTeam) {
        return homeTeam + "-" + awayTeam;
    }
    private void validateTeams(String homeTeam, String awayTeam) throws ScoreBoardException {
        if (homeTeam == null || homeTeam.trim().isEmpty()) {
            throw new ScoreBoardException("Home team cannot be null or empty");
        }
        if (awayTeam == null || awayTeam.trim().isEmpty()) {
            throw new ScoreBoardException("Away team cannot be null or empty");
        }
        if (homeTeam.equals(awayTeam)) {
            throw new ScoreBoardException("A team cannot play against itself");
        }
    }

    @Override
    public Match updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore)
            throws ScoreBoardException {
        validateTeams(homeTeam, awayTeam);
        validateScores(homeScore, awayScore);

        String key = generateKey(homeTeam, awayTeam);
        Match existingMatch = matches.get(key);

        if (existingMatch == null) {
            throw new ScoreBoardException("Match " + key + " not found");
        }

        Match updatedMatch = existingMatch.withScore(homeScore, awayScore);
        matches.put(key, updatedMatch);

        return updatedMatch;
    }
    private void validateScores(int homeScore, int awayScore) throws ScoreBoardException {
        if (homeScore < 0) {
            throw new ScoreBoardException("Home score cannot be negative");
        }
        if (awayScore < 0) {
            throw new ScoreBoardException("Away score cannot be negative");
        }
    }
    @Override
    public List<Match> getSummary() {
        return matches.values().stream()
                .sorted(Comparator
                        .comparing(Match::getTotalScore, Comparator.reverseOrder())
                        .thenComparing(Match::startTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
