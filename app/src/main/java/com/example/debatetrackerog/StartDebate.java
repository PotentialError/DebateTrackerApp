package com.example.debatetrackerog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class StartDebate extends AppCompatActivity implements SettingUpDebateFragment.onSetupFeedBackListener, DebateTimerFragment.onEndDebateListener {
    FragmentManager fm;
    DebateRound round;
    Fragment frag;
    boolean isEdit;
    int positionInHistory;
    private static final String FRAG_KEY = "frag";
    private static final String JSON_KEY = "json";
    public static final String ROUND_KEY = "round";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_debate);
        fm = getSupportFragmentManager();
        isEdit = getIntent().getBooleanExtra(GlobalDataKeys.IS_EDIT_KEY, false);
        if(isEdit) {
            positionInHistory = getIntent().getIntExtra(DebateRoundAdapter.POSITION_KEY, -1);
        }
        else {
            positionInHistory = -1;
        }
        if (savedInstanceState != null) {
            frag = getSupportFragmentManager().getFragment(savedInstanceState, FRAG_KEY);
            round = new Gson().fromJson(savedInstanceState.getString(JSON_KEY), new TypeToken<DebateRound>() {} .getType());
        } else {
            frag = SettingUpDebateFragment.newInstance(positionInHistory);
        }
        fm.beginTransaction().replace(R.id.startDebateLayout, frag).commit();

    }

    @Override
    public void onSetupFeedBack(int position, int debateStyle, boolean proSpeaksFirst, String opponent1, String opponent2, String opponent3, String opponentSchool, String tournament, boolean isNSDA, int rebuttalPosition, int debatingAlone, String winLoss, String date) {
        String proCon = "Con";
        if ((debateStyle == 0 && ((proSpeaksFirst && position < 3) || (!proSpeaksFirst && position > 2))) || (debateStyle != 0 && position < 3)) {
            proCon = "Pro";
        }
        String opponents = opponent1;
        if (!opponent2.equals("")) {
            opponents += "\n" + opponent2;
            if (!opponent3.equals("")) {
                opponents += "\n" + opponent3;
            }
        }
        String debateStyleText = "";
        if (debateStyle == 0) {
            debateStyleText = "Public Forum";
        } else if (debateStyle == 1) {
            debateStyleText = "Lincoln Douglas";
        } else if (debateStyle == 2) {
            debateStyleText = "Policy";
        } else if (debateStyle == 3) {
            debateStyleText = "Big Questions";
        } else if (debateStyle == 4) {
            debateStyleText = "Extemporaneous";
        } else if (debateStyle == 5) {
            debateStyleText = "World Schools";
        }
        round = new DebateRound(tournament, opponents, opponentSchool, debateStyleText, proCon, winLoss, date);
        if(isEdit) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = pref.edit();
            String roundsJson = pref.getString(GlobalDataKeys.ROUNDS_KEY, null);
            ArrayList<DebateRound> rounds = new Gson().fromJson(roundsJson, new TypeToken<ArrayList<DebateRound>>() {} .getType());
            rounds.set(positionInHistory, round);
            if(!pref.getString(GlobalDataKeys.ROUNDS_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.ROUNDS_KEY).apply();
            }
            edit.putString(GlobalDataKeys.ROUNDS_KEY, new Gson().toJson(rounds)).apply();
            Intent i = new Intent(this, MainMenu.class);
            i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, 1);
            startActivity(i);
        }
        else {
            frag = DebateTimerFragment.newInstance(debateStyle, proSpeaksFirst, position, isNSDA, rebuttalPosition, debatingAlone);
            fm.beginTransaction().replace(R.id.startDebateLayout, frag).commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof SettingUpDebateFragment) {
            ((SettingUpDebateFragment) fragment).setOnSetupFeedBackListener(this);
        }
        if (fragment instanceof DebateTimerFragment) {
            ((DebateTimerFragment) fragment).setOnEndDebateListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        if (frag.isAdded()) {
            getSupportFragmentManager().putFragment(onSavedInstanceState, FRAG_KEY, frag);
        }
        String json = new Gson().toJson(round);
        onSavedInstanceState.putString(JSON_KEY, json);
    }

    @Override
    public void onEndDebate() {
        Intent i = new Intent(this, MainMenu.class);
        i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, 0);
        Gson gson = new Gson();
        String json = gson.toJson(round);
        i.putExtra(ROUND_KEY, json);
        startActivity(i);
    }
}
