package com.leartgjoni.app.pathfindingsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.leartgjoni.app.pathfindingsimulator.Graph.Edge;
import com.leartgjoni.app.pathfindingsimulator.Graph.Graph;
import com.leartgjoni.app.pathfindingsimulator.Graph.Vertex;

import java.util.Stack;

/**
 * Created by leart on 16-04-11.
 */
public class SparseView extends View {
    int animationtime=500;
    float width,height;
    Graph graph=new Graph();
    int buttonClicked=0; //1-start 2-stop 3-vertex 4-edge 5-edgeStart 6-edgeStop
    int counter = 0;
    float start_x,start_y;
    float stop_x,stop_y;
    Vertex edgeStart,edgeStop;//ku do te ruajme nodet ne mom qe zgjdhet te krijohet nje edge
    int edgeReady=0;//setohet 1 kur jemi gati te marrim peshen e edge
    int rrezenode=50;
    Paint paint = new Paint();
    async animationthread = new async();
    public SparseView(Context context){
        super(context);
        init(null,0);
    }
    public SparseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public SparseView(Context context,AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    public void init(AttributeSet attrs,int defStyle){
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }



    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<graph.vertex.size();i++){
            paint.setStyle(Paint.Style.FILL);
            Vertex v = graph.vertex.get(i);
            if(v.x==start_x&&v.y==start_y)
                paint.setColor(Color.RED);
            if(v.x==stop_x&&v.y==stop_y)
                paint.setColor(Color.GREEN);

            canvas.drawCircle((float) v.x, (float) v.y, rrezenode, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(80);
            canvas.drawText("" + v.id, (float) v.x, (float) v.y, paint);
            paint.setColor(Color.BLACK);
            for(int j=0;j<graph.vertex.get(i).edges.size();j++){
                Vertex v1=graph.vertex.get(i);
                Vertex v2=v1.edges.get(j).destination;
                if(v1.edges.get(j).isPath==1)
                    paint.setColor(Color.YELLOW);
                else
                    paint.setColor(Color.BLACK);

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8);
                canvas.drawLine((float)v1.x,(float) v1.y,(float) v2.x,(float) v2.y, paint);
                paint.setColor(Color.BLACK);
                float xmes = (float) (v1.x+v2.x)/2;
                float ymes = (float) (v1.y+v2.y)/2;
                paint.setTextSize(30);
                paint.setStrokeWidth(2);
                canvas.drawText("" + String.format( "%.2f", v1.edges.get(j).weight ), xmes, ymes+40/*qe ms shkruajme mbi vize*/, paint);

            }
        }

    }


    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (buttonClicked == 1) {
                    if(!kontrollPike(motionEvent.getX(),motionEvent.getY())) {
                        graph.addVertex(counter, motionEvent.getX(), motionEvent.getY());
                        start_x = motionEvent.getX();
                        start_y = motionEvent.getY();
                        counter++;
                        buttonClicked = 0;
                        invalidate();
                    }
                } else if (buttonClicked == 2) {
                    if(!kontrollPike(motionEvent.getX(),motionEvent.getY())) {
                        graph.addVertex(counter, motionEvent.getX(), motionEvent.getY());
                        stop_x = motionEvent.getX();
                        stop_y = motionEvent.getY();
                        counter++;
                        buttonClicked = 0;
                        invalidate();
                    }

                } else if (buttonClicked == 3) {
                    if(!kontrollPike(motionEvent.getX(),motionEvent.getY())) {
                        graph.addVertex(counter, motionEvent.getX(), motionEvent.getY());
                        counter++;
                        buttonClicked = 0;
                        invalidate();
                    }
                } else if (buttonClicked == 4){
                    Vertex v= getVertex(motionEvent.getX(),motionEvent.getY());
                    if(v!=null){
                        edgeStart=v;
                        buttonClicked=5;
                    }
                }else if(buttonClicked==5){
                    Vertex v= getVertex(motionEvent.getX(),motionEvent.getY());
                    if(v!=null){
                        edgeStop=v;
                        edgeReady=1;
                                }

                }
            }


        return true;
    }
    public boolean kontrollPike(float x,float y){//kontrollo nqs pika x,y pret ndonje vertex
        for(int i=0;i<graph.vertex.size();i++){
            Vertex v= graph.vertex.get(i);
            double d = Math.sqrt((x - v.x) * (x - v.x)+(y-v.y)*(y-v.y));//distanca e dy qendrave
            if(d<2*rrezenode)
                return true;//rrathet nderpriten
        }
        return false;

    }
    public Vertex getVertex(float x,float y){
        for(int i=0;i<graph.vertex.size();i++){
            Vertex v= graph.vertex.get(i);
            double d = Math.sqrt((x - v.x) * (x - v.x)+(y-v.y)*(y-v.y));//distanca e qendres se vertex nga pika
            if(d<=rrezenode)
                return v;//dmth qe pika eshte ne rreth
        }
        return null;

    }
    public Graph freeGraph(){
        //free graph nga shenimet e alg paraardhes
        for(int i=0;i<graph.vertex.size();i++){
            Vertex current = graph.vertex.get(i);
            current.parent=null;
            current.d_value= Double.POSITIVE_INFINITY;
            current.discovered=false;
            current.h_value=0;
            current.f_value=Double.POSITIVE_INFINITY;
            for(int j=0;j<current.edges.size();j++){
                current.edges.get(j).isPath=0;
            }
        }
        return graph;
    }
    public String Dijkstra(){
        System.gc();
        if(graph.vertex.size()==0)
            return "You have not created a graph yet";
        graph = freeGraph();
        if(graph.getV(stop_x, stop_y)==null)
            return "You have not specified the destination node yet";

        String text=graph.Dijkstra(graph.getV(start_x, start_y), graph.getV(stop_x, stop_y));//ky exe na duhet per statistika
        animationthread = new async();
        animationthread.execute();
        invalidate();
        return text;

    }
    public String Astar(){
        System.gc();
        if(graph.vertex.size()==0)
            return "You have not created a graph yet";
        graph=freeGraph();
        if(graph.getV(stop_x, stop_y)==null)
            return "You have not specified the destination node yet";
        String text=graph.Astar(graph.getV(start_x, start_y), graph.getV(stop_x, stop_y));
        animationthread = new async();
        animationthread.execute();
        invalidate();
        return text;
    }
    public class async extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            //set edges me isPath=1
            Vertex current = graph.getV(stop_x,stop_y);
            if(current.parent==null){
                //dmth nuk ka path
                return null;
            }
            while(current!=graph.getV(start_x,start_y)){
                if (isCancelled()) break;
                Vertex parent = current.parent;
                Edge e;
                for(int i=0;i<current.edges.size();i++){
                    Edge current_edge=current.edges.get(i);
                    if(current_edge.destination==parent) {
                        current_edge.isPath = 1;
                        //duhet bere dhe edge i anasjellte 1
                        for(int j=0;j<parent.edges.size();j++) {
                            if (parent.edges.get(j).destination == current) {
                                parent.edges.get(j).isPath = 1;
                            }
                        }
                    }
                }
                current=parent;
            }
            return null;
        }
    }

}
