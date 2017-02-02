package kub.kubSolver;

public class Solution {
    private static final int[][] symHods=HodTransforms.getSymHodsFor3Axis();
    private static final String[] hodString=HodTransforms.getHodString();
    public final int length;
    private final int[] hods;
    private final int sym;
    private final int[] fase1;

    Solution(int sym, int[] fase1, int[] fase2) {
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
        this.fase1=fase1;
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

    int[] getFase1(){
        return fase1.clone();
    }

    public String toString() {
        String out = "";
        for (int hod : hods) out += hodString[hod];
        return out + length + "f symmerty="+sym;
    }
}
