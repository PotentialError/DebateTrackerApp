package com.example.debatetrackerog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DebateRoundAdapter extends RecyclerView.Adapter<DebateRoundAdapter.DebateRoundViewHolder> {
    private ArrayList<DebateRound> mRounds;
    public static final String POSITION_KEY = "position";
    public DebateRoundAdapter(ArrayList<DebateRound> array) {
        mRounds = array;
    }
    @Override
    public DebateRoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.debate_round_item, parent, false);
        return new DebateRoundViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DebateRoundViewHolder holder, int position) {
        DebateRound debateRound = mRounds.get(position);
        holder.bindDebateRound(debateRound);
    }
    @Override
    public int getItemCount() {
        return mRounds.size();
    }

    private void removeAt(int position) {
        mRounds.remove(position);
        notifyItemRemoved(position);
    }
    public class DebateRoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mProCon;
        //0 = N/A 1 = Loss 2 = Win
        private TextView mWinLoss;
        private TextView mDebateStyle;
        private TextView mOpponentNames;
        private TextView mTournamentName;
        private TextView mSchoolName;
        private DebateRound mDebateRound;
        private TextView mDebateDisplay;
        private View mRootView;
        public DebateRoundViewHolder(View rootView) {
            super(rootView);
            rootView.setOnClickListener(this);
            mRootView = rootView;
            mProCon = rootView.findViewById(R.id.proCon);
            mWinLoss = rootView.findViewById(R.id.winLoss);
            mDebateStyle = rootView.findViewById(R.id.debateStyle);
            mOpponentNames = rootView.findViewById(R.id.opponentNames);
            mTournamentName = rootView.findViewById(R.id.tournamentName);
            mSchoolName = rootView.findViewById(R.id.opponentSchool);
            mDebateDisplay = rootView.findViewById(R.id.dateDisplay);
        }
        public void bindDebateRound(DebateRound debateRound) {
            mDebateRound = debateRound;
            if(mDebateRound.getTournamentName().equals("")) {
                mRootView.findViewById(R.id.tournamentNameTitle).setVisibility(View.GONE);
                mTournamentName.setVisibility(View.GONE);
            }
            else {
                mTournamentName.setText(mDebateRound.getTournamentName());
            }
            mOpponentNames.setText(mDebateRound.getOpponentNames());
            mProCon.setText(mDebateRound.getProCon());
            mSchoolName.setText(mDebateRound.getSchoolName());
            mDebateStyle.setText(mDebateRound.getDebateStyle());
            mWinLoss.setText(mDebateRound.getWinLoss());
            mDebateDisplay.setText(mDebateRound.getDate());
        }

        @Override
        public void onClick(View v) {
            if(MainMenu.roundOption == -1) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mRootView.getContext());
                alert.setTitle("Delete Round");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        removeAt(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyDataSetChanged();
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mRootView.getContext());
                        SharedPreferences.Editor edit = pref.edit();
                        if(!pref.getString(GlobalDataKeys.ROUNDS_KEY, "").equals("")) {
                            edit.remove(GlobalDataKeys.ROUNDS_KEY).apply();
                        }
                        edit.putString(GlobalDataKeys.ROUNDS_KEY, new Gson().toJson(mRounds)).apply();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
            else if(MainMenu.roundOption == 1) {
                Intent i = new Intent(mRootView.getContext(), StartDebate.class);
                i.putExtra(GlobalDataKeys.IS_EDIT_KEY, true);
                i.putExtra(POSITION_KEY, getAdapterPosition());
                mRootView.getContext().startActivity(i);
            }
        }
    }
}
