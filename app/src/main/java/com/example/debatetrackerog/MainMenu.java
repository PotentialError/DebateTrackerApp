package com.example.debatetrackerog;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.onEditProfileListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Fragment frag;
    private ArrayList<DebateRound> rounds;
    //-1 = remove rounds 0 = look at rounds 1 = edit rounds
    public static int roundOption;

    private static final String SAVED_FRAG_KEY = "frag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        drawerLayout = findViewById(R.id.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigator);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        String roundsJson = pref.getString(GlobalDataKeys.ROUNDS_KEY, null);
        rounds = new Gson().fromJson(roundsJson, new TypeToken<ArrayList<DebateRound>>() {} .getType());
        if(rounds == null) {
            rounds = new ArrayList<>();
        }
        if(savedInstanceState == null) {
            String roundJson = getIntent().getStringExtra(StartDebate.ROUND_KEY);
            DebateRound debateRound = new Gson().fromJson(roundJson, new TypeToken<DebateRound>() {}.getType());
            if(debateRound != null) {
                rounds.add(0, debateRound);
            }
            if(!pref.getString(GlobalDataKeys.ROUNDS_KEY, "").equals("")) {
                edit.remove(GlobalDataKeys.ROUNDS_KEY).apply();
            }
            edit.putString(GlobalDataKeys.ROUNDS_KEY, new Gson().toJson(rounds)).apply();
            int fragmentType = getIntent().getIntExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, 0);
            if(fragmentType == 0) {
                roundOption = 0;
                frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            }
            else if(fragmentType == 1) {
                roundOption = 1;
                frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            }
            else {
                frag = new ProfileFragment();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.mainMenuFrameLayout, frag).commit();
        }
        else {
            frag = getSupportFragmentManager().getFragment(savedInstanceState, SAVED_FRAG_KEY);
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            roundOption = 0;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            String roundsJson = pref.getString(GlobalDataKeys.ROUNDS_KEY, null);
            rounds = new Gson().fromJson(roundsJson, new TypeToken<ArrayList<DebateRound>>() {} .getType());
            frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(itemID == R.id.addDebateNav) {
            Intent i = new Intent(this, StartDebate.class);
            i.putExtra(GlobalDataKeys.IS_EDIT_KEY, false);
            startActivity(i);
        }
        else if(itemID == R.id.lookDebateNav) {
            roundOption = 0;
            String roundsJson = pref.getString(GlobalDataKeys.ROUNDS_KEY, null);
            rounds = new Gson().fromJson(roundsJson, new TypeToken<ArrayList<DebateRound>>() {} .getType());
            frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
        else if(itemID == R.id.removeDebateNav) {
            roundOption = -1;
            frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
        else if(itemID == R.id.profileNav) {
            frag = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
        else if(itemID == R.id.editDebateNav) {
            roundOption = 1;
            frag = RoundHistoryFragment.newInstance(new Gson().toJson(rounds));
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
        else if(itemID == R.id.aAndQNav) {
            roundOption = 0;
            frag = new QAFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainMenuFrameLayout, frag).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle onSavedInstanceState) {
        super.onSaveInstanceState(onSavedInstanceState);
        if (frag.isAdded()) {
            getSupportFragmentManager().putFragment(onSavedInstanceState, SAVED_FRAG_KEY, frag);
        }
    }

    @Override
    public void onEditProfile() {
        //-1 go to profile; 0 go to debate history 1 = go to edit debates
        Intent i = new Intent(this, ChooseDebateActivity.class);
        i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, -1);
        startActivity(i);
    }

    @Override
    public void onAttachFragment(Fragment frag) {
        if(frag instanceof ProfileFragment) {
            ((ProfileFragment)frag).setOnEditProfileListener(this);
        }
    }
}
