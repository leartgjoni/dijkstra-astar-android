package com.leartgjoni.app.pathfindingsimulator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GridActivity extends FragmentActivity {
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        Button dijkstra = (Button) findViewById(R.id.dijkstra);
        Button astar = (Button) findViewById(R.id.astar);
        final TextView rezultate = (TextView) findViewById(R.id.rezultate);
        grid = (GridView) findViewById(R.id.grid);


        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            grid.stopClicked=0;
            grid.startClicked=1;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                grid.startClicked=0;
                grid.stopClicked=1;
            }
        });
        dijkstra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=grid.Dijkstra();
                rezultate.setText(text);
            }
        });
        astar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=grid.Astar();
                rezultate.setText(text);
            }
        });
    }

    @Override
    protected void onStop() {
        if(grid.dijkstrathread.getStatus()== AsyncTask.Status.RUNNING)
            grid.dijkstrathread.cancel(true);
        if(grid.astarthread.getStatus()==AsyncTask.Status.RUNNING){
            grid.astarthread.cancel(true);
        }

        super.onStop();
    }
}//end activity
