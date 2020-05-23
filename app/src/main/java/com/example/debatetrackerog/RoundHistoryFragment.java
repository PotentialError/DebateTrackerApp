package com.example.debatetrackerog;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class RoundHistoryFragment extends Fragment {
    private View rootView;
    private ArrayList<DebateRound> rounds;
    private static final String NEW_INSTANCE_ROUNDS_KEY = "newInstance";
    private static final String SAVED_ROUNDS_KEY = "savedRounds";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            rounds = new Gson().fromJson(savedInstanceState.getString(SAVED_ROUNDS_KEY), new TypeToken<ArrayList<DebateRound>>() {}.getType());;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_round_history, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.debateRoundRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        if(rounds == null) {
            rounds = new Gson().fromJson(getArguments().getString(NEW_INSTANCE_ROUNDS_KEY), new TypeToken<ArrayList<DebateRound>>() {}.getType());
        }
        DebateRoundAdapter mAdapter = new DebateRoundAdapter(rounds);
        recyclerView.setAdapter(mAdapter);
        changeText();
        return rootView;
    }
    public static RoundHistoryFragment newInstance(String rounds) {
        Bundle bundle = new Bundle();
        bundle.putString(NEW_INSTANCE_ROUNDS_KEY, rounds);
        RoundHistoryFragment frag = new RoundHistoryFragment();
        frag.setArguments(bundle);
        return frag;
    }
    public void changeText() {
        TextView hint = rootView.findViewById(R.id.menuHint);
        if(MainMenu.roundOption == -1) {
            hint.setText("Click on the debate round you want to delete");
        }
        else if(MainMenu.roundOption == 1) {
            hint.setText("Click on the debate round you want to edit");
        }
        else if(rounds.size() > 0) {
            hint.setText("When you know the result of your rounds, record the result under Edit Rounds");
        }
        else {
            hint.setText("Click the three bars at the top to start your first debate");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SAVED_ROUNDS_KEY, new Gson().toJson(rounds));
    }
}
