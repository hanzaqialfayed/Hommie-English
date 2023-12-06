package com.example.hommieenglish;

import static com.example.hommieenglish.QuestionActivity.matchParentMatchParent;
import static com.example.hommieenglish.QuestionActivity.matchParentWrapContent;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.hommieenglish.dao.AnswerDao;
import com.example.hommieenglish.dao.QuestionsDao;
import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.QuestionAndAnswers;
import com.example.hommieenglish.entity.Questions;
import com.example.hommieenglish.utils.BankQuestionsAnswer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BankQuestionsExplanation extends Activity implements Serializable {
    private HommieEnglish db;
    private AnswerDao answerDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_questions_explanation);

        List<BankQuestionsAnswer> bankQuestionsAnswerList = (List<BankQuestionsAnswer>) getIntent().getSerializableExtra("bank_question_answers");

        LinearLayout bqe = findViewById(R.id.bank_question_explanation_layout);
        db = HommieEnglish.getInstance(this);
        answerDao = db.answerDao();
        CompletableFuture<List<BankQuestionsAnswer>> completableFutureBQA = CompletableFuture.supplyAsync(
                () -> {
                    Log.d("DEBUG", "INI HASIL INTENT" + bankQuestionsAnswerList.size());
                    List<BankQuestionsAnswer> result = new ArrayList<>();
                    for (BankQuestionsAnswer b : bankQuestionsAnswerList) {
                        Log.d("DEBUG", b.getCurrentAnswer());
                        Log.d("DEBUG", String.valueOf(b.getUnitId()));
                        Log.d("DEBUG", b.getQuestion());
                        BankQuestionsAnswer newBankQuestionAnswer = new BankQuestionsAnswer(b.getCurrentAnswer(), b.getUnitId(), b.getQuestion());
                        Answer answer = answerDao.getCorrectAnswer(b.getUnitId(), true);
                        newBankQuestionAnswer.setExplanation(answer.getExplanation());
                        newBankQuestionAnswer.setCorrectAnwer(answer.getAnswerText());
                        result.add(newBankQuestionAnswer);
                    }
                    return result;
                }
        );

        completableFutureBQA.thenAcceptAsync(dataList -> {
            runOnUiThread(() -> {
                Integer sequence = 1;
                for (BankQuestionsAnswer b : dataList) {
                    // Membuat layout untuk pertanyaan
                    LinearLayout explanationLayout = new LinearLayout(this);
                    explanationLayout.setLayoutParams(matchParentWrapContent);
                    explanationLayout.setOrientation(LinearLayout.VERTICAL);
                    explanationLayout.setBackgroundResource(R.drawable.background_color);

                    TextView textViewQuestion = new TextView(this);
                    textViewQuestion.setLayoutParams(matchParentWrapContent);
                    textViewQuestion.setTextColor(Color.BLACK);
                    textViewQuestion.setPadding(16,16,16,16);
                    textViewQuestion.setTextSize(16);
                    textViewQuestion.setText(sequence + ". " + b.getQuestion());

                    TextView currentAnswer = new TextView(this);
                    currentAnswer.setLayoutParams(matchParentWrapContent);
                    currentAnswer.setTextColor(Color.BLACK);
                    currentAnswer.setPadding(16,16,16,16);
                    currentAnswer.setTextSize(16);
                    currentAnswer.setText("Current Answer : " + b.getCurrentAnswer());

                    TextView correctAnswer = new TextView(this);
                    correctAnswer.setLayoutParams(matchParentWrapContent);
                    correctAnswer.setTextColor(Color.BLACK);
                    correctAnswer.setPadding(16,16,16,16);
                    correctAnswer.setTextSize(16);
                    correctAnswer.setText("Correct Answer : " + b.getCorrectAnwer());
                    correctAnswer.setTypeface(Typeface.DEFAULT_BOLD);

                    TextView explanation = new TextView(this);
                    explanation.setLayoutParams(matchParentWrapContent);
                    explanation.setPadding(16,16,16,16);
                    explanation.setTextColor(Color.BLACK);
                    explanation.setText("Explanation : " + b.getExplanation());
                    explanation.setTextSize(16);
                    explanation.setTypeface(Typeface.DEFAULT_BOLD);

                    explanationLayout.addView(textViewQuestion);
                    explanationLayout.addView(currentAnswer);
                    explanationLayout.addView(correctAnswer);
                    explanationLayout.addView(explanation);
                    bqe.addView(explanationLayout);

                    sequence++;

                }
            });
        });
    }
}
