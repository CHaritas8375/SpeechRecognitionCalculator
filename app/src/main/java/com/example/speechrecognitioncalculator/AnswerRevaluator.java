package com.example.speechrecognitioncalculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class AnswerRevaluator extends AppCompatActivity {
private TextView speechRecorder;
private ImageView micImage;
private String problemResult;
private Button evaluate,reset;
private String speech;
private TextToSpeech resultAnnounce;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_revaluator);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            problemResult = bundle.getString("problemResult");
        }

        speechRecorder = findViewById(R.id.text_view_speech_recorder);
        micImage = findViewById(R.id.image_view_mic);
        evaluate = findViewById(R.id.button_evaluate);
        reset = findViewById(R.id.button_reset);

        micImage.setOnClickListener(View->{
            Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Your Answer...");
            startActivityForResult(speechRecognizerIntent,10);
        });
        resultEvaluator();

        reset.setOnClickListener(View -> {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        speechRecorder.setText(result.get(0));
        speechRecorder.setTextSize(28);
    }

    public void resultEvaluator(){
        evaluate.setOnClickListener(View -> {

            if(problemResult.equals(String.valueOf(speechRecorder.getText()))){
                speech = "Your Answer is correct.";
            }
            else{
                speech = "Your Answer is incorrect, Correct Answer is : "+problemResult;
            }

            speechRecorder.setText(speech);

            resultAnnounce = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status == TextToSpeech.SUCCESS) {
                        resultAnnounce.setPitch(1.2f);
                        resultAnnounce.setSpeechRate(0.8f);
                        resultAnnounce.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }
}