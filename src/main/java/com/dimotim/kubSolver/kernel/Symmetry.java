package com.dimotim.kubSolver.kernel;

import java.util.Arrays;

public final class Symmetry{
    private static final int[] convertSymHalfToFull={0,1,4,5,8,9,12,13};
    private static final int[] convertSymFullToHalf={0,1,-1,-1,2,3,-1,-1,4,5,-1,-1,6,7};
    private static final int[] hods18to10=HodTransforms.getP18to10();
    private static final int[] hods10to18=HodTransforms.getP10To18();

    private static final int[] inverseSymmetry= InitializerInverseSymmetry.getInverseSymmetry(Symmetry.getSymHodsAllSymmetry());
    private static final int[] inverseSymmetryHalf=initInverseSymmetryHalf(inverseSymmetry);
    private static final int[][] symHods=getSymHodsAllSymmetry();
    private static final int[][] symmetryMul= getSymmetryMul();  // matrix1*matrix2*vector -> matrix*vector

    public static int[] getInverseSymmetry() {
        return inverseSymmetry.clone();
    }

    public static int[] getInverseSymmetryHalf() {
        return inverseSymmetryHalf.clone();
    }

    public static int[][] getSymHodsHalf() {
        return initSymHodsHalf(symHods);
    }

    public static int[][] getSymHods10() {
        return initSymHods10(symHods);
    }

    public static int[][] getSymmetryMulHalf() {
        return getSymmetryMulHalf(symmetryMul);
    }

    private static int[][] initSymHods10(int[][] symHods){
        int[][] symHods10=new int[symHods.length][hods10to18.length];
        for(int s=0;s<symHods.length;s++){
            for(int p=0;p<hods10to18.length;p++){
                symHods10[s][p]=hods18to10[symHods[s][hods10to18[p]]];
            }
        }
        return symHods10;
    }

    private static int[][] initSymHodsHalf(int[][] symHods){
        int[][] symHodsHalf=new int[8][symHods[0].length];
        for(int i=0;i<8;i++){
            System.arraycopy(symHods[convertSymHalfToFull[i]],0,symHodsHalf[i],0,symHodsHalf[0].length);
        }
        return symHodsHalf;
    }

    private static int[] initInverseSymmetryHalf(int[] inverseSymmetry){
        int[] inverseSymmetryHalf=new int[8];
        for (int i=0;i<8;i++)inverseSymmetryHalf[i]=convertSymFullToHalf[inverseSymmetry[convertSymHalfToFull[i]]];
        return inverseSymmetryHalf;
    }

    private static int[][] getSymmetryMulHalf(int[][] symmetryMul){
        int[][] symmetryMulHalf=new int[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                symmetryMulHalf[i][j]=convertSymFullToHalf[symmetryMul[convertSymHalfToFull[i]][convertSymHalfToFull[j]]];
            }
        }
        return symmetryMulHalf;
    }

    public static int[][] getSymmetryMul(){
        int[][] symmetryMul=new int[48][48];
        int[] hodsInit={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
        for(int i=0;i<symmetryMul.length;i++){
            int[] hods1=hodsTransform(hodsInit,i);
            for(int j=0;j<symmetryMul.length;j++){
                int[] hods2=hodsTransform(hods1,j);
                boolean check=false;
                for(int s=0;s<symmetryMul.length;s++){
                    int[] hodsM=hodsTransform(hodsInit,s);
                    if(Arrays.equals(hodsM,hods2)){
                        if(check)throw new RuntimeException();
                        symmetryMul[j][i]=s;
                        check=true;
                    }
                }
            }
        }
        return symmetryMul;
    }

    private static int[] hodsTransform(int[] hods, int s){
        int[] ret=new int[hods.length];
        for(int i=0;i<ret.length;i++)ret[i]=symHods[s][hods[i]];
        return ret;
    }

    public static int[][][] symZ(int[][][] graniIn) { // symmetry n 2
        return Grani.povorot(Grani.povorot(Grani.povorot(graniIn, 19), 4), 14);
    }

    public static int[][][] symX(int[][][] graniIn) { // symmetry n 3
        return Grani.povorot(Grani.povorot(Grani.povorot(graniIn, 10), 8), 22);
    }

    public static int[][][] symY(int[][][] graniIn) {
        return Grani.povorot(Grani.povorot(Grani.povorot(graniIn, 16), 2), 25);
    }

    private static class InitializerInverseSymmetry {
        private static int[] getInverseSymmetry(int[][] symHods){
            int[] inv=new int[symHods.length];
            main: for(int s=0;s<inv.length;s++){
                for(int i=0;i<inv.length;i++){
                    if(Arrays.equals(symHods[0],transform(symHods[s],symHods,i))){
                        inv[s]=i;
                        continue main;
                    }
                }
                throw new RuntimeException();
            }
            return inv;
        }
        private static int[] transform(int[] hods,int[][] symHods, int s){
            int[] t=new int[hods.length];
            for(int i=0;i<hods.length;i++)t[i]=symHods[s][hods[i]];
            return t;
        }
    }
    private static class InitializerSymHods {
        private static int[][] createSymHods() {
            int symmetry[][]=new int[48][];
            for(int i=0;i<24;i++){
                int[] rotatedSymmetry= rotatedSymmetry(i);
                symmetry[i*2]=rotatedHods(rotatedSymmetry);
                symmetry[i*2+1]=mirror(symmetry[i*2]);
            }
            return symmetry;
        }
        private static int[] rotatedSymmetry(int n){
            int[][][] grani=new int[6][3][3];
            for(int g=0;g<6;g++)for(int i=0;i<3;i++)Arrays.fill(grani[g][i],g);

            int R,UD,OS;
            R=n-n/4*4;
            n=n/4;
            UD=n-n/2*2;
            n=n/2;
            OS=n-n/3*3;
            if(OS==1)grani=symX(grani);
            if(OS==2)grani=symZ(grani);
            if(UD==1)grani=symZ(symZ(grani));
            for(int i=0;i<R;i++)grani=symY(grani);
            int m[]=new int[6];
            for(int i=0;i<6;i++)m[i]=grani[i][1][1];
            return m;
        }

        private static int[] rotatedHods(int[] rotatedSymmetry) {
            int[] symmetry = new int[19];
            for (int j = 0; j < 6; j++) {
                symmetry[j * 3 + 1] = rotatedSymmetry[j] * 3 + 1;
                symmetry[j * 3 + 2] = rotatedSymmetry[j] * 3 + 2;
                symmetry[j * 3 + 3] = rotatedSymmetry[j] * 3 + 3;
            }
            return symmetry;
        }

        private static int[] mirror(int[] povorots){
            int[] converter=new int[] { 0,
                                        2,  1,  3,
                                        5,  4,  6,
                                        11, 10, 12,
                                        8,  7,  9,
                                        14, 13, 15,
                                        17, 16, 18};
            int[] m=new int[19];
            for(int i=0;i<19;i++)m[i]=converter[povorots[i]];
            return m;
        }
    }
    public static int[][] getSymHodsAllSymmetry(){
        return InitializerSymHods.createSymHods();
    }
}

