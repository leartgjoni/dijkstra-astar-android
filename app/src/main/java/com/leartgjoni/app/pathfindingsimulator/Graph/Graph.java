package com.leartgjoni.app.pathfindingsimulator.Graph;

import java.util.*;
public class Graph {

    public ArrayList<Vertex> vertex=new ArrayList<Vertex>();

    public void addVertex(int id){
        Vertex v1 = new Vertex(id);
        vertex.add(v1);
    }
    public void addVertex(int id,double x,double y){
        Vertex v1 = new Vertex(id,x,y);
        vertex.add(v1);
    }
    public void addEdge(Vertex source,Vertex destination,double weight){
        source.edges.add(new Edge(source,destination,weight));
        destination.edges.add(new Edge(destination,source,weight));
    }
    public void addEdgeGrid(Vertex source,Vertex destination,double weight){
        //per grid mqs do inicializojm vertex edget e tij do e bejme edge vtm nje drejtmsh
        source.edges.add(new Edge(source,destination,weight));
    }
    public Vertex getV(double x,double y){
        for(int i=0;i<vertex.size();i++)
            if(vertex.get(i).x==x&&vertex.get(i).y==y)
                return vertex.get(i);
        //nese nuk gjendet
        return null;
    }

    public void linkedlist(){
        for(int i=0;i<vertex.size();i++){
            System.out.print(vertex.get(i).id+": ");
            for(int j=0;j<vertex.get(i).edges.size();j++){
                System.out.print("=>"+vertex.get(i).edges.get(j).destination.id);
            }
            System.out.println();
        }
    }

    public String Dijkstra(Vertex start,Vertex destination){
        String text="Alg: Dijkstra "; //do te mbajme ketu rezultatet e perpunimeve
        //Initialization
        // d[start]=0 (other vertex's d_value is infinity by default), S={0} , Q = vertex
        long startTime = System.nanoTime();
        LinkedList<Vertex> set = new LinkedList<Vertex>();
        PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
        queue.add(start);
        start.d_value=0;

        //cycle until queue is empty or destination has been inserted into s
        while(!queue.isEmpty()){

            Vertex extracted = queue.poll();
            extracted.discovered=true;//kur eshte ne set dhe tashme d_value eshte percaktuar
            set.add(extracted);
            if(extracted==destination){
                break;
            }


            //for each vertex into dhe adj list of extracted -> relax.
            for(int i=0;i<extracted.edges.size();i++){
                //edge examined
                Edge edge = extracted.edges.get(i);
                //get neighbor vertex and relax
                Vertex neighbor = edge.destination;
                if(neighbor.discovered==false){
                    //Relaxation
                    if(neighbor.d_value>extracted.d_value+edge.weight){
                        neighbor.d_value=extracted.d_value+edge.weight;
                        neighbor.parent=extracted;
                        //insert neighbors in queue so we can choose dhe min one
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }//if discovered

            }
        }
        long stopTime = System.nanoTime();
        if(destination.parent==null)
            text="This path does not exist";
        else{

            text+=" Vertex ne set: "+set.size();
            //Dijkstra process finished, now we will take our path and print it
            Stack<Vertex> stack = new Stack<Vertex>();
            Vertex current = destination;
            while(current!=null){
                stack.push(current);
                current = current.parent;
            }
            double path_length = destination.d_value;

            text+=" Nr.Hops:"+(stack.size()-1)+" Path length: "+String.format( "%.2f", path_length )+" Time: "+(stopTime-startTime)+" ns";
        }//fund else per ekzistencen e path
        return text;
    }
    public String Astar(Vertex start,Vertex destination){
        String text="Alg: A*(A star) ";
        //Initialization
        long startTime = System.nanoTime();
        LinkedList<Vertex> CLOSED = new LinkedList<Vertex>();
        astarcomparator comparator = new astarcomparator();
        PriorityQueue<Vertex> OPEN = new PriorityQueue<Vertex>(1,comparator);
        start.d_value=0;
        start.f_value=0;
        OPEN.add(start);


        //cycle until queue is empty or destination has been inserted into s
        while(!OPEN.isEmpty()){

            Vertex extracted = OPEN.poll();
            extracted.discovered=true;//kur eshte ne CLOSED dhe tashme f_value eshte percaktuar
            CLOSED.add(extracted);
            if(extracted==destination){
                break;
            }


            //for each vertex into dhe adj list of extracted -> relax.
            for(int i=0;i<extracted.edges.size();i++){
                //edge examined
                Edge edge = extracted.edges.get(i);
                //get neighbor vertex and relax
                Vertex neighbor = edge.destination;
                if(neighbor.discovered==false){
                    //Relaxation
                    heuristic(neighbor,destination);
                    if(neighbor.f_value>extracted.f_value+edge.weight){
                        neighbor.d_value=extracted.d_value+edge.weight;
                        heuristic(neighbor,destination);
                        neighbor.f_value=neighbor.d_value+neighbor.h_value;
                        neighbor.parent=extracted;
                        //insert neighbors in queue so we can choose dhe min one
                        OPEN.remove(neighbor);
                        OPEN.add(neighbor);
                    }
                }//if discovered

            }
        }
        long stopTime = System.nanoTime();
        //Pjesa e shkrimeve

        if(destination.parent==null)
            text="This path does not exist";
        else{
            text+=" Vertex ne CLOSED: "+CLOSED.size();
            System.out.println();
            //Dijkstra process finished, now we will take our path and print it
            Stack<Vertex> stack = new Stack<Vertex>();
            Vertex current = destination;
            while(current!=null){
                stack.push(current);
                current = current.parent;
            }
            int hops = stack.size() - 1;
            double path_length = destination.d_value;
            text+=" Nr.Hops:"+hops+" Path length: "+String.format( "%.2f", path_length )+" Time: "+(stopTime-startTime)+" ns";
        }//fund else per ekzistencen e path
        return text;
    }
    public void heuristic(Vertex v,Vertex destination){
        //distance heuklidiane
        v.h_value=Math.sqrt((v.x-destination.x)*(v.x-destination.x)+
                (v.y-destination.y)*(v.y-destination.y));
    }


}

