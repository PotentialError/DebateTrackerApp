package com.example.debatetrackerog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {
    private ArrayList<Question> mQuestions;

    public QuestionsAdapter(ArrayList<Question> array) {
        mQuestions = array;
    }

    @Override
    public QuestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.question_item, parent, false);
        return new QuestionsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(QuestionsViewHolder holder, int position) {
        Question question = mQuestions.get(position);
        holder.bindDebateStyle(question);
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class QuestionsViewHolder extends RecyclerView.ViewHolder {
        private TextView mQuestionText;
        private TextView mAnswerText;
        private Question mQuestion;
        public QuestionsViewHolder(View rootView) {
            super(rootView);
            mQuestionText = rootView.findViewById(R.id.questionText);
            mAnswerText = rootView.findViewById(R.id.answerText);
        }

        public void bindDebateStyle(Question question) {
            mQuestion = question;
            mQuestionText.setText(mQuestion.getQuestion());
            mAnswerText.setText(mQuestion.getAnswer());
        }
    }
}