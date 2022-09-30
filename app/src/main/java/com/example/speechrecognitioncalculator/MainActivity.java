

package com.example.speechrecognitioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView question;
    private Button problem;
    private Button answer;
    private String speech;
    private TextToSpeech textToSpeech;
    private int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question = findViewById(R.id.text_view_question);
        problem = findViewById(R.id.button_problem);
        answer = findViewById(R.id.button_answer);
        generateProblem();
        answerCollector();
    }

    public void generateProblem(){
        problem.setOnClickListener(View -> {
            Random random = new Random();
            int num1 = random.nextInt(1000);
            int num2 = random.nextInt(1000);

            String speechSign = null;
            char sign='0';

            switch (random.nextInt(4)){
                case 0 :
                    sign = '+';
                    speechSign = "plus";
                    result = num1+num2;
                    break;
                case 1 :
                    sign = '-';
                    speechSign = "minus";
                    result = num1-num2;
                    break;
                case 2 :
                    sign = '*';
                    speechSign = "multiply by";
                    result = num1*num2;
                    break;
                case 3:
                    sign = '/';
                    speechSign = "divided by";
                    result = num1/num2;
                    break;
                default:
                    break;
            }
            speech = num1+","+speechSign+","+num2;
            question.setTextSize(32);
            question.setText(num1+" "+sign+" "+num2);
            generateSpeech();
        });
    }


    public void generateSpeech() {
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if(i==TextToSpeech.SUCCESS){
                textToSpeech.setPitch(1.5f);
                textToSpeech.setSpeechRate(0.8f);
                textToSpeech.speak(speech,TextToSpeech.QUEUE_FLUSH,null);
            }
            else{
                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void answerCollector(){
        answer.setOnClickListener(View -> {
            Intent intent = new Intent(this, AnswerRevaluator.class);
            intent.putExtra("problemResult",String.valueOf(result));
            startActivity(intent);
        });
    }
}