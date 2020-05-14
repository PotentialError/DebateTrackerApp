package com.example.debatetrackerog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class DebateTimerFragment extends Fragment {
    private ArrayList<Speech> round;
    View rootView;
    private int count;
    private onEndDebateListener endDebateListener;

    private int debateStyle;
    private int position;
    private Button nextButton;
    private Button backButton;
    private Button currentStartStopButton;
    private Button currentResetButton;
    private Button myResetButton;
    private Button myStartStopButton;
    private Button enemyResetButton;
    private Button enemyStartStopButton;
    private int myPrep;
    private int enemyPrep;
    private int prepTime;
    private CountDownTimer myTimer;
    private CountDownTimer enemyTimer;
    private TextView myTimerText;
    private TextView enemyTimerText;
    private String myStartStopButtonText;
    private String enemyStartStopButtonText;
    private String currentStartStopButtonText;
    private String nextButtonText;
    private int currentTime;
    private boolean isRotated;
    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        if(onSavedInstanceState != null) {
            count = onSavedInstanceState.getInt("count");
            myPrep = onSavedInstanceState.getInt("myPrep");
            enemyPrep = onSavedInstanceState.getInt("enemyPrep");
            currentTime = onSavedInstanceState.getInt("currentTime");
            nextButtonText = onSavedInstanceState.getString("nextButtonText");
            myStartStopButtonText = onSavedInstanceState.getString("myStartStopButtonText");
            enemyStartStopButtonText = onSavedInstanceState.getString("enemyStartStopButtonText");
            currentStartStopButtonText = onSavedInstanceState.getString("currentStartStopButtonText");
            isRotated = true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_debate_timer, container, false);
        debateStyle = getArguments().getInt("debateStyle");
        nextButton = rootView.findViewById(R.id.currentNextTimer);
        backButton = rootView.findViewById(R.id.currentPreviousTimer);
        currentStartStopButton = rootView.findViewById(R.id.currentStopTimer);
        currentResetButton = rootView.findViewById(R.id.currentResetTimer);
        myResetButton = rootView.findViewById(R.id.myResetTimer);
        myStartStopButton = rootView.findViewById(R.id.myStopTimer);
        enemyResetButton = rootView.findViewById(R.id.opponentResetTimer);
        enemyStartStopButton = rootView.findViewById(R.id.opponentStopTimer);

        position = getArguments().getInt("position");
        myTimerText = rootView.findViewById(R.id.myTimerText);
        enemyTimerText = rootView.findViewById(R.id.opponentTimerText);
        round = new ArrayList<>();
        //If side = 0 make role speaking the first speaker out of the two who will fight
        //If name is Grand Cross everyone is talking
        if (debateStyle == 0) {
            //For proGoesFirst -1 = CON first 1 = PRO first
            int proGoesFirst = -1;
            if (getArguments().getBoolean("proGoesFirst")) {
                proGoesFirst = 1;
            }
            round.add(new Speech(4, 0, "Constructive", proGoesFirst));
            round.add(new Speech(4, 3, "Constructive", -proGoesFirst));
            round.add(new Speech(3, 0, "Crossfire", 0));
            round.add(new Speech(4, 1, "Rebuttal", proGoesFirst));
            round.add(new Speech(4, 4, "Rebuttal", -proGoesFirst));
            round.add(new Speech(3, 1, "Crossfire", 0));
            if (getArguments().getBoolean("isNSDA")) {
                prepTime = 3;
                round.add(new Speech(3, 0, "Summary", proGoesFirst));
                round.add(new Speech(3, 3, "Summary", -proGoesFirst));
                Log.i("Thing", "NSDA");
            } else {
                prepTime = 2;
                round.add(new Speech(2, 0, "Summary", proGoesFirst));
                round.add(new Speech(2, 3, "Summary", -proGoesFirst));
            }
            round.add(new Speech(3, 0, "Grand Crossfire", 0));
            round.add(new Speech(4, 1, "Final Focus", proGoesFirst));
            round.add(new Speech(4, 4, "Final Focus", -proGoesFirst));
        } else if (debateStyle == 1) {
            prepTime = 4;
            round.add(new Speech(6, 0, "Constructive", 1));
            round.add(new Speech(3, 3, "Cross-Examination", -1));
            round.add(new Speech(7, 3, "Constructive", -1));
            round.add(new Speech(3, 0, "Cross-Examination", 1));
            round.add(new Speech(4, 0, "First Rebuttal", 1));
            round.add(new Speech(6, 3, "Rebuttal", -1));
            round.add(new Speech(3, 0, "Second Rebuttal", 1));
        } else if (debateStyle == 2) {
            prepTime = 5;
            round.add(new Speech(8, 0, "1AC", 1));
            round.add(new Speech(3, 4, "Cross-Examination", -1));
            round.add(new Speech(8, 3, "1NC", -1));
            round.add(new Speech(3, 0, "Cross-Examination", 1));
            round.add(new Speech(8, 1, "2AC", 1));
            round.add(new Speech(3, 3, "Cross-Examination", -1));
            round.add(new Speech(8, 4, "2NC", -1));
            round.add(new Speech(3, 1, "Cross-Examination", 1));
            round.add(new Speech(5, 3, "1NR", -1));
            round.add(new Speech(5, 0, "1AR", 1));
            round.add(new Speech(5, 4, "2NR", -1));
            round.add(new Speech(5, 1, "1AR", 1));
        } else if (debateStyle == 3) {
            int debatingAlone = getArguments().getInt("debatingAlone");
            prepTime = 3;
            round.add(new Speech(5, 0, "Constructive", 1));
            round.add(new Speech(5, 3, "Constructive", -1));
            round.add(new Speech(3, 0, "Question Segment", 0));
            if(debatingAlone == 0) {
                round.add(new Speech(4, 0, "Rebuttal", 1));
                round.add(new Speech(4, 3, "Rebuttal", -1));
                round.add(new Speech(3, 0, "Question Segment", 0));
                round.add(new Speech(3, 0, "Consolidation", 1));
                round.add(new Speech(3, 3, "Consolidation", -1));
                round.add(new Speech(3, 0, "Rationale", 1));
                round.add(new Speech(3, 3, "Rationale", -1));
            }
            else if(debatingAlone == 1) {
                round.add(new Speech(4, 0, "Rebuttal", 1));
                round.add(new Speech(4, 4, "Rebuttal", -1));
                round.add(new Speech(3, 0, "Question Segment", 0));
                round.add(new Speech(3, 0, "Consolidation", 1));
                round.add(new Speech(3, 3, "Consolidation", -1));
                round.add(new Speech(3, 0, "Rationale", 1));
                round.add(new Speech(3, 4, "Rationale", -1));
            }
            else if(debatingAlone == 2) {
                round.add(new Speech(4, 1, "Rebuttal", 1));
                round.add(new Speech(4, 3, "Rebuttal", -1));
                round.add(new Speech(3, 1, "Question Segment", 0));
                round.add(new Speech(3, 0, "Consolidation", 1));
                round.add(new Speech(3, 3, "Consolidation", -1));
                round.add(new Speech(3, 1, "Rationale", 1));
                round.add(new Speech(3, 3, "Rationale", -1));
            }
            else {
                round.add(new Speech(4, 1, "Rebuttal", 1));
                round.add(new Speech(4, 4, "Rebuttal", -1));
                round.add(new Speech(3, 1, "Question Segment", 0));
                round.add(new Speech(3, 0, "Consolidation", 1));
                round.add(new Speech(3, 3, "Consolidation", -1));
                round.add(new Speech(3, 1, "Rationale", 1));
                round.add(new Speech(3, 4, "Rationale", -1));
            }
        } else if (debateStyle == 4) {
            round.add(new Speech(2, 0, "Constructive", 1));
            round.add(new Speech(1, 3, "Cross Examination", -1));
            round.add(new Speech(2, 3, "Constructive", -1));
            round.add(new Speech(1, 0, "Cross Examination", 1));
            round.add(new Speech(1, -1, "Prep Time", 0));
            round.add(new Speech(2, 0, "Rebuttal", 1));
            round.add(new Speech(2, 3, "Rebuttal", -1));
            round.add(new Speech(1, -1, "Prep Time", 0));
            round.add(new Speech(2, 0, "Rebuttal", 1));
            round.add(new Speech(2, 3, "Rebuttal", -1));
        }
        else if(debateStyle == 5) {
            int rebuttalPosition = getArguments().getInt("rebuttalPosition", -1);
            round.add(new Speech(8, 0, "Constructive", 1));
            round.add(new Speech(8, 3, "Constructive", -1));
            round.add(new Speech(8, 1, "Constructive", 1));
            round.add(new Speech(8, 4, "Constructive", -1));
            round.add(new Speech(8, 2, "Constructive", 1));
            round.add(new Speech(8, 5, "Constructive", -1));
            if(position < 3) {
                round.add(new Speech(4, 7, "Rebuttal", -1));
                round.add(new Speech(4, rebuttalPosition, "Rebuttal", 1));
            }
            else {
                round.add(new Speech(4, rebuttalPosition, "Rebuttal", -1));
                round.add(new Speech(4, 7, "Rebuttal", 1));
            }
        }
        if(prepTime == 0) {
            rootView.findViewById(R.id.myPrepTime).setVisibility(View.GONE);
            rootView.findViewById(R.id.opponentPrepTime).setVisibility(View.GONE);
        }
        if(isRotated) {
            myStartStopButton.setText(myStartStopButtonText);
            enemyStartStopButton.setText(enemyStartStopButtonText);
            currentStartStopButton.setText(currentStartStopButtonText);
            nextButton.setText(nextButtonText);
            if(currentStartStopButtonText.equals("Resume")) {
                currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27f);
                currentStartStopButton.setPadding(setPadding(13f), setPadding(12.25f), setPadding(13f), setPadding(12.25f));
            }
            else {
                currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
            }
        }
        else {
            myStartStopButtonText = myStartStopButton.getText().toString();
            enemyStartStopButtonText = enemyStartStopButton.getText().toString();
            currentStartStopButtonText = currentStartStopButton.getText().toString();
            nextButtonText = nextButton.getText().toString();
            count = 0;
            myPrep = prepTime;
            enemyPrep = prepTime;
        }
        prepTime = prepTime * 60 * 1000;
        String prepTimeText = prepTime / (60 * 1000) + ":" + new DecimalFormat("00").format(prepTime % (60 * 1000));
        String timeText = round.get(count).getTime() / (60 * 1000) + ":" + new DecimalFormat("00").format(round.get(count).getTime() % (60 * 1000));
        if(myStartStopButtonText.equals("Start")) {
            myTimerText.setText(prepTimeText);
        }
        else if(myStartStopButtonText.equals("Pause")) {
            resumePrepTime(true);
        }
        else if(myStartStopButtonText.equals("Resume")) {
            String tempText = myPrep / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(((long)myPrep) % (60 * 1000)/1000));
            myTimerText.setText(tempText);
        }
        else {
            String tempText = "0:00";
            myTimerText.setText(tempText);
        }
        if(enemyStartStopButtonText.equals("Start")) {
            enemyTimerText.setText(prepTimeText);
        }
        else if(enemyStartStopButtonText.equals("Pause")) {
            resumePrepTime(false);
        }
        else if(enemyStartStopButtonText.equals("Resume")) {
            String tempText = enemyPrep / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(((long)enemyPrep) % (60 * 1000)/1000));
            enemyTimerText.setText(tempText);
        }
        else {
            String tempText = "0:00";
            enemyTimerText.setText(tempText);
        }
        if(currentStartStopButtonText.equals("Start")) {
            round.get(count).setCurrentTimerText(timeText);
        }
        else if(currentStartStopButtonText.equals("Pause")) {
            round.get(count).setCurrentTime(currentTime);
            round.get(count).resumeTimer();
        }
        else if(currentStartStopButtonText.equals("Resume")) {
            String tempText = currentTime / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(((long)currentTime) % (60 * 1000)/1000));
            round.get(count).setCurrentTimerText(tempText);
            round.get(count).setCurrentTime(currentTime);
        }
        else {
            String tempText = "0:00";
            round.get(count).setCurrentTimerText(tempText);
        }
        refreshSpeech();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == nextButton.getId()) {
                    if (count < round.size() - 1) {
                        round.get(count).cancelTimer();
                        count++;
                        refreshSpeech();
                        round.get(count).resetTimer();
                        currentStartStopButtonText = "Start";
                        currentStartStopButton.setText(currentStartStopButtonText);
                        currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
                    } else {
                        endDebateListener.onEndDebate();
                    }
                    if (count == round.size() - 1) {
                        nextButtonText = "Finish Debate";
                        nextButton.setText(nextButtonText);
                    }
                } else if (v.getId() == backButton.getId()) {
                    if (count > 0) {
                        round.get(count).cancelTimer();
                        count--;
                        refreshSpeech();
                        round.get(count).resetTimer();
                        currentStartStopButtonText = "Start";
                        currentStartStopButton.setText(currentStartStopButtonText);
                        currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
                    }
                    if (count != round.size() - 1) {
                        nextButtonText = "Next Speech";
                        nextButton.setText(nextButtonText);
                    }
                } else if (v.getId() == currentStartStopButton.getId()) {
                    if (currentStartStopButton.getText().equals("Start")) {
                        round.get(count).startTimer();
                        currentStartStopButtonText = "Pause";
                        currentStartStopButton.setText(currentStartStopButtonText);
                        currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
                    } else if (currentStartStopButton.getText().equals("Pause")) {
                        round.get(count).pauseTimer();
                        currentStartStopButtonText = "Resume";
                        currentStartStopButton.setText(currentStartStopButtonText);
                        currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27f);
                        currentStartStopButton.setPadding(setPadding(13f), setPadding(12.25f), setPadding(13f), setPadding(12.25f));
                    } else if (currentStartStopButton.getText().equals("Resume")) {
                        round.get(count).resumeTimer();
                        currentStartStopButtonText = "Pause";
                        currentStartStopButton.setText(currentStartStopButtonText);
                        currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                        currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
                    }
                } else if (v.getId() == currentResetButton.getId()) {
                    round.get(count).resetTimer();
                    currentStartStopButtonText = "Start";
                    currentStartStopButton.setText(currentStartStopButtonText);
                    currentStartStopButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    currentStartStopButton.setPadding(setPadding(20), setPadding(10), setPadding(20), setPadding(10));
                }
                else if(v.getId() == myStartStopButton.getId()) {
                    if(myStartStopButton.getText().equals("Start")) {
                        startPrepTime(true);
                    }
                    else if(myStartStopButton.getText().equals("Pause")) {
                        myStartStopButtonText = "Resume";
                        myStartStopButton.setText(myStartStopButtonText);
                        pausePrepTime(true);
                    }
                    else if(myStartStopButton.getText().equals("Resume")) {
                        myStartStopButtonText = "Pause";
                        myStartStopButton.setText(myStartStopButtonText);
                        resumePrepTime(true);
                    }
                }
                else if(v.getId() == myResetButton.getId()) {
                    resetPrepTime(true);
                }
                else if(v.getId() == enemyStartStopButton.getId()) {
                    if(enemyStartStopButton.getText().equals("Start")) {
                        startPrepTime(false);
                    }
                    else if(enemyStartStopButton.getText().equals("Pause")) {
                        enemyStartStopButtonText = "Resume";
                        enemyStartStopButton.setText(enemyStartStopButtonText);
                        pausePrepTime(false);
                    }
                    else if(enemyStartStopButton.getText().equals("Resume")) {
                        enemyStartStopButtonText = "Pause";
                        enemyStartStopButton.setText(enemyStartStopButtonText);
                        resumePrepTime(false);
                    }
                }
                else if(v.getId() == enemyResetButton.getId()) {
                    resetPrepTime(false);
                }
            }
        };
        nextButton.setOnClickListener(listener);
        backButton.setOnClickListener(listener);
        currentStartStopButton.setOnClickListener(listener);
        currentResetButton.setOnClickListener(listener);
        myStartStopButton.setOnClickListener(listener);
        myResetButton.setOnClickListener(listener);
        enemyStartStopButton.setOnClickListener(listener);
        enemyResetButton.setOnClickListener(listener);

        return rootView;
    }
    private int setPadding(float val) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                val,
                getContext().getResources().getDisplayMetrics());
    }
    private void startPrepTime(boolean isYourPrep) {
        if(isYourPrep) {
            myStartStopButtonText = "Pause";
            myStartStopButton.setText(myStartStopButtonText);
            myTimer = new CountDownTimer(prepTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    myTimerText.setText(timeText);
                    myPrep = (int) millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    myStartStopButtonText = "DONE";
                    myStartStopButton.setText(myStartStopButtonText);
                }
            }.start();
        }
        else {
            enemyStartStopButtonText = "Pause";
            enemyStartStopButton.setText(enemyStartStopButtonText);
            enemyTimer = new CountDownTimer(prepTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    enemyTimerText.setText(timeText);
                    enemyPrep = (int) millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    enemyStartStopButtonText = "DONE";
                    enemyStartStopButton.setText(enemyStartStopButtonText);
                }
            }.start();
        }
    }
    private void resumePrepTime(boolean isYourPrep) {
        if(isYourPrep) {

            myTimer = new CountDownTimer(myPrep, 1000) {
                public void onTick(long millisUntilFinished) {
                    String timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    myTimerText.setText(timeText);
                    myPrep = (int) millisUntilFinished;
                }
                public void onFinish() {
                    myStartStopButtonText = "DONE";
                    myStartStopButton.setText(myStartStopButtonText);
                }
            }.start();
        }
        else {

            enemyTimer = new CountDownTimer(enemyPrep, 1000) {
                public void onTick(long millisUntilFinished) {
                    String timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    enemyTimerText.setText(timeText);
                    enemyPrep = (int) millisUntilFinished;
                }
                public void onFinish() {
                    enemyStartStopButtonText = "DONE";
                    enemyStartStopButton.setText(enemyStartStopButtonText);
                }
            }.start();
        }
    }
    private void pausePrepTime(boolean isYourPrep) {
        if(isYourPrep) {

                if(myTimer != null) {
                    myTimer.cancel();
                }
        }
        else {
                if(enemyTimer != null) {
                    enemyTimer.cancel();
                }
        }
    }
    public void resetPrepTime(boolean isYourPrep) {
        if(isYourPrep) {
            if(myTimer != null) {
                myTimer.cancel();
            }
            myStartStopButtonText = "Start";
            myStartStopButton.setText(myStartStopButtonText);
            String timeText = prepTime / (60 * 1000) + ":" + new DecimalFormat("00").format(prepTime % (60 * 1000)/1000);
            myTimerText.setText(timeText);
        }
        else {
            if(enemyTimer != null) {
                enemyTimer.cancel();
            }
            enemyStartStopButtonText = "Start";
            enemyStartStopButton.setText(enemyStartStopButtonText);
            String timeText = prepTime / (60 * 1000) + ":" + new DecimalFormat("00").format(prepTime % (60 * 1000)/1000);
            enemyTimerText.setText(timeText);
        }
    }
    public class Speech {
        private int mTime;
        //Time is in minutes in parameters
        private int mRoleSpeaking;
        private String mName;
        private int mSide;
        private TextView currentTimer;
        private CountDownTimer timer;
        private int mCurrentTime;
        private String timeText;

        //-1 con speaks
        //0 both sides speak
        //1 pro speaks
        public Speech(int time, int roleSpeaking, String name, int side) {
            mTime = 60 * time * 1000;
            mRoleSpeaking = roleSpeaking;
            mName = name;
            mSide = side;
            mCurrentTime = mTime;
            currentTimer = rootView.findViewById(R.id.currentTimer);
            timer = new CountDownTimer(mTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    currentTimer.setText(timeText);
                    mCurrentTime = (int) millisUntilFinished;
                }
                public void onFinish() {
                    currentStartStopButtonText = "DONE";
                    currentStartStopButton.setText(currentStartStopButtonText);
                }
            };
        }
        private int getTime() {
            return mTime;
        }
        private int getRoleSpeaking() {
            return mRoleSpeaking;
        }

        private String getTitle() {
            return mName;
        }

        private int getSide() {
            return mSide;
        }
        private int getCurrentTime() {
            return mCurrentTime;
        }
        private void setCurrentTime(int time) {
            mCurrentTime = time;
        }
        private void startTimer() {
            timer.start();
        }

        private void pauseTimer() {
            timer.cancel();
        }
        private void setCurrentTimerText(String time) {
            timeText = time;
            currentTimer.setText(time);
        }
        private void resumeTimer() {
            timer = new CountDownTimer(mCurrentTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    currentTimer.setText(timeText);
                    mCurrentTime = (int) millisUntilFinished;
                }

                public void onFinish() {
                    currentStartStopButtonText = "DONE";
                    currentStartStopButton.setText(currentStartStopButtonText);
                }
            }.start();
        }

        private void resetTimer() {
            timer.cancel();
            timer = new CountDownTimer(mTime, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeText = millisUntilFinished / (60 * 1000) + ":" + new DecimalFormat("00").format((int)(millisUntilFinished % (60 * 1000)/1000));
                    currentTimer.setText(timeText);
                    mCurrentTime = (int) millisUntilFinished;
                }

                public void onFinish() {
                    currentStartStopButtonText = "DONE";
                    currentStartStopButton.setText(currentStartStopButtonText);
                }
            };
            timeText = mTime / (60 * 1000) + ":" + new DecimalFormat("00").format(mTime % (60 * 1000)/1000);
            currentTimer.setText(timeText);
        }
        private void cancelTimer() {
            timer.cancel();
        }
    }

    public static DebateTimerFragment newInstance(int debateStyle, boolean proGoesFirst, int position, boolean isNSDA, int rebuttalPosition, int debatingAlone) {
        Bundle bundle = new Bundle();
        bundle.putInt("debateStyle", debateStyle);
        bundle.putBoolean("proGoesFirst", proGoesFirst);
        bundle.putInt("position", position);
        bundle.putBoolean("isNSDA", isNSDA);
        bundle.putInt("rebuttalPosition", rebuttalPosition);
        bundle.putInt("debatingAlone", debatingAlone);
        DebateTimerFragment frag = new DebateTimerFragment();
        frag.setArguments(bundle);
        return frag;
    }

    private void refreshSpeech() {
        TextView youAreCurrentlySpeaking = rootView.findViewById(R.id.youAreCurrent);
        youAreCurrentlySpeaking.setVisibility(View.GONE);
        Speech currentPart = round.get(count);
        TextView currentSpeech = rootView.findViewById(R.id.currentSpeech);
        currentSpeech.setText(currentPart.getTitle());
        TextView currentSpeaker = rootView.findViewById(R.id.currentSpeaker);
        String currentSpeakerText = "";
        if (currentPart.getSide() == 0) {
            currentSpeakerText = "Both sides";
        } else if (currentPart.getSide() == -1) {
            currentSpeakerText = "CON";
        } else {
            currentSpeakerText = "PRO";
        }
        if (debateStyle != 1 && debateStyle != 4 && debateStyle != 3 && round.get(count).getRoleSpeaking() != 7) {
            if (currentPart.getRoleSpeaking() == 0 || currentPart.getRoleSpeaking() == 3) {
                currentSpeakerText += ": Speaker 1";
            } else if (currentPart.getRoleSpeaking() == 1 || currentPart.getRoleSpeaking() == 4) {
                currentSpeakerText += ": Speaker 2";
            } else if (currentPart.getRoleSpeaking() == 2 || currentPart.getRoleSpeaking() == 5) {
                currentSpeakerText += ": Speaker 3";
            }
        }
        if (currentPart.getTitle().equals("Grand Crossfire")) {
            currentSpeakerText = "Everyone";
        }
        currentSpeaker.setText(currentSpeakerText);
        if (position == currentPart.getRoleSpeaking() || (currentPart.getSide() == 0 && position - 3 == currentPart.getRoleSpeaking()) || currentSpeakerText.equals("Everyone")) {
            youAreCurrentlySpeaking.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        onSavedInstanceState.putInt("count", count);
        onSavedInstanceState.putInt("myPrep", myPrep);
        onSavedInstanceState.putInt("enemyPrep", enemyPrep);
        onSavedInstanceState.putInt("currentTime", round.get(count).getCurrentTime());
        onSavedInstanceState.putString("currentStartStopButtonText", currentStartStopButtonText);
        onSavedInstanceState.putString("myStartStopButtonText", myStartStopButtonText);
        onSavedInstanceState.putString("enemyStartStopButtonText", enemyStartStopButtonText);
        onSavedInstanceState.putString("nextButtonText", nextButtonText);
    }
    public interface onEndDebateListener {
        public void onEndDebate();
    }
    public void setOnEndDebateListener(onEndDebateListener listen) {
        endDebateListener = listen;
    }
}
