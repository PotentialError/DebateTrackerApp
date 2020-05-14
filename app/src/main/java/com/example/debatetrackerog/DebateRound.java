package com.example.debatetrackerog;

public class DebateRound {
    private String mTournamentName;
    private String mOpponentNames;
    private String mSchoolName;
    private String mDebateStyle;
    private String mWinLoss;
    private String mProCon;
    private String mDate;
    public DebateRound(String tournament, String opponentNames, String schoolName, String debateStyle, String proCon, String winLoss, String date) {
        mTournamentName = tournament;
        mOpponentNames = opponentNames;
        mSchoolName = schoolName;
        mDebateStyle = debateStyle;
        mWinLoss = winLoss;
        mProCon = proCon;
        mDate = date;
    }
    public String getTournamentName() {
        return mTournamentName;
    }
    public String getOpponentNames() {
        return mOpponentNames;
    }

    public String getSchoolName() {
        return mSchoolName;
    }
    public String getDebateStyle() {
        return mDebateStyle;
    }
    public String getWinLoss() {
        return mWinLoss;
    }
    public String getProCon() {
        return mProCon;
    }
    public String getDate() {
        return mDate;
    }
}
