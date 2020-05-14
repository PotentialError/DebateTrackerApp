package com.example.debatetrackerog;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;


//In listener to determine speaker role and when to talk
//0 - 2 User's side speaks first
    //0 = User is speaker 1
    //1 = User is speaker 2
    //2= User is speaker 3
//3-5 Opponent's side speaks first
    //3 = User is speaker 1
    //4 = User is speaker 2
    //5 = User is speaker 3
//7 = Other side rebuttal person

//For debatingAlone
//0 = 1v1
//1 = 1v2
//2 = 2v1
//3 = 2v2
public class SettingUpDebateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private EditText opponentSchoolEdit;
    private EditText opponentOneEdit;
    private EditText opponentTwoEdit;
    private EditText opponentThreeEdit;
    private EditText tournamentEdit;
    private RadioButton firstSpeaker;
    private RadioButton secondSpeaker;
    private RadioButton thirdSpeaker;
    private RadioButton firstSideSpeak;
    private RadioButton secondSideSpeak;
    private RadioButton tournamentOptionSetup;
    private RadioButton practiceOptionSetup;
    private RadioButton NSDAOptionSetup;
    private RadioButton NCFLOptionSetup;
    private RadioButton proOptionSetup;
    private RadioButton conOptionSetup;
    private RadioButton firstRebutOption;
    private RadioButton secondRebutOption;
    private RadioButton thirdRebutOption;
    private RadioButton yesDebateAloneOption;
    private RadioButton noDebateAloneOption;
    private RadioButton winOption;
    private RadioButton lossOption;
    private RadioButton NAOption;
    private int debateStyle;
    private TextView datePreview;
    private String dateText;
    onSetupFeedBackListener listener;
    private boolean isTournament;
    private boolean isNCFL;
    View rootView;

    private static final String HISTORY_POSITION_KEY = "historyPosition";
    private static final String IS_NCFL_KEY = "isNCFL";
    private static final String IS_TOURNAMENT_KEY = "isTournament";
    private static final String DATE_KEY = "date";
    private DebateRound historyRound;
    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        if(onSavedInstanceState != null) {
            isTournament = onSavedInstanceState.getBoolean(IS_TOURNAMENT_KEY);
            isNCFL = onSavedInstanceState.getBoolean(IS_NCFL_KEY);
            dateText = onSavedInstanceState.getString(DATE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting_up_debate, container, false);
        if(dateText == null) {
            dateText = Calendar.getInstance().get(Calendar.MONTH) + "/" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + Calendar.getInstance().get(Calendar.YEAR);
        }
        datePreview = rootView.findViewById(R.id.dateTextPreview);

        opponentOneEdit = rootView.findViewById(R.id.inputOpponentOne);
        opponentTwoEdit = rootView.findViewById(R.id.inputOpponentTwo);
        opponentThreeEdit = rootView.findViewById(R.id.inputOpponentThree);
        tournamentEdit = rootView.findViewById(R.id.inputTournamentName);
        firstSpeaker = rootView.findViewById(R.id.firstSpeakerOption);
        secondSpeaker = rootView.findViewById(R.id.secondSpeakerOption);
        thirdSpeaker = rootView.findViewById(R.id.thirdSpeakerOption);
        firstSideSpeak = rootView.findViewById(R.id.firstSideSpeakOption);
        secondSideSpeak = rootView.findViewById(R.id.secondSideSpeakOption);
        tournamentOptionSetup = rootView.findViewById(R.id.tournamentOptionSetup);
        practiceOptionSetup = rootView.findViewById(R.id.practiceOptionSetup);
        NCFLOptionSetup = rootView.findViewById(R.id.NCFLOptionSetup);
        NSDAOptionSetup = rootView.findViewById(R.id.NSDAOptionSetup);
        opponentSchoolEdit = rootView.findViewById(R.id.inputOpponentSchool);
        proOptionSetup = rootView.findViewById(R.id.sidePROSetup);
        conOptionSetup = rootView.findViewById(R.id.sideCONSetup);
        firstRebutOption = rootView.findViewById(R.id.firstGivingRebutOption);
        secondRebutOption = rootView.findViewById(R.id.secondGivingRebutOption);
        thirdRebutOption = rootView.findViewById(R.id.thirdGivingRebutOption);
        yesDebateAloneOption = rootView.findViewById(R.id.yesAloneOption);
        noDebateAloneOption = rootView.findViewById(R.id.noAloneOption);
        winOption = rootView.findViewById(R.id.winSetup);
        lossOption = rootView.findViewById(R.id.lossSetup);
        NAOption = rootView.findViewById(R.id.NASetup);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        debateStyle = pref.getInt(GlobalDataKeys.DEBATE_STYLE_KEY, -1);

        if(isTournament) {
            tournamentEdit.setVisibility(View.VISIBLE);
        }
        else {
            tournamentEdit.setVisibility(View.GONE);
        }
        if(isNCFL) {
            rootView.findViewById(R.id.speakingOrderText).setVisibility(View.VISIBLE);
            firstSideSpeak.setVisibility(View.VISIBLE);
            secondSideSpeak.setVisibility(View.VISIBLE);
        }
        else {
            rootView.findViewById(R.id.speakingOrderText).setVisibility(View.GONE);
            firstSideSpeak.setVisibility(View.GONE);
            secondSideSpeak.setVisibility(View.GONE);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if(id == R.id.changeDateButton) {
                    showDatePicker();
                }
                else if(id == R.id.tournamentOptionSetup) {
                    tournamentEdit.setVisibility(View.VISIBLE);
                    isTournament = true;
                }
                else if(id == R.id.practiceOptionSetup) {
                    tournamentEdit.setVisibility(View.GONE);
                    tournamentEdit.getText().clear();
                    isTournament = false;
                }
                else if(id == R.id.NCFLOptionSetup) {
                    rootView.findViewById(R.id.speakingOrderText).setVisibility(View.GONE);
                    firstSideSpeak.setVisibility(View.GONE);
                    secondSideSpeak.setVisibility(View.GONE);
                    isNCFL = true;
                }
                else if(id == R.id.NSDAOptionSetup) {
                    rootView.findViewById(R.id.speakingOrderText).setVisibility(View.VISIBLE);
                    firstSideSpeak.setVisibility(View.VISIBLE);
                    secondSideSpeak.setVisibility(View.VISIBLE);
                    isNCFL = true;
                }
                else if(id == R.id.noAloneOption) {
                    rootView.findViewById(R.id.speakingRoleText).setVisibility(View.VISIBLE);
                    firstSpeaker.setVisibility(View.VISIBLE);
                    secondSpeaker.setVisibility(View.VISIBLE);
                }
                else if(id == R.id.yesAloneOption) {
                    rootView.findViewById(R.id.speakingRoleText).setVisibility(View.GONE);
                    firstSpeaker.setVisibility(View.GONE);
                    secondSpeaker.setVisibility(View.GONE);
                }
                else if(id == R.id.startDebateRound) {
                    String error = "";
                    if(winOption.getVisibility() == View.VISIBLE && !lossOption.isChecked() && !winOption.isChecked() && !NAOption.isChecked()) {
                        error = "Win, N/A, Loss";
                    }
                    else if(!proOptionSetup.isChecked() && !conOptionSetup.isChecked()) {
                        error = "Pro or Con";
                    }
                    else if(opponentSchoolEdit.getText().toString().trim().equals("")) {
                        error = "Opponent's School";
                    }
                    else if(opponentOneEdit.getText().toString().trim().equals("")) {
                        if(debateStyle == 1 || debateStyle == 4) {
                            error = "Opponent Name";
                        }
                        else {
                            error = "Opponent 1 Name";
                        }
                    }
                    else if((debateStyle == 0 || debateStyle == 2) && opponentTwoEdit.getText().toString().trim().equals("")) {
                        error = "Opponent 2 Name";
                    }
                    else if(debateStyle == 5 && opponentThreeEdit.getText().toString().trim().equals("")) {
                        error = "Opponent 3 Name";
                    }
                    else if(historyRound == null) {
                        if (!tournamentOptionSetup.isChecked() && !practiceOptionSetup.isChecked()) {
                            error = "Round Type";
                        } else if (debateStyle == 0 && !NSDAOptionSetup.isChecked() && !NCFLOptionSetup.isChecked()) {
                            error = "Rules Type";
                        }
                    }
                    else if(tournamentOptionSetup.isChecked() && tournamentEdit.getText().toString().trim().equals("")) {
                        error = "Tournament Name";
                    }
                    else if(historyRound == null) {
                        if(debateStyle == 0 && NSDAOptionSetup.isChecked() && !firstSideSpeak.isChecked() && !secondSideSpeak.isChecked()) {
                            error = "Speaking Order";
                        }
                        else if(yesDebateAloneOption.getVisibility() == View.VISIBLE && !yesDebateAloneOption.isChecked() && !noDebateAloneOption.isChecked()) {
                            error = "Debating Alone";
                        }
                        else if(firstSpeaker.getVisibility() == View.VISIBLE && !firstSpeaker.isChecked() && !secondSpeaker.isChecked() && !thirdSpeaker.isChecked()) {
                            error = "Speaking Role";
                        }
                        else if(firstRebutOption.getVisibility() == View.VISIBLE && !firstRebutOption.isChecked() && !secondRebutOption.isChecked() && !thirdRebutOption.isChecked()) {
                            error = "Rebuttal Role";
                        }
                    }

                    if(error.equals("")) {
                        submitInfo();
                    }
                    else {
                        error = "Missing " + error;
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        tournamentOptionSetup.setOnClickListener(listener);
        practiceOptionSetup.setOnClickListener(listener);
        NCFLOptionSetup.setOnClickListener(listener);
        NSDAOptionSetup.setOnClickListener(listener);
        yesDebateAloneOption.setOnClickListener(listener);
        noDebateAloneOption.setOnClickListener(listener);
        rootView.findViewById(R.id.startDebateRound).setOnClickListener(listener);
        rootView.findViewById(R.id.changeDateButton).setOnClickListener(listener);
        Spinner debateStyleSpinner = rootView.findViewById(R.id.debateStyleSpinner);
        ArrayAdapter gradeAdapter = ArrayAdapter.createFromResource(rootView.getContext(), R.array.debate_style_array, android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        debateStyleSpinner.setAdapter(gradeAdapter);
        debateStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    debateStyle = position - 1;
                }
                else {
                    debateStyle = pref.getInt(GlobalDataKeys.DEBATE_STYLE_KEY, -1);
                }
                if(debateStyle != 5) {
                    opponentThreeEdit.getText().clear();
                }
                if(debateStyle == 1 || debateStyle == 4) {
                    opponentTwoEdit.getText().clear();
                }
                addRemoveOptions(debateStyle);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int historyPosition = getArguments().getInt(HISTORY_POSITION_KEY);
        if(historyPosition != -1) {
            historyRound = ((ArrayList<DebateRound>)new Gson().fromJson(pref.getString(GlobalDataKeys.ROUNDS_KEY, ""), new TypeToken<ArrayList<DebateRound>>() {} .getType())).get(historyPosition);
            String debateStyleText = historyRound.getDebateStyle();
            if (debateStyleText.equals("Public Forum")) {
                debateStyle = 0;
            }
            else if (debateStyleText.equals("Lincoln Douglas")) {
                debateStyle = 1;
            }
            else if (debateStyleText.equals("Policy")) {
                debateStyle = 2;
            }
            else if (debateStyleText.equals("Big Questions")) {
                debateStyle = 3;
            }
            else if (debateStyleText.equals("Extemporaneous")) {
                debateStyle = 4;
            }
            else if (debateStyleText.equals("World Schools")) {
                debateStyle = 5;
            }
            debateStyleSpinner.setSelection(debateStyle + 1);
            ((TextView)rootView.findViewById(R.id.startDebateRound)).setText("Submit");
            if(historyRound.getTournamentName().equals("")) {
                practiceOptionSetup.setChecked(true);
            }
            else {
                tournamentOptionSetup.setChecked(true);
                tournamentEdit.setText(historyRound.getTournamentName());
            }
            if(historyRound.getProCon().equals("Pro")) {
                proOptionSetup.setChecked(true);
            }
            else {
                conOptionSetup.setChecked(true);
            }
            opponentSchoolEdit.setText(historyRound.getSchoolName());
            addRemoveOptions(debateStyle);
            String opponentNames = historyRound.getOpponentNames();
            if(!opponentNames.contains("\n")) {
                opponentOneEdit.setText(opponentNames);
                opponentNames = "";
            }
            else {
                opponentOneEdit.setText(opponentNames.substring(0, opponentNames.indexOf("\n")));
                opponentNames = opponentNames.substring(opponentNames.indexOf("\n") + 1);
            }
            if(opponentTwoEdit.getVisibility() == View.VISIBLE) {
                if(!opponentNames.contains("\n")) {
                    opponentTwoEdit.setText(opponentNames);
                }
                else {
                    opponentTwoEdit.setText(opponentNames.substring(0, opponentNames.indexOf("\n")));
                    opponentNames = opponentNames.substring(opponentNames.indexOf("\n") + 1);
                }
            }
            if(opponentThreeEdit.getVisibility() == View.VISIBLE) {
                opponentThreeEdit.setText(opponentNames);
            }
            if(historyRound.getWinLoss().equals("Win")) {
                winOption.setChecked(true);
            }
            else if(historyRound.getWinLoss().equals("Loss")) {
                lossOption.setChecked(true);
            }
            else {
                NAOption.setChecked(true);
            }
            datePreview.setText(historyRound.getDate());
        }
        addRemoveOptions(debateStyle);
        return rootView;
    }
    private void addRemoveOptions(int typeOfDebate) {
        if(typeOfDebate == 5) {
            opponentThreeEdit.setVisibility(View.VISIBLE);
            thirdSpeaker.setVisibility(View.VISIBLE);
        }
        else {
            opponentThreeEdit.setVisibility(View.GONE);
            thirdSpeaker.setVisibility(View.GONE);
        }
        if(typeOfDebate == 1 || typeOfDebate == 4) {
            opponentTwoEdit.setVisibility(View.GONE);
            opponentOneEdit.setHint("Opponent Name");
            secondSpeaker.setVisibility(View.GONE);
            firstSpeaker.setVisibility(View.GONE);
            rootView.findViewById(R.id.speakingRoleText).setVisibility(View.GONE);
        }
        else {
            opponentTwoEdit.setVisibility(View.VISIBLE);
            secondSpeaker.setVisibility(View.VISIBLE);
            firstSpeaker.setVisibility(View.VISIBLE);
            opponentOneEdit.setHint("Opponent 1 Name");
            rootView.findViewById(R.id.speakingRoleText).setVisibility(View.VISIBLE);
        }
        if(typeOfDebate == 3) {
            opponentTwoEdit.setHint("Opponent 2 Name (Optional)");
            rootView.findViewById(R.id.speakingAloneText).setVisibility(View.VISIBLE);
            yesDebateAloneOption.setVisibility(View.VISIBLE);
            noDebateAloneOption.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.speakingRoleText).setVisibility(View.GONE);
            firstSpeaker.setVisibility(View.GONE);
            secondSpeaker.setVisibility(View.GONE);
        }
        else {
            opponentTwoEdit.setHint("Opponent 2 Name");
            rootView.findViewById(R.id.speakingAloneText).setVisibility(View.GONE);
            yesDebateAloneOption.setVisibility(View.GONE);
            noDebateAloneOption.setVisibility(View.GONE);
        }
        if(typeOfDebate == 0) {
            NSDAOptionSetup.setVisibility(View.VISIBLE);
            NCFLOptionSetup.setVisibility(View.VISIBLE);
        }
        else {
            NSDAOptionSetup.setVisibility(View.GONE);
            NCFLOptionSetup.setVisibility(View.GONE);
        }
        if(typeOfDebate == 5) {
            rootView.findViewById(R.id.rebutRoleText).setVisibility(View.VISIBLE);
            firstRebutOption.setVisibility(View.VISIBLE);
            secondRebutOption.setVisibility(View.VISIBLE);
            thirdRebutOption.setVisibility(View.VISIBLE);
        }
        else {
            rootView.findViewById(R.id.rebutRoleText).setVisibility(View.GONE);
            firstRebutOption.setVisibility(View.GONE);
            secondRebutOption.setVisibility(View.GONE);
            thirdRebutOption.setVisibility(View.GONE);
        }
        if(historyRound != null) {
            if(historyRound.getTournamentName().equals("")) {
                tournamentEdit.setVisibility(View.GONE);
            }
            else {
                tournamentEdit.setVisibility(View.VISIBLE);
            }
            rootView.findViewById(R.id.rebutRoleText).setVisibility(View.GONE);
            firstRebutOption.setVisibility(View.GONE);
            secondRebutOption.setVisibility(View.GONE);
            thirdRebutOption.setVisibility(View.GONE);
            NSDAOptionSetup.setVisibility(View.GONE);
            NCFLOptionSetup.setVisibility(View.GONE);
            rootView.findViewById(R.id.speakingAloneText).setVisibility(View.GONE);
            yesDebateAloneOption.setVisibility(View.GONE);
            noDebateAloneOption.setVisibility(View.GONE);
            thirdSpeaker.setVisibility(View.GONE);
            secondSpeaker.setVisibility(View.GONE);
            firstSpeaker.setVisibility(View.GONE);
            rootView.findViewById(R.id.speakingRoleText).setVisibility(View.GONE);
        }
        else {
            winOption.setVisibility(View.GONE);
            lossOption.setVisibility(View.GONE);
            NAOption.setVisibility(View.GONE);
        }
    }
    private void showDatePicker() {
        if(historyRound == null) {
            new DatePickerDialog(getContext(), this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
        }
        else {
            String date = historyRound.getDate();
            int month = Integer.parseInt(date.substring(0, date.indexOf("/")));
            date = date.substring(date.indexOf("/") + 1);
            int day = Integer.parseInt(date.substring(0, date.indexOf("/")));
            date = date.substring(date.indexOf("/") + 1);
            int year = Integer.parseInt(date);
            new DatePickerDialog(getContext(), this, year, month - 1, day).show();
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateText = month + 1 + "/" + dayOfMonth + "/" + year;
        datePreview.setText(dateText);
    }

    public interface onSetupFeedBackListener {
        public void onSetupFeedBack(int position, int debateStyle, boolean proSpeaksFirst, String opponent1, String opponent2, String opponent3, String opponentSchool, String tournament, boolean isNSDA, int personGivingRebuttal, int debatingAlone, String winLoss, String date);
    }
    public void setOnSetupFeedBackListener(onSetupFeedBackListener listen) {
        listener = listen;
    }
    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        onSavedInstanceState.putBoolean(IS_NCFL_KEY, isNCFL);
        onSavedInstanceState.putBoolean(IS_TOURNAMENT_KEY, isTournament);
        onSavedInstanceState.putString(DATE_KEY, dateText);
    }
    private void submitInfo() {
        int position = 0;
        boolean proSpeaksFirst = true;
        boolean isNSDA = true;
        int rebuttalPosition = 0;
        //Only Big Questions
        int debatingAlone = 0;
        if(proOptionSetup.isChecked()) {
            if(noDebateAloneOption.isChecked()) {
                if(opponentTwoEdit.getText().toString().trim().equals("")) {
                    debatingAlone = 2;
                }
                else {
                    debatingAlone = 3;
                }
            }
            else if(yesDebateAloneOption.isChecked() && !opponentTwoEdit.getText().toString().trim().equals("")){
                debatingAlone = 1;
            }
        }
        else {
            if(noDebateAloneOption.isChecked()) {
                if(opponentTwoEdit.getText().toString().trim().equals("")) {
                    debatingAlone = 1;
                }
                else {
                    debatingAlone = 3;
                }
            }
            else if(yesDebateAloneOption.isChecked() && !opponentTwoEdit.getText().toString().trim().equals("")){
                debatingAlone = 2;
            }
        }
        if((conOptionSetup.isChecked() && debateStyle!= 0) || (debateStyle == 0 && ((conOptionSetup.isChecked() && NCFLOptionSetup.isChecked()) || (secondSideSpeak.isChecked() && NSDAOptionSetup.isChecked())))) {
            position = 3;
        }
        if(debateStyle != 1 && debateStyle != 4 && secondSpeaker.isChecked() || (debateStyle == 3 && noDebateAloneOption.isChecked() && secondSpeaker.isChecked())) {
            position+=1;
        }
        else if(debateStyle == 5 && thirdSpeaker.isChecked()){
            position+=2;
        }
        if((secondSideSpeak.isChecked() && NSDAOptionSetup.isChecked() && proOptionSetup.isChecked()) || (firstSideSpeak.isChecked() && NSDAOptionSetup.isChecked() && conOptionSetup.isChecked())) {
            proSpeaksFirst = false;
        }
        if(NCFLOptionSetup.isChecked()) {
            isNSDA = false;
        }
        if(position < 3) {
            if(secondRebutOption.isChecked()) {
                rebuttalPosition = 1;
            }
            else {
                rebuttalPosition = 2;
            }
        }
        else {
            if(firstRebutOption.isChecked()) {
                rebuttalPosition = 3;
            }
            else if(secondRebutOption.isChecked()) {
                rebuttalPosition = 4;
            }
            else {
                rebuttalPosition = 5;
            }
        }
        String winLoss = "N/A";
        if(historyRound != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor edit = pref.edit();
            if(winOption.isChecked()) {
                winLoss = "Win";
                int winCount = pref.getInt(GlobalDataKeys.WIN_KEY, 0);
                if(winCount != 0) {
                    edit.remove(GlobalDataKeys.WIN_KEY).apply();
                }
                winCount++;
                edit.putInt(GlobalDataKeys.WIN_KEY, winCount).apply();
            }
            else if(lossOption.isChecked()) {
                winLoss = "Loss";
                int lossCount = pref.getInt(GlobalDataKeys.LOSS_KEY, 0);
                if(lossCount != 0) {
                    edit.remove(GlobalDataKeys.LOSS_KEY).apply();
                }
                lossCount++;
                edit.putInt(GlobalDataKeys.LOSS_KEY, lossCount).apply();
            }
        }
        listener.onSetupFeedBack(position, debateStyle, proSpeaksFirst, opponentOneEdit.getText().toString().trim(), opponentTwoEdit.getText().toString().trim(), opponentThreeEdit.getText().toString().trim(), opponentSchoolEdit.getText().toString().trim(), tournamentEdit.getText().toString().trim(), isNSDA, rebuttalPosition, debatingAlone, winLoss, dateText);
    }

    public static SettingUpDebateFragment newInstance(int historyPosition) {
        SettingUpDebateFragment frag = new SettingUpDebateFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HISTORY_POSITION_KEY, historyPosition);
        frag.setArguments(bundle);
        return frag;
    }
}
