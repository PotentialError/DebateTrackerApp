package com.example.debatetrackerog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ChooseDebateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_debate);
        RecyclerView recyclerView = findViewById(R.id.debateStyleRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<DebateStyle> array = new ArrayList<>();
        array.add(new DebateStyle("Public Forum", "The only good thing about this event is you get a partner to suffer with", R.drawable.publiccircle, 0));
        array.add(new DebateStyle("Lincoln Douglas", "No other type of debater or any of your friends and family will understand this", R.drawable.circlelincoln, 1));
        array.add(new DebateStyle("Policy", "Judge: How fast are you gonna speak?\nPolicy Debater: Yes", R.drawable.policycircle, 2));
        array.add(new DebateStyle("Big Questions", "Contrary to the title, there are no people dressed up like question marks", R.drawable.bigquestioncircle, 3));
        array.add(new DebateStyle("Extemporaneous", "Taking the stress of researching and debating and forcing people to do both of them in a single day", R.drawable.extempcircle, 4));
        array.add(new DebateStyle("World Schools", "Again, contrary to the title, no one is dressed up like the world", R.drawable.worldcircle, 5));
        DebateStyleAdapter adapter = new DebateStyleAdapter(array);
        adapter.setFromMainActivity(getIntent().getIntExtra(GlobalDataKeys.FRAG_REQUEST_EDIT_KEY, -1));
        recyclerView.setAdapter(adapter);
    }
}
