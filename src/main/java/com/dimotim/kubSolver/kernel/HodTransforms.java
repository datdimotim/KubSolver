package com.dimotim.kubSolver.kernel;

public final class HodTransforms {
    public static final int NUM_HODS_1=19;
    public static final int NUM_HODS_2=11;

    private static final int[] p10To18=getP10To18();

    public static String[] getHodString() {
        return new String[]{"",
                "D ", "D' ", "D2 ",
                "F ", "F' ", "F2 ",
                "L ", "L' ", "L2 ",
                "R ", "R' ", "R2 ",
                "B ", "B' ", "B2 ",
                "U ", "U' ", "U2 "};
    }

    public static int[] getP10To18() {
        return new int[]{0,1,2,3,6,9,12,15,16,17,18};
    }

    public static int[] getP18to10() {
        return new int[]{0,1,2,3,-1,-1,4,-1,-1,5,-1,-1,6,-1,-1,7,8,9,10};
    }

    public static int[][] getSymHodsFor3Axis() {
        int[][] symHodsAllSymmetry=Symmetry.getSymHodsAllSymmetry();
        return new int[][] {symHodsAllSymmetry[0].clone(),
                symHodsAllSymmetry[32].clone(),
                symHodsAllSymmetry[16].clone()};
    }

    public static boolean hodPredHod1Fase(int hod,int predHod){
        if(predHod!=0& hod ==0)return false;
        if(predHod!=0) {
            if ((predHod - 1) / 3==(hod - 1) / 3)return false;
            if ((predHod - 1) / 3==0& (hod - 1) / 3==5)return false;
            if ((predHod - 1) / 3==1&(hod - 1) / 3==4)return false;
            if ((predHod - 1) / 3==2&(hod - 1) / 3==3)return false;
        }
        return true;
    }

    public static boolean hodPredHod2Fase(int hod, int predHod){
        return hodPredHod1Fase(p10To18[hod],p10To18[predHod]);
    }
}