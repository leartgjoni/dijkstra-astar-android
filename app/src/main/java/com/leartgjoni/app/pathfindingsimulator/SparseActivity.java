package com.leartgjoni.app.pathfindingsimulator;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SparseActivity extends FragmentActivity {
    SparseView sparse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparse);

        Button start = (Button) findViewById(R.id.start);
        Button stop = (Button) findViewById(R.id.stop);
        Button vertex = (Button) findViewById(R.id.vertex);
        Button edge = (Button) findViewById(R.id.edge);
        final TextView rezultate = (TextView) findViewById(R.id.rezultate);
        Button dijkstra = (Button) findViewById(R.id.dijkstra);
        Button astar = (Button) findViewById(R.id.astar);
        final TextView weightEdge = (TextView) findViewById(R.id.weightEdge);
        sparse = (SparseView) findViewById(R.id.sparse);


        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sparse.buttonClicked = 1;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sparse.buttonClicked = 2;
            }
        });
        vertex.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sparse.buttonClicked = 3;
            }
        });
        edge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sparse.buttonClicked = 4;
            }
        });

        weightEdge.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (sparse.edgeReady == 1) {
                        float weight = Float.parseFloat("" + weightEdge.getText());
                        sparse.graph.addEdge(sparse.edgeStart, sparse.edgeStop, weight);
                        sparse.edgeReady = 0;
                        sparse.buttonClicked = 0;
                        sparse.invalidate();
                        return true;
                    }
                }
                return false;
            }
        });
        dijkstra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text=sparse.Dijkstra();
                rezultate.setText(text);
            }
        });
        astar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = sparse.Astar();
                rezultate.setText(text);
            }
        });

    }
    @Override
    protected void onStop() {
        if(sparse.animationthread.getStatus()== AsyncTask.Status.RUNNING)
            sparse.animationthread.cancel(true);
        super.onStop();
    }

}
