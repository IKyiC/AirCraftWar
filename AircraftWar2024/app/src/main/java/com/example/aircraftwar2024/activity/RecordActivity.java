package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.R;

public class RecordActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);

        int myScore = getIntent().getIntExtra("myScore", 0);
        int opscore1 = getIntent().getIntExtra("opScore", 0);

        TextView text_1 = findViewById(R.id.myScore);
        text_1.setText(""+myScore);

        TextView text_2 = findViewById(R.id.yourScore);
        text_2.setText(""+opscore1);

        TextView text_3 = findViewById(R.id.result_text);
        if(myScore > opscore1) {
            text_3.setText("win!");
        }
        else if(opscore1 > myScore) {
            text_3.setText("defeat!");
        }
        else {
            text_3.setText("平局");
        }


        Button return_btn = (Button) findViewById(R.id.return_btn);
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
