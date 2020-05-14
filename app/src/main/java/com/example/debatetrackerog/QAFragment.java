package com.example.debatetrackerog;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class QAFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_q_a, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.questionRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("How do you set win/loss for your rounds?", "Go to \"Edit\" in the sidebar menu and click on the round you want to set."));
        questions.add(new Question("Why did you have to make fun of my debate event in the default debate selection?", "Because it's funny and true. Don't worry I made fun of my event too."));
        questions.add(new Question("Why is everything blue in this app?", "I like blue. If you don't like it you can suggest better colors or even add some code to change color formats with the info at the bottom."));
        questions.add(new Question("I am an amazing coder and/or XML designer. I want to make your flimsy app better.", "Ok, not a question but this app is open source! I want fellow debaters to fix ... I mean contribute to my code so we can have an app made by debaters for debaters. Check my Github info below to contribute!"));
        questions.add(new Question("You did not answer my question or error.", "You can certainly email me below. However, please double check what you did just to make sure you tried everything you could to solve your issue."));
        questions.add(new Question("Here is all my info to ask a question or contribute to this project:", "Email: debateTrackerApp@gmail.com\nGithub Account: PotentialError"));
        QuestionsAdapter adapter = new QuestionsAdapter(questions);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
