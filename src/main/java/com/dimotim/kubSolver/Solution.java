package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.HodTransforms;

public class Solution {
    private static final int[][] symHods= HodTransforms.getSymHodsFor3Axis();
    private static final String[] hodString=HodTransforms.getHodString();
    public final int length;
    private final int[] hods;
    private final int sym;

    public Solution(int sym, int[] fase1, int[] fase2) {
        this.sym = sym;
        fase1=nomalize(fase1);
        fase2=nomalize(fase2);
        length =fase1.length+fase2.length;
        hods = new int[length];
        int kol=0;
        for(int hod:fase1)hods[kol++]=hod;
        for(int hod:fase2)hods[kol++]=hod;
        kol=0;
        for (int hod:hods) hods[kol++] = symHods[sym - 1][hod];
    }

    private static int[] nomalize(int [] hods){
        int s = 0;
        for (int hod : hods) if (hod != 0) s++;
        int[] res=new int[s];
        s=0;
        for (int hod : hods) if (hod != 0) res[s++]=hod;
        return res;
    }

    public int[] getHods() {
        return hods.clone();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int hod : hods) out.append(hodString[hod]);
        return out.toString() + length + "f symmerty="+sym;
    }
}