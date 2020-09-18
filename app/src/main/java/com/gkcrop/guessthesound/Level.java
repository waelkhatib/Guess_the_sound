package com.gkcrop.guessthesound;

class Level {

	private String MusicId;
	private String Ribbon;
	private String Answer;

    // --Commented out by Inspection START (18/09/2020 20:47):
//	public String getLevelNumber() {
//		return levelNumber;
//	}
// --Commented out by Inspection STOP (18/09/2020 20:47)
//	public void setLevelNumber(String levelNumber) {
//        String levelNumber1 = levelNumber;
//	}

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
