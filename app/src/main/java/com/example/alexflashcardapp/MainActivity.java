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

public class MainActivity extends AppCompatActivity {

    TextView questionTextView;
    TextView answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
            }
        }
    }

}