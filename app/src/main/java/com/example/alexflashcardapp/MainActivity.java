package com.example.alexflashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
// my added
import android.content.Intent;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;
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




        findViewById(R.id.flashcard_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        questionTextView = findViewById(R.id.input_question);
        answerTextView = findViewById(R.id.input_answer);

        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionTextView.setVisibility(View.INVISIBLE);
                answerTextView.setVisibility(View.VISIBLE);

                //Toast.makeText(MainActivity.this, "I clicked the question!", Toast.LENGTH_SHORT).show();
                //Log.i("Alex", "entered question onclick method");
            }
        });

        answerTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerTextView.setVisibility(View.INVISIBLE);
                questionTextView.setVisibility(View.VISIBLE);
            }
        }));



        ImageView addQuestionImageView = findViewById(R.id.flashcard_add_button);
        addQuestionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);
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