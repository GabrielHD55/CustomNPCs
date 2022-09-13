package noppes.npcs.api.wrapper;

import net.minecraft.scoreboard.Score;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardScoreWrapper implements IScoreboardScore {
	private final Score score;
	
	public ScoreboardScoreWrapper(Score score){
		this.score = score;
	}
	
	@Override
	public int getValue() {
		return score.getScore();
	}

	@Override
	public void setValue(int val) {
		score.setScore(val);
	}

	@Override
	public String getPlayerName() {
		return score.getOwner();
	}

}
