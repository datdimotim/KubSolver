package kub.kubSolver;

public class HodTransforms {
    public static final int NUM_HODS_1=19;
    public static final int NUM_HODS_2=11;
    private static final int[] p10To18 =new int[]{0,1,2,3,6,9,12,15,16,17,18};
    private static final int[] p18to10=new int[]{0,1,2,3,-1,-1,4,-1,-1,5,-1,-1,6,-1,-1,7,8,9,10};
    private static final String[] hodString = {"",
                                                "D ", "D' ", "D2 ",
                                                "F ", "F' ", "F2 ",
                                                "L ", "L' ", "L2 ",
                                                "R ", "R' ", "R2 ",
                                                "B ", "B' ", "B2 ",
                                                "U ", "U' ", "U2 "};

    private final static int[][] symHodsAllSymmetry=Symmetry.getSymHodsAllSymmetry();
    private static final int[][] symHodsFor3Axis = {symHodsAllSymmetry[0].clone(),
                                                    symHodsAllSymmetry[32].clone(),
                                                    symHodsAllSymmetry[16].clone()};
    public static String[] getHodString() {
        return hodString.clone();
    }

    public static int[] getP10To18() {
        return p10To18.clone();
    }
    public static int[] getP18to10(){return p18to10.clone();}

    public static int[][] getSymHodsFor3Axis() {
        int[][] ret=new int[symHodsFor3Axis.length][];
        for(int i = 0; i< symHodsFor3Axis.length; i++)ret[i]= symHodsFor3Axis[i].clone();
        return ret;
    }

    public static int[][] getSymHodsAllSymmetry() {
        return Symmetry.getSymHodsAllSymmetry();
    }
}
