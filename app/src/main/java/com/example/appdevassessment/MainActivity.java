package com.example.appdevassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button reportBridgeButton = (Button) findViewById(R.id.report_bridge_button);

        reportBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ReportBridgeActivity.class)));
    }
}
