package com.scoreboard;

import com.scoreboard.exception.ScoreBoardException;
import com.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ScoreBoardImplTest {

    private IScoreBoard scoreBoard;

    @BeforeEach
    void setUp() {
        scoreBoard = new ScoreBoardImpl();
    }

    @Test
    @DisplayName("Should start a match with initial score 0-0")
    void testStartMatch_Success() throws ScoreBoardException {
        Match match = scoreBoard.startMatch("Mexico", "Canada");
        assertNotNull(match);
        assertEquals("Mexico", match.getHomeTeam());
        assertEquals("Canada", match.getAwayTeam());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }

    @Test
    @DisplayName("Should throw exception when starting duplicate match")
    void testStartMatch_Duplicate() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");

        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.startMatch("Mexico", "Canada");
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Should throw exception when home team is null")
    void testStartMatch_NullHomeTeam() {
        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.startMatch(null, "Canada");
        });

        assertTrue(exception.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should throw exception when away team is empty")
    void testStartMatch_EmptyAwayTeam() {
        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.startMatch("Mexico", "");
        });

        assertTrue(exception.getMessage().contains("cannot be null or empty"));
    }

    @Test
    @DisplayName("Should throw exception when team plays against itself")
    void testStartMatch_SameTeam() {
        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.startMatch("Mexico", "Mexico");
        });

        assertTrue(exception.getMessage().contains("cannot play against itself"));
    }
    @Test
    @DisplayName("Should finish an existing match")
    void testFinishMatch_Success() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");

        Match finishedMatch = scoreBoard.finishMatch("Mexico", "Canada");

        assertNotNull(finishedMatch);
        assertEquals("Mexico", finishedMatch.getHomeTeam());
        assertEquals("Canada", finishedMatch.getAwayTeam());
    }

    @Test
    @DisplayName("Should throw exception when finishing non-existent match")
    void testFinishMatch_NotFound() {
        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.finishMatch("Mexico", "Canada");
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should update score of existing match")
    void testUpdateScore_Success() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");

        Match updated = scoreBoard.updateScore("Mexico", "Canada", 0, 5);

        assertEquals(0, updated.getHomeScore());
        assertEquals(5, updated.getAwayScore());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent match")
    void testUpdateScore_NotFound() {
        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.updateScore("Mexico", "Canada", 1, 1);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should throw exception when home score is negative")
    void testUpdateScore_NegativeHomeScore() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");

        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.updateScore("Mexico", "Canada", -1, 5);
        });

        assertTrue(exception.getMessage().contains("cannot be negative"));
    }

    @Test
    @DisplayName("Should throw exception when away score is negative")
    void testUpdateScore_NegativeAwayScore() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");

        ScoreBoardException exception = assertThrows(ScoreBoardException.class, () -> {
            scoreBoard.updateScore("Mexico", "Canada", 0, -5);
        });

        assertTrue(exception.getMessage().contains("cannot be negative"));
    }
    @Test
    @DisplayName("Should return empty list when no matches")
    void testGetSummary_Empty() {
        List<Match> summary = scoreBoard.getSummary();

        assertTrue(summary.isEmpty());
    }

    @Test
    @DisplayName("Should return matches sorted by total score descending")
    void testGetSummary_SortedByScore() throws ScoreBoardException {
        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 0, 5);

        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);

        scoreBoard.startMatch("Germany", "France");
        scoreBoard.updateScore("Germany", "France", 2, 2);

        List<Match> summary = scoreBoard.getSummary();

        assertEquals(3, summary.size());
        assertEquals("Spain", summary.get(0).getHomeTeam());
        assertEquals("Mexico", summary.get(1).getHomeTeam());
        assertEquals("Germany", summary.get(2).getHomeTeam());
    }
    @Test
    @DisplayName("Should sort by most recent when total scores are equal")
    void testGetSummary_SortedByTimeWhenScoresEqual() throws ScoreBoardException, InterruptedException {
        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.updateScore("Mexico", "Canada", 0, 5);

        Thread.sleep(10);

        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);

        Thread.sleep(10);

        scoreBoard.startMatch("Uruguay", "Italy");
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);

        List<Match> summary = scoreBoard.getSummary();

        assertEquals("Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Spain", summary.get(1).getHomeTeam());
        assertEquals("Mexico", summary.get(2).getHomeTeam());
    }

    @Test
    @DisplayName("Should match the example from requirements")
    void testGetSummary_RequirementsExample() throws ScoreBoardException, InterruptedException {
        scoreBoard.startMatch("Mexico", "Canada");
        Thread.sleep(5);
        scoreBoard.startMatch("Spain", "Brazil");
        Thread.sleep(5);
        scoreBoard.startMatch("Germany", "France");
        Thread.sleep(5);
        scoreBoard.startMatch("Uruguay", "Italy");
        Thread.sleep(5);
        scoreBoard.startMatch("Argentina", "Australia");

        scoreBoard.updateScore("Mexico", "Canada", 0, 5);
        scoreBoard.updateScore("Spain", "Brazil", 10, 2);
        scoreBoard.updateScore("Germany", "France", 2, 2);
        scoreBoard.updateScore("Uruguay", "Italy", 6, 6);
        scoreBoard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> summary = scoreBoard.getSummary();

        assertEquals(5, summary.size());
        assertEquals("Uruguay 6 - Italy 6", summary.get(0).toString());
        assertEquals("Spain 10 - Brazil 2", summary.get(1).toString());
        assertEquals("Mexico 0 - Canada 5", summary.get(2).toString());
        assertEquals("Argentina 3 - Australia 1", summary.get(3).toString());
        assertEquals("Germany 2 - France 2", summary.get(4).toString());
    }



}

