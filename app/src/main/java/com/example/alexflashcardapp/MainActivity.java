package com.example.alexflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
// my added
import android.content.Intent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView questionTextView;
    TextView answerTextView;

    // outside of oncreate, create flashcarddatabase
    FlashcardDatabase flashcardDatabase;

    // in mainactivity: hold our flashcard objects, outside of oncreate hold list of flashcards
    List<Flashcard> allFlashcards;

    // init variable for card display
    int currentCardDisplayIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inside ooncreate initalize flashcarddatabase variable
        // inside oncreate because getapplicationcontext will return null if app not init yet
        flashcardDatabase = new FlashcardDatabase(getApplicationContext()); // this instead of getappcontext?

        // check if any data is in our database to be displayed / read database when app launched
        allFlashcards = flashcardDatabase.getAllCards();

        // if statement, check if list empty or not, display saved flashcard or default from xml
        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.input_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.input_answer)).setText(allFlashcards.get(0).getAnswer());
        }

        // next button to view saved flashcards
        findViewById(R.id.flashcard_next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // base case
                if (allFlashcards.size() == 0) {
                    return;
                }
                // advance pointer to next index to show next card
                currentCardDisplayIndex += 1;

                // make sure not index out of bounds if viewing last card in list
                if (currentCardDisplayIndex >= allFlashcards.size()) {
                    // reset
                    currentCardDisplayIndex = 0;
                }

                // set question and answer textviews with data form database
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayIndex);

                ((TextView) findViewById(R.id.input_question)).setText(flashcard.getQuestion());
                ((TextView) findViewById(R.id.input_answer)).setText(flashcard.getAnswer());

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // method called when animation starts
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // animation finished
                        questionTextView.startAnimation(rightInAnim);

                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // no need to worry
                    }

                });


            }
        });
        // PREVIOUS button to view saved flashcards
        findViewById(R.id.flashcard_prev_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // base case
                if (allFlashcards.size() == 0) {
                    return;
                }
                // advance pointer to next index to show next card
                currentCardDisplayIndex -= 1;

                // make sure not index out of bounds if viewing last card in list
                if (currentCardDisplayIndex < 0) {
                    // reset
                    currentCardDisplayIndex = allFlashcards.size() - 1;
                }

                // set question and answer textviews with data form database
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayIndex);

                ((TextView) findViewById(R.id.input_question)).setText(flashcard.getQuestion());
                ((TextView) findViewById(R.id.input_answer)).setText(flashcard.getAnswer());
            }
        });

        // DELETE current card
        findViewById(R.id.flashcard_trash_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFlashcards = flashcardDatabase.getAllCards();

                // what is base case if last card to delete? lol
                // base case no more cards left
                if (allFlashcards.size() == 0 || allFlashcards.size() == 1) {
                    return;
                }

                // else

                // delete card
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.input_question)).getText().toString());

                // advance pointer to next index to show next card
                currentCardDisplayIndex -= 1;

                // make sure not index out of bounds if viewing last card in list
                if (currentCardDisplayIndex < 0) {
                    // reset
                    currentCardDisplayIndex = 0;
                }

                // set question and answer textviews with data form database
                allFlashcards = flashcardDatabase.getAllCards();
                Flashcard flashcard = allFlashcards.get(currentCardDisplayIndex);

                ((TextView) findViewById(R.id.input_question)).setText(flashcard.getQuestion());
                ((TextView) findViewById(R.id.input_answer)).setText(flashcard.getAnswer());


            }
        });




        findViewById(R.id.flashcard_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        questionTextView = findViewById(R.id.input_question);
        answerTextView = findViewById(R.id.input_answer);

        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextView.setVisibility(View.INVISIBLE);
                answerTextView.setVisibility(View.VISIBLE);



                View answerSideView = findViewById(R.id.input_answer);

// get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

// get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

// hide the question and show the answer to prepare for playing the animation!
                questionTextView.setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();

                //Toast.makeText(MainActivity.this, "I clicked the question!", Toast.LENGTH_SHORT).show();
                //Log.i("Alex", "entered question onclick method");


            }
        });

        answerTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerTextView.setVisibility(View.INVISIBLE);
                questionTextView.setVisibility(View.VISIBLE);

                View questionSideView = findViewById(R.id.input_question);

// get the center for the clipping circle
                int cx = questionSideView.getWidth() / 2;
                int cy = questionSideView.getHeight() / 2;

// get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(questionSideView, cx, cy, 0f, finalRadius);

// hide the question and show the answer to prepare for playing the animation!
                answerTextView.setVisibility(View.INVISIBLE);
                questionSideView.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();
            }
        }));



        ImageView addQuestionImageView = findViewById(R.id.flashcard_add_button);
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // get data
            if (data != null) {
                String questionString = data.getExtras().getString("question_key");
                String answerString = data.getExtras().getString("answer_key");
                questionTextView.setText(questionString);
                answerTextView.setText(answerString);

                // grab data to save into database
                flashcardDatabase.insertCard(new Flashcard(questionString, answerString));

                // local variable holding list of flashcards update
                allFlashcards = flashcardDatabase.getAllCards();

            }
        }
    }

}