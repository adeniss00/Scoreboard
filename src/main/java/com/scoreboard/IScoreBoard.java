package com.scoreboard;

import com.scoreboard.exception.ScoreBoardException;
import com.scoreboard.model.Match;

public interface IScoreBoard {

    Match startMatch(String homeTeam,String awayTeam) throws ScoreBoardException;
    Match finishMatch(String homeTeam,String awayTeam) throws ScoreBoardException;
}
