package com.example.debatetrackerog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InfoCollector extends AppCompatActivity {
    private int MainMenuFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_collector);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        MainMenuFrag = getIntent().getIntExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, -1);
        int debateStyle = pref.getInt(GlobalDataKeys.DEBATE_STYLE_KEY, -1);
        TextView debateStyleText = findViewById(R.id.infoDebateStyleText);
        if(debateStyle == 0) {
            debateStyleText.setText("Default Debate Style:\nPublic Forum");
        }
        else if(debateStyle == 1) {
            debateStyleText.setText("Default Debate Style:\nLincoln Douglas");
        }
        else if(debateStyle == 2) {
            debateStyleText.setText("Default Debate Style:\nPolicy");
        }
        else if(debateStyle == 3) {
            debateStyleText.setText("Default Debate Style:\nBig Questions");
        }
        else if(debateStyle == 4) {
            debateStyleText.setText("Default Debate Style:\nExtemporaneous");
        }
        else if(debateStyle == 5) {
            debateStyleText.setText("Default Debate Style:\nWorld Schools");
        }
        else {
            debateStyleText.setText("No Default Debate Style");
        }
        Spinner gradeSpinner = findViewById(R.id.gradeSpinner);
        ArrayAdapter gradeAdapter = ArrayAdapter.createFromResource(this, R.array.grades_array, android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradeAdapter);
        int grade = pref.getInt(GlobalDataKeys.GRADE_KEY, 0);
        if(grade != 0) {
            if(grade == 9) {
                gradeSpinner.setSelection(1);
            }
            else if(grade == 10) {
                gradeSpinner.setSelection(2);
            }
            else if(grade == 11) {
                gradeSpinner.setSelection(3);
            }
            else if(grade == 12) {
                gradeSpinner.setSelection(4);
            }
            else if(grade == 13) {
                gradeSpinner.setSelection(5);
            }
        }
        String name = pref.getString(GlobalDataKeys.NAME_KEY, "None");
        if(!name.equals("None")) {
            ((EditText)findViewById(R.id.inputName)).setText(name);
        }
        String school = pref.getString(GlobalDataKeys.USER_SCHOOL_KEY, "None");
        if(!school.equals("None")) {
            ((EditText)findViewById(R.id.inputSchool)).setText(school);
        }
        String pronoun = pref.getString("Pronoun", "None");
        if(!pronoun.equals("None")) {
            ((EditText)findViewById(R.id.inputPronoun)).setText(pronoun);
        }
        String yearsDebating = "" + pref.getInt("Years Debating", 100);
        if(!yearsDebating.equals("100")) {
            ((EditText)findViewById(R.id.inputYearsDebating)).setText(yearsDebating);
        }
        String funFact = pref.getString("Fun Fact", "None");
        if(!funFact.equals("None")) {
            ((EditText)findViewById(R.id.inputFunFacts)).setText(funFact);
        }
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int grade = 8;
                if(position == 1) {
                    grade = 9;
                }
                else if(position == 2) {
                    grade = 10;
                }
                else if(position == 3) {
                    grade = 11;
                }
                else if(position == 4) {
                    grade = 12;
                }
                else if(position == 5) {
                    grade = 13;
                }
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(InfoCollector.this);
                SharedPreferences.Editor edit = pref.edit();
                if(pref.getInt(GlobalDataKeys.GRADE_KEY, -1) != -1) {
                    edit.remove(GlobalDataKeys.GRADE_KEY).apply();
                }
                edit.putInt(GlobalDataKeys.GRADE_KEY, grade).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private boolean isFilled(EditText edit) {
        if(edit.getText().toString().trim().equals("")) {
            return false;
        }
        return true;
    }
    public void submitInfo(View v) {
        EditText name = findViewById(R.id.inputName);
        EditText pronoun = findViewById(R.id.inputPronoun);
        EditText yearsDebating = findViewById(R.id.inputYearsDebating);
        EditText funFact = findViewById(R.id.inputFunFacts);
        EditText schoolName = findViewById(R.id.inputSchool);
        if(isFilled(name) && isFilled(pronoun) && isFilled(yearsDebating) && isFilled(funFact) && isFilled(schoolName)) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(InfoCollector.this);
            SharedPreferences.Editor edit = pref.edit();
            if(!pref.getString(GlobalDataKeys.NAME_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.NAME_KEY).apply();
            }
            edit.putString(GlobalDataKeys.NAME_KEY, name.getText().toString().trim()).apply();
            if(!pref.getString(GlobalDataKeys.PRONOUN_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.PRONOUN_KEY).apply();
            }
            edit.putString(GlobalDataKeys.PRONOUN_KEY, pronoun.getText().toString().trim()).apply();
            if(pref.getInt(GlobalDataKeys.YEARS_KEY, -1) != -1) {
                edit.remove(GlobalDataKeys.YEARS_KEY).apply();
            }
            edit.putInt(GlobalDataKeys.YEARS_KEY, Integer.parseInt(yearsDebating.getText().toString().trim())).apply();
            if(!pref.getString(GlobalDataKeys.FUN_FACT_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.FUN_FACT_KEY).apply();
            }
            edit.putString(GlobalDataKeys.FUN_FACT_KEY, funFact.getText().toString().trim()).apply();
            if(!pref.getString(GlobalDataKeys.USER_SCHOOL_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.USER_SCHOOL_KEY).apply();
            }
            edit.putString(GlobalDataKeys.USER_SCHOOL_KEY, schoolName.getText().toString().trim()).apply();
            Intent i = new Intent(InfoCollector.this, MainMenu.class);
            i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, MainMenuFrag);
            startActivity(i);
        }
        else {
            String error = "";
            if(!isFilled(name)) {
                error = "Name/Nickname";
            }
            else if(!isFilled(pronoun)) {
                error = "Preferred Pronoun";
            }
            else if(!isFilled(schoolName)) {
                error = "School Name";
            }
            else if(!isFilled(yearsDebating)) {
                error = "Years Debating";
            }
            else {
                error = "Fun Fact";
            }
            Toast.makeText(this, "Missing " + error, Toast.LENGTH_SHORT).show();
        }
    }
}
