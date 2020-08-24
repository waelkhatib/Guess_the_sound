package com.gkcrop.guessthesound;

public class Level {

	private String MusicId;
	private String Ribbon;
	private String Answer;
	private String levelNumber;

	public String getLevelNumber() {
		return levelNumber;
	}
	public void setLevelNumber(String levelNumber) {
		this.levelNumber = levelNumber;
	}

	public String getMusicId() {
		return MusicId;
	}
	public void setMusicId(String MusicId) {
		this.MusicId = MusicId;
	}

	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String Answer) {
		this.Answer = Answer;
	}

	public String getRibbon() {
		return Ribbon;
	}
	public void setRibbon(String Ribbon) {
		this.Ribbon = Ribbon;
	}

}
