package com.scoreboard;

import com.scoreboard.exception.ScoreBoardException;
import com.scoreboard.model.Match;

import java.util.LinkedHashMap;
import java.util.Map;

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

        Match match = new Match(homeTeam, awayTeam, 0, 0);
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
}
