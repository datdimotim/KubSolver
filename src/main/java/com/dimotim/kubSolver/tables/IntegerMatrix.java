package com.dimotim.kubSolver.tables;

import com.dimotim.compact_arrays.CompactIntegerArrayShift;
import com.dimotim.compact_arrays.IntegerArray;

import java.io.Serializable;

public final class IntegerMatrix implements Serializable {
    private static final long serialVersionUID = -9045342885638574768L;
    private final IntegerArray array;
    final int iLength;
    final int jLength;
    IntegerMatrix(int maxI, int maxJ, int maxVal){
        iLength =maxI;
        jLength =maxJ;
        int i=0;
        int v=1;
        while (v<maxVal){
            v*=2;
            i++;
        }
        array=new CompactIntegerArrayShift(maxI*maxJ,i);
        //array=new CompactIntegerArrayDivide(maxI*maxJ,maxVal);
    }
    void set(int i,int j,int val){
        array.set(i* jLength +j,val);
    }
    long get(int i,int j){
        return array.get(i* jLength +j);
    }
}
