package com.example.quizeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizeapp.controller.AppController;
import com.example.quizeapp.data.AnswerListsyncResponse;
import com.example.quizeapp.data.AnswerListsyncResponse;
import com.example.quizeapp.data.QuestionBank;
import com.example.quizeapp.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextview;
    private TextView questioncounterTextiew;
    private Button trueButton;
    private Button falsButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falsButton = findViewById(R.id.false_button);
        questioncounterTextiew = findViewById(R.id.counter_text);
        questionTextview = findViewById(R.id.question_textview);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falsButton.setOnClickListener(this);





               questionList = new QuestionBank().getQuestion(new AnswerListsyncResponse() {
                   @SuppressLint("SetTextI18n")
                   @Override
                   public void processFinished(ArrayList<Question> questionArrayList) {


                       questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                       questioncounterTextiew.setText(currentQuestionIndex + " / " + questionArrayList.size());
                       Log.d("Inside", "processFinished: " + questionArrayList);


                   }




                   // Log.d("Main", "onCreate: " + questionList);

               });
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex > 0){
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;

                case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex  + 1) % questionList.size();
                updateQuestion();
                break;

            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;

                case R.id.false_button:
                    checkAnswer(false);
                    updateQuestion();
                break;

        }
    }

      private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMassageId;
        if(userChooseCorrect == answerIsTrue){
            fadeView();
            toastMassageId = R.string.correct_anser;
        } else {
            shakeAnimation();
            toastMassageId = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this,toastMassageId,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        questioncounterTextiew.setText(currentQuestionIndex + "/" + questionList.size());
    }

      private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
          AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

          alphaAnimation.setDuration(250);
          alphaAnimation.setRepeatCount(1);
          alphaAnimation.setRepeatMode(Animation.REVERSE);

          cardView.setAnimation(alphaAnimation);

          alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
              @Override
              public void onAnimationStart(Animation animation) {
                  cardView.setCardBackgroundColor(Color.GREEN);
              }

              @Override
              public void onAnimationEnd(Animation animation) {
                  cardView.setCardBackgroundColor(Color.WHITE);

              }

              @Override
              public void onAnimationRepeat(Animation animation) {

              }
          });


      }


    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
         final   CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
