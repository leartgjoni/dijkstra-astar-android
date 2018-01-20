package com.leartgjoni.app.pathfindingsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button sparseButton = (Button) findViewById(R.id.sparseButton);
        sparseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.gc();
                startActivity(new Intent(Home.this, SparseActivity.class));
            }
        });

        final Button gridButton = (Button) findViewById(R.id.gridButton);
        gridButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.gc();
                startActivity(new Intent(Home.this, GridActivity.class));
            }
        });
    }
}
