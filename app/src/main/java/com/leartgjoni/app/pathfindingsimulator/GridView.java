package com.leartgjoni.app.pathfindingsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.leartgjoni.app.pathfindingsimulator.Graph.Edge;
import com.leartgjoni.app.pathfindingsimulator.Graph.Graph;
import com.leartgjoni.app.pathfindingsimulator.Graph.Vertex;
import com.leartgjoni.app.pathfindingsimulator.Graph.astarcomparator;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by leart on 16-04-08.
 */
public class GridView extends View{
    int rows=20;
    int cols=20;
    float tileWidth;
    float tileHeight;
    float width;
    float height;
    int[][] grid = new int[rows][cols]; //0-bosh 1-start 2-end 3-pengese 4-ndodhet ne set 5-ndodhet ne queue 6-tile path
    int startClicked = 0;
    int start_x,start_y;
    int stopClicked = 0;
    int stop_x,stop_y;
    final int animationtime=50;

    Graph graph;
    Paint paint = new Paint();
    async dijkstrathread=new async();
    async2 astarthread=new async2();

    public GridView(Context context){
        super(context);
        init(null,0);
    }
    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    public GridView(Context context,AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    public void init(AttributeSet attrs,int defStyle){
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }







    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //canvas.drawRect(100, 100, 150, 150, paint);
        tileWidth = width/cols;
        tileHeight = height/rows;
        int border=0;
        //Vizato kornizen
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        //vizato secilin tile sipas permbajtjes se matrices grid
        for(int r=0;r<rows;r++)
            for(int c=0;c<cols;c++)
            {
                paint.setStyle(Paint.Style.FILL);
                int value = grid[r][c];
                switch(value){
                    case 0:
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.BLACK);
                        break;
                    case 1:
                        paint.setColor(Color.RED);
                        break;
                    case 2:
                        paint.setColor(Color.GREEN);
                        break;
                    case 3:
                        paint.setColor(Color.BLACK);
                        break;
                    case 4:
                        paint.setColor(Color.CYAN);
                        border=1;
                        break;
                    case 5:
                        paint.setColor(Color.LTGRAY);
                        border=1;
                        break;
                    case 6:
                        paint.setColor(Color.YELLOW);
                        border=1;
                        break;
                }
                canvas.drawRect(c*tileWidth, r*tileHeight,c*tileWidth+tileWidth ,r*tileHeight+tileHeight , paint);
                if(border==1){
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLACK);
                    canvas.drawRect(c * tileWidth, r * tileHeight, c * tileWidth + tileWidth, r * tileHeight + tileHeight, paint);
                }

            }
    }






    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
    }







    public boolean onTouchEvent(MotionEvent motionEvent){
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int c = (int) (x / tileWidth);
            int r = (int) (y / tileHeight);
            if(startClicked==1){
                grid[start_y][start_x]=0;//setojm start e kaluar ne empty
                grid[r][c]=1;
                start_x=c;
                start_y=r;
                startClicked=0;
            }
            else if(stopClicked==1){
                grid[stop_y][stop_x]=0;
                grid[r][c]=2;
                stop_x=c;
                stop_y=r;
                stopClicked=0;
            }
            else if (grid[r][c]!=1&&grid[r][c]!=2){
                    if(grid[r][c] != 3)
                    grid[r][c] = 3;
                    else
                    grid[r][c] = 0;
            }



        }
        if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int c = (int) (x / tileWidth);
            int r = (int) (y / tileHeight);
            if(x>width || y>height||x<0||y<0)
                return false;//pusho se degjuari ACTION_MOVE nqs dole jashte kufijve te view.
            if(grid[r][c]!=1&&grid[r][c]!=2)
            if (grid[r][c] != 3)
                grid[r][c] = 3;
        }
        invalidate();
        return true;
    }












    public String Dijkstra(){
        System.gc();
        getGraph();
        dijkstrathread = new async();
        dijkstrathread.execute();
        getGraph();
        String text=graph.Dijkstra(graph.getV(start_x, start_y), graph.getV(stop_x, stop_y));//ky exe na duhet per statistika
        return text;

    }




    public String Astar() {
        System.gc();
        getGraph();
        astarthread = new async2();
        astarthread.execute();
        getGraph();
        String text=graph.Astar(graph.getV(start_x, start_y), graph.getV(stop_x, stop_y));
        return text;
    }








    public void getGraph(){
        //marrim nga grid, grafin e kompjutuar,heqim ato tile qe kan qene te perpunuara nga ndonje nga algoritmat me pare
        Graph g = new Graph();
        int counter=0;
        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++) {
                if(grid[i][j]!=3)
                g.addVertex(counter, j, i);
                if(grid[i][j]==4||grid[i][j]==5||grid[i][j]==6)
                    grid[i][j]=0;

                counter++;
            }
        }
        int x1,x2,x3,x4,x5,x6,x7,x8;
        int y1,y2,y3,y4,y5,y6,y7,y8;

        for(int i=0;i<rows;i++) {
            for (int j = 0; j < cols; j++){
                if(grid[i][j]==3)
                    continue;
                x1=j-1;y1=i-1;
                x2=j;y2=i-1;
                x3=j+1;y3=i-1;
                x4=j-1;y4=i;
                x5=j+1;y5=i;
                x6=j-1;y6=i+1;
                x7=j;y7=i+1;
                x8=j+1;y8=i+1;
                if(x1>=0&&x1<cols&&y1>=0&&y1<rows)
                    if(grid[y1][x1]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x1,y1),1.4);
                if(x2>=0&&x2<cols&&y2>=0&&y2<rows)
                    if(grid[y2][x2]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x2,y2),1);
                if(x3>=0&&x3<cols&&y3>=0&&y3<rows)
                    if(grid[y3][x3]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x3,y3),1.4);
                if(x4>=0&&x4<cols&&y4>=0&&y4<rows)
                    if(grid[y4][x4]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x4,y4),1);
                if(x5>=0&&x5<cols&&y5>=0&&y5<rows)
                    if(grid[y5][x5]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x5,y5),1);
                if(x6>=0&&x6<cols&&y6>=0&&y6<rows)
                    if(grid[y6][x6]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x6,y6),1.4);
                if(x7>=0&&x7<cols&&y7>=0&&y7<rows)
                    if(grid[y7][x7]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x7,y7),1);
                if(x8>=0&&x8<cols&&y8>=0&&y8<rows)
                    if(grid[y8][x8]!=3)
                    g.addEdgeGrid(g.getV(j,i),g.getV(x8,y8),1.4);

            }
        }
        graph = g;
        invalidate();
    }


    public class async extends AsyncTask<Void,Void,Void>{
        protected Void doInBackground(Void... params) {
            //pjesa me poshte duhet per grafik
            Vertex start=graph.getV(start_x, start_y);
            Vertex destination = graph.getV(stop_x, stop_y);
            //Initialization
            // d[start]=0 (other vertex's d_value is infinity by default), S={0} , Q = vertex
            LinkedList<Vertex> set = new LinkedList<Vertex>();
            PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
            queue.add(start);
            start.d_value=0;

            //cycle until queue is empty or destination has been inserted into s
            while(!queue.isEmpty()) {
                if (isCancelled()) break;
                Vertex extracted = queue.poll();
                extracted.discovered = true;//kur eshte ne set dhe tashme d_value eshte percaktuar
                set.add(extracted);
                if(grid[(int) extracted.y][(int) extracted.x]!=1&&grid[(int) extracted.y][(int) extracted.x]!=2)
                grid[(int) extracted.y][(int) extracted.x] = 4;
                publishProgress();
                try {
                    Thread.sleep(animationtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (extracted == destination) {
                    break;
                }


                //for each vertex into dhe adj list of extracted -> relax.
                for (int i = 0; i < extracted.edges.size(); i++) {
                    //edge examined
                    Edge edge = extracted.edges.get(i);
                    //get neighbor vertex and relax
                    Vertex neighbor = edge.destination;
                    if (neighbor.discovered == false) {
                        //Relaxation
                        if(grid[(int) neighbor.y][(int) neighbor.x]!=1&&grid[(int) neighbor.y][(int) neighbor.x]!=2)
                        grid[(int) neighbor.y][(int) neighbor.x] = 5;
                        publishProgress();
                        try {
                            Thread.sleep(animationtime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (neighbor.d_value > extracted.d_value + edge.weight) {
                            neighbor.d_value = extracted.d_value + edge.weight;
                            neighbor.parent = extracted;
                            //insert neighbors in queue so we can choose dhe min one
                            queue.remove(neighbor);
                            queue.add(neighbor);
                        }
                    }//if discovered

                }
                }
            Vertex current = destination;
            while (current != null) {
                if (isCancelled()) break;
                if (grid[(int) current.y][(int) current.x] != 1 && grid[(int) current.y][(int) current.x] != 2)
                    grid[(int) current.y][(int) current.x] = 6;
                publishProgress();
                try {
                    Thread.sleep(animationtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                current = current.parent;
            }
            return null;
        }
        protected void onProgressUpdate(Void... values) {
            invalidate();
        }
        protected void onPostExecute(){

        }
    }

    public class async2 extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            Vertex start = graph.getV(start_x, start_y);
            Vertex destination = graph.getV(stop_x, stop_y);

            //Initialization
            LinkedList<Vertex> CLOSED = new LinkedList<Vertex>();
            astarcomparator comparator = new astarcomparator();
            PriorityQueue<Vertex> OPEN = new PriorityQueue<Vertex>(1, comparator);
            start.d_value = 0;
            start.f_value = 0;
            OPEN.add(start);


            //cycle until queue is empty or destination has been inserted into s
            while (!OPEN.isEmpty()) {
                if (isCancelled()) break;
                Vertex extracted = OPEN.poll();
                extracted.discovered = true;//kur eshte ne CLOSED dhe tashme f_value eshte percaktuar
                CLOSED.add(extracted);
                if(grid[(int) extracted.y][(int) extracted.x]!=1&&grid[(int) extracted.y][(int) extracted.x]!=2)
                grid[(int)extracted.y][(int)extracted.x]=4;
                publishProgress();
                try {
                    Thread.sleep(animationtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (extracted == destination) {
                    break;
                }


                //for each vertex into dhe adj list of extracted -> relax.
                for (int i = 0; i < extracted.edges.size(); i++) {
                    //edge examined
                    Edge edge = extracted.edges.get(i);
                    //get neighbor vertex and relax
                    Vertex neighbor = edge.destination;
                    if (neighbor.discovered == false) {
                        //Relaxation
                        if(grid[(int) neighbor.y][(int) neighbor.x]!=1&&grid[(int) neighbor.y][(int) neighbor.x]!=2)
                        grid[(int)neighbor.y][(int)neighbor.x]=5;
                        publishProgress();
                        try {
                            Thread.sleep(animationtime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        graph.heuristic(neighbor, destination);
                        if (neighbor.f_value > extracted.f_value + edge.weight) {
                            neighbor.d_value = extracted.d_value + edge.weight;
                            graph.heuristic(neighbor, destination);
                            neighbor.f_value = neighbor.d_value + neighbor.h_value;
                            neighbor.parent = extracted;
                            //insert neighbors in queue so we can choose dhe min one
                            OPEN.remove(neighbor);
                            OPEN.add(neighbor);
                        }
                    }//if discovered

                }
            }
            Vertex current = destination;
            while(current!=null){
                if (isCancelled()) break;
                if(grid[(int) current.y][(int) current.x]!=1&&grid[(int) current.y][(int) current.x]!=2)
                grid[(int)current.y][(int)current.x]=6;
                publishProgress();
                try {
                    Thread.sleep(animationtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current = current.parent;
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            invalidate();
        }

        protected void onPostExecute(){

        }
    }
}
