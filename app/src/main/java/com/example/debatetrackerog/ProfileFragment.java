package com.example.debatetrackerog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ProfileFragment extends Fragment {
    private onEditProfileListener editProfileListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        int grade = pref.getInt(GlobalDataKeys.GRADE_KEY, 0);
        String gradeSchool;
        if (grade == 8) {
            gradeSchool = "Middle Schooler";
        } else if (grade == 9) {
            gradeSchool = "Freshman";
        } else if (grade == 10) {
            gradeSchool = "Sophomore";
        } else if (grade == 11) {
            gradeSchool = "Junior";
        } else if (grade == 12) {
            gradeSchool = "Senior";
        } else {
            gradeSchool = "Adult";
        }
        gradeSchool += " from " + pref.getString(GlobalDataKeys.USER_SCHOOL_KEY, "None");
        ((TextView) rootView.findViewById(R.id.profileSchoolGrade)).setText(gradeSchool);
        ((TextView) rootView.findViewById(R.id.profileName)).setText(pref.getString(GlobalDataKeys.NAME_KEY, "None"));
        ((TextView) rootView.findViewById(R.id.profileFunFact)).setText(pref.getString(GlobalDataKeys.FUN_FACT_KEY, "None"));
        ((TextView) rootView.findViewById(R.id.profilePronoun)).setText(pref.getString(GlobalDataKeys.PRONOUN_KEY, "None"));
        String tempText = "Debating for " + pref.getInt(GlobalDataKeys.YEARS_KEY, 0) + " years";
        ((TextView) rootView.findViewById(R.id.profileYearsDebating)).setText(tempText);
        int debateStyle = pref.getInt(GlobalDataKeys.DEBATE_STYLE_KEY, -1);
        TextView debateStyleText = rootView.findViewById(R.id.profileDebateStyle);
        if (debateStyle == 0) {
            debateStyleText.setText("Public Forum");
        } else if (debateStyle == 1) {
            debateStyleText.setText("Lincoln Douglas");
        } else if (debateStyle == 2) {
            debateStyleText.setText("Policy");
        } else if (debateStyle == 3) {
            debateStyleText.setText("Big Questions");
        } else if (debateStyle == 4) {
            debateStyleText.setText("Extemporaneous");
        } else if (debateStyle == 5) {
            debateStyleText.setText("World Schools");
        } else {
            debateStyleText.setText("No Default Debate Style");
        }
        tempText = "Wins: " + pref.getInt(GlobalDataKeys.WIN_KEY, 0) + "   Losses: " + pref.getInt(GlobalDataKeys.LOSS_KEY, 0);
        ((TextView)rootView.findViewById(R.id.winLossText)).setText(tempText);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.profileEdit) {
                    editProfileListener.onEditProfile();
                }
            }
        };
        rootView.findViewById(R.id.profileEdit).setOnClickListener(listener);
        return rootView;
    }

    public interface onEditProfileListener {
        public void onEditProfile();
    }

    public void setOnEditProfileListener(onEditProfileListener listener) {
        editProfileListener = listener;
    }
}
