package com.github.grishberg.instrumentaltestsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button phoneButton;
    private Button tabletButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        phoneButton = findViewById(R.id.buttonForPhone);
        tabletButton = findViewById(R.id.buttonForTablet);

        if (phoneButton != null) {
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText(R.string.text_for_phone);
                }
            });
        }

        if (tabletButton != null) {
            tabletButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText(R.string.text_for_tablet);
                }
            });
        }
    }
}
