package com.example.debatetrackerog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private int count;
    private long startMillis;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_UP) {
            long time= System.currentTimeMillis();
            if (startMillis==0 || (time-startMillis> 4000) ) {
                startMillis=time;
                count=1;
            }
            else{
                count++;
            }

            if (count==5) {
                clearAll(null);
            }
            return true;
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = 0;
        startMillis = 0;
    }
    public void chooseDebateStyle(View v) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String name = pref.getString("Name", "");
        Intent i;
        MainMenu.roundOption = 0;
        if(!name.equals("")) {
            i = new Intent(this, MainMenu.class);
        }
        else {
            i = new Intent(this, ChooseDebateActivity.class);
        }
        i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, 0);
        startActivity(i);
    }
    public void clearAll(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete All Data");
        alert.setMessage("Are you sure you want to delete?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor edit = pref.edit();
                edit.clear().apply();
                Toast.makeText(MainActivity.this, "All data deleted", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

}
