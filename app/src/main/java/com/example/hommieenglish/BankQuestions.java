package com.example.hommieenglish;

import static com.example.hommieenglish.QuestionActivity.backgroundColor;
import static com.example.hommieenglish.QuestionActivity.matchParentMatchParent;
import static com.example.hommieenglish.QuestionActivity.matchParentWrapContent;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.hommieenglish.dao.AnswerDao;
import com.example.hommieenglish.dao.QuestionsDao;
import com.example.hommieenglish.db.HommieEnglish;
import com.example.hommieenglish.entity.Answer;
import com.example.hommieenglish.entity.QuestionAndAnswers;
import com.example.hommieenglish.entity.Questions;
import com.example.hommieenglish.utils.BankQuestionsAnswer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BankQuestions extends Activity implements Serializable{
    private HommieEnglish db;
    private QuestionsDao questionsDao;
    private AnswerDao answerDao;
    private List<Integer> radioGroupIds = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_questions);
        Intent intent = getIntent();

        LinearLayout layout = findViewById(R.id.bank_question_layout);
        db = HommieEnglish.getInstance(this);
        questionsDao = db.questionsDao();
        answerDao = db.answerDao();
        Button submitButton = findViewById(R.id.bank_question_submit_answer);

        CompletableFuture<List<QuestionAndAnswers>> completableFutureQnA = CompletableFuture.supplyAsync(
                () -> {
                    List<Questions> questions = questionsDao.getBankQuestions(true, intent.getStringExtra("level"));
                    List<QuestionAndAnswers> result = new ArrayList<>();
                    for (Questions q:questions) {
                        QuestionAndAnswers qna = new QuestionAndAnswers();
                        qna.setUnitId(q.getUnitId());
                        qna.setQuestion(q.getQuestion());
                        qna.setParentQuestion(q.getParentQuestion());
                        qna.setType(q.getType());
                        qna.setContent(q.getContent());
                        qna.setSequence(q.getSequence());
                        qna.setId(q.getId());
                        qna.setAnswers(answerDao.getByQuestionId(q.getId()));
                        result.add(qna);
                    }
                    return result;
                }
        );
        completableFutureQnA.thenAcceptAsync(dataList -> {
            runOnUiThread(() -> {
                if (dataList.size() == 0) {
                    submitButton.setVisibility(View.GONE);
                }
                Integer sequence = 1;
                for (QuestionAndAnswers q : dataList) {
                    // Membuat layout untuk pertanyaan
                    LinearLayout questionLayout = new LinearLayout(this);
                    questionLayout.setLayoutParams(matchParentMatchParent);
                    questionLayout.setGravity(Gravity.CENTER);
                    questionLayout.setPadding(30, 30, 30, 0);
                    questionLayout.setOrientation(LinearLayout.VERTICAL);

                    if (q.getParentQuestion()) {
                        if (q.getType().equals("image")) {
                            // Membuat gambar pada layout pertanyaan
                            ImageView questionImage = new ImageView(this);
                            String imageName = q.getContent();

                            int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            questionImage.setImageResource(resID);
                            int heightInSp = 300;

                            int heightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightInSp, getResources().getDisplayMetrics());

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightInPx);
                            questionImage.setLayoutParams(layoutParams);
                            questionImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            questionLayout.addView(questionImage);
                        } else if (q.getType().equals("text")) {
                            TextView textView = new TextView(this);
                            textView.setLayoutParams(matchParentWrapContent);
                            textView.setText(q.getContent());
                            textView.setTextSize(16);
                            textView.setTypeface(Typeface.SANS_SERIF);
                            textView.setPadding(10,10,10,0);
                            textView.setBackgroundColor(Color.parseColor(backgroundColor));
                            textView.setTextColor(Color.WHITE);
                            questionLayout.addView(textView);
                        } else if (q.getType().equals("audio")) {
                            RelativeLayout relativeLayout = new RelativeLayout(this);
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            relativeLayout.setLayoutParams(layoutParams);
                            relativeLayout.setPadding(16, 16, 16, 16);
                            relativeLayout.setBackgroundColor(Color.parseColor(backgroundColor));

                            ImageButton btnPlayPause = new ImageButton(this);
                            RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            );
                            btnLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            btnPlayPause.setLayoutParams(layoutParams);
                            btnPlayPause.setBackgroundColor(Color.TRANSPARENT);
                            btnPlayPause.setId(View.generateViewId());
                            int resID = getResources().getIdentifier("ic_play", "drawable", getPackageName());
                            btnPlayPause.setImageResource(resID);

                            int audioId = getResources().getIdentifier(q.getContent(), "raw", getPackageName());
                            AssetFileDescriptor afd = getResources().openRawResourceFd(audioId);
                            MediaPlayer md = new MediaPlayer();
                            if (afd != null) {
                                try {
                                    md.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                    afd.close();
                                    md.prepare();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            final Boolean[] isPlaying = {false};
                            btnPlayPause.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isPlaying[0]) {
                                        isPlaying[0] = pauseAudio(md, btnPlayPause);
                                    } else {
                                        isPlaying[0] = playAudio(md, btnPlayPause);
                                    }
                                }
                            });

                            relativeLayout.addView(btnPlayPause);
                            questionLayout.addView(relativeLayout);
                        }
                    }

                    // Membuat text view untuk pertanyaan
                    TextView questionText = new TextView(this);
                    questionText.setLayoutParams(matchParentWrapContent);
                    questionText.setText(sequence + ". " + q.getQuestion());
                    questionText.setTextSize(16);
                    questionText.setTypeface(Typeface.DEFAULT_BOLD);
                    questionText.setTextColor(Color.WHITE);
                    questionText.setBackgroundColor(Color.parseColor(backgroundColor));
                    questionText.setForegroundGravity(Gravity.BOTTOM);
                    questionText.setPadding(16,16,16,16);

                    // Menambahkan image dan text view ke layout
                    questionLayout.addView(questionText);

                    // Membuat Layout untuk jawaban
                    LinearLayout answerLayout = new LinearLayout(this);
                    answerLayout.setLayoutParams(matchParentWrapContent);
                    answerLayout.setGravity(Gravity.TOP);
                    answerLayout.setPadding(16,16,16,16);
                    answerLayout.setOrientation(LinearLayout.VERTICAL);

                    // Membuat radio group untuk pilihan ganda
                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.setLayoutParams(matchParentWrapContent);
                    radioGroup.setBackgroundColor(Color.parseColor(backgroundColor));
                    radioGroup.setOrientation(LinearLayout.VERTICAL);
                    radioGroup.setId(View.generateViewId());

                    for (Answer a : q.getAnswers()) {
                        RadioButton rb1 = new RadioButton(this);
                        LinearLayout.LayoutParams lprb = matchParentWrapContent;
                        lprb.setMargins(10,10,10,10);
                        rb1.setLayoutParams(lprb);
                        rb1.setTextSize(16);
                        rb1.setTextColor(Color.WHITE);
                        rb1.setText(a.getAnswerText());
                        rb1.setId(View.generateViewId());
                        rb1.setTag(new BankQuestionsAnswer(a.getAnswerText(), a.getQuestionId(), q.getQuestion()));
                        radioGroup.addView(rb1);
                    }
                    radioGroupIds.add(radioGroup.getId());
                    answerLayout.addView(radioGroup);
                    layout.addView(questionLayout);
                    layout.addView(answerLayout);
                    sequence++;
                }
            });
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<BankQuestionsAnswer> bankQuestionsAnswerList = new ArrayList<>();
                for (Integer radioGroupId : radioGroupIds) {
                    RadioGroup radioGroup = findViewById(radioGroupId);
                    int idRadioButton = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(idRadioButton);
                    if (selectedRadioButton == null) {
                        continue;
                    }
                    BankQuestionsAnswer bankQuestionsAnswer = (BankQuestionsAnswer) selectedRadioButton.getTag();
                    Log.d("DEBUG", "Current Answer" + bankQuestionsAnswer.getCurrentAnswer());
                    bankQuestionsAnswerList.add(bankQuestionsAnswer);
                }

                Intent i = new Intent(getBaseContext(), BankQuestionsExplanation.class);
                i.putExtra("bank_question_answers", (Serializable) bankQuestionsAnswerList);
                startActivity(i);
            }
        });
    }

    // Fungsi untuk memainkan audio yang diputar
    public static Boolean playAudio(MediaPlayer mediaPlayer, ImageButton btnPlayPause) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        }
        return true;
    }

    // Fungsi untuk mem-pause audio yang diputar
    public static Boolean pauseAudio(MediaPlayer mediaPlayer, ImageButton btnPlayPause) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.ic_play);
        }
        return false;
    }
}
