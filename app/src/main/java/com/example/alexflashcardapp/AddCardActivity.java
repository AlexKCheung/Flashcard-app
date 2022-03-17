package com.example.alexflashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        findViewById(R.id.flashcard_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.flashcard_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String inputQuestion = ((EditText) findViewById(R.id.input_question)).getText().toString();
                String inputAnswer = ((EditText) findViewById(R.id.input_answer)).getText().toString();
                data.putExtra("question_key", inputQuestion);
                data.putExtra("answer_key", inputAnswer);
                setResult(RESULT_OK, data);
                finish();

            }
        });




    }
}