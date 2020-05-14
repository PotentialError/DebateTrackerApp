package com.example.debatetrackerog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DebateStyleAdapter extends RecyclerView.Adapter<DebateStyleAdapter.DebateStyleViewHolder> {
    private ArrayList<DebateStyle> mDebateStyles;
    private int MainMenuFrag;
    public DebateStyleAdapter(ArrayList<DebateStyle> array) {
        mDebateStyles = array;
    }
    @Override
    public DebateStyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.debate_style_item, parent, false);
        return new DebateStyleViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder( DebateStyleViewHolder holder, int position) {
        DebateStyle debateStyle = mDebateStyles.get(position);
        holder.bindDebateStyle(debateStyle);
    }
    @Override
    public int getItemCount() {
        return mDebateStyles.size();
    }
    public class DebateStyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mLogo;
        private DebateStyle mDebateStyle;
        private int mTypeOfDebate;
        public DebateStyleViewHolder(View rootView) {
            super(rootView);
            rootView.setOnClickListener(this);
            mTitle = rootView.findViewById(R.id.textViewTitle);
            mDescription = rootView.findViewById(R.id.textViewDescription);
            mLogo = rootView.findViewById(R.id.logo);
        }
        public void bindDebateStyle(DebateStyle debateStyle) {
            mDebateStyle = debateStyle;
            mTitle.setText(mDebateStyle.getTitle());
            mDescription.setText(mDebateStyle.getDescription());
            mLogo.setImageResource(mDebateStyle.getImageResource());
            mTypeOfDebate = debateStyle.getTypeOfDebate();
        }

        @Override
        public void onClick(View v) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
            SharedPreferences.Editor edit = pref.edit();
            if(pref.getInt(GlobalDataKeys.DEBATE_STYLE_KEY, -1) != -1) {
                edit.remove(GlobalDataKeys.DEBATE_STYLE_KEY).apply();
            }
            edit.putInt(GlobalDataKeys.DEBATE_STYLE_KEY, mTypeOfDebate).apply();
            Intent i = new Intent(v.getContext(), InfoCollector.class);
            i.putExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, MainMenuFrag);
            v.getContext().startActivity(i);
        }

    }
    //Tells which activity requested the edit of information (Home Screen or Profile Screen)
    public void setFromMainActivity(int MainMenuFrag) {
        this.MainMenuFrag = MainMenuFrag;
    }
}
