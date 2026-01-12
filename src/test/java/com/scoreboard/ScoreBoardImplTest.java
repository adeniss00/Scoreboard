package com.scoreboard;

import com.scoreboard.exception.ScoreBoardException;
import com.scoreboard.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


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


}

