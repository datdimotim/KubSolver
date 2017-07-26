package com.dimotim.kubSolver;

public final class Cubie {
    private static final int[][] rPerest=new int[19][12];
    private static final int[][] uPerest=new int[19][8];
    private static final int[][] rFlip=new int[19][12];
    private static final int[][] uFlip=new int[19][8];

    static {
        int[] u_oS=new int[8];
        int[] r_oS=new int[12];
        int[] u_pS={1,2,3,4,5,6,7,8};
        int[] r_pS={1,2,3,4,5,6,7,8,9,10,11,12};
        for (int np=0;np<19;np++){
            int[][][] grani= Grani.povorot((GraniCubieConverter.cubieToGrani(u_oS,u_pS,r_oS,r_pS)),np);
            int[] u_o= GraniCubieConverter.graniToUO(grani);
            int[] u_p= GraniCubieConverter.graniToUP(grani);
            int[] r_o= GraniCubieConverter.graniToRO(grani);
            int[] r_p= GraniCubieConverter.graniToRP(grani);
            rPerest[np]=r_p;
            uPerest[np]=u_p;
            rFlip[np]=r_o;
            uFlip[np]=u_o;
        }
    }

    public static void povorotUP(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++)out[i]=in[uPerest[np][i]-1];
    }
    public static void povorotRP(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++)out[i]=in[rPerest[np][i]-1];
    }
    public static void povorotUO(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++){
            out[i]=in[uPerest[np][i]-1];
            out[i]+=uFlip[np][i];
            if(out[i]>2)out[i]-=3;
        }
    }
    public static void povorotRO(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++){
            out[i]=in[rPerest[np][i]-1];
            out[i]+=rFlip[np][i];
            if(out[i]>1)out[i]-=2;
        }
    }

}
