package com.leartgjoni.app.pathfindingsimulator.Graph;

import java.util.Comparator;


public class astarcomparator implements Comparator<Vertex>{

    @Override
    public int compare(Vertex arg0, Vertex arg1) {
        // TODO Auto-generated method stub
        return Double.compare(arg0.f_value,arg1.f_value);
    }

}

