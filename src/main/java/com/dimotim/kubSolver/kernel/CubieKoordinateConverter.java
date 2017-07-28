package com.dimotim.kubSolver.kernel;

public final class CubieKoordinateConverter {
    public static int[] z1ToCubie(int z) {
        int[] r_p = new int[12];
        int[] a=Combinations.intToComb(z,4,12);
        r_p[12-a[0]] = 9;
        r_p[12-a[1]] = 10;
        r_p[12-a[2]] = 11;
        r_p[12-a[3]] = 12;
        int count=1;
        for(int i=0;i<12;i++)if(r_p[i]==0)r_p[i]=count++;
        return r_p;
    }

    public static int[] y1ToCubie(int x) {
        return Combinations.intToPosNumber(x,2,12);
    }

    public static int[] x1ToCubie(int x) {
        return Combinations.intToPosNumber(x,3,8);
    }

    public static int[] x2ToCubie(int x) {
        return Combinations.intToPerestanovka(x,8);
    }

    public static int[] y2ToCubie(int x) {
        int[] m = Combinations.intToPerestanovka(x,8);
        int[] r_p={1,2,3,4,5,6,7,8,9,10,11,12};
        System.arraycopy(m,0,r_p,0,8);
        return r_p;
    }

    public static int[] z2ToCubie(int z) {
        int[] m = Combinations.intToPerestanovka(z, 4);
        int[] r_p = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        for (int i = 0; i < 4; i++) {
            r_p[i + 8] = m[i] + 8;
        }
        return r_p;
    }

    public static int uoToX1(int[] u_o){
        return Combinations.posNumberToInt(u_o,3);
    }

    public static int roToY1(int[] r_o){
        return Combinations.posNumberToInt(r_o,2);
    }

    public static int rpToZ1(int[] r_p){
        int[] a=new int[4];
        int ind=0;
        for (int i = 0; i < 12; i++) {
            if (r_p[i] >= 9) a[ind++]=12-i;
        }
        return Combinations.combToInt(a);
    }

    public static int upToX2(int[] u_p){
        return Combinations.perestanovkaToInt(u_p);
    }

    public static int rpToY2(int[] r_p){
        int[] m = new int[8];
        System.arraycopy(r_p,0,m,0,8);
        return Combinations.perestanovkaToInt(m);
    }

    public static int rpToZ2(int[] r_p){
        int[] m = new int[4];
        for (int i = 0; i < 4; i++) {
            m[i] = r_p[i + 8] - 8;
        }
        return Combinations.perestanovkaToInt(m);
    }
    public static int rpToY2Comb(int[] r_p){
        int[] ind=new int[4];
        int c=0;
        for(int i=0;i<8;i++)if(r_p[i]<=4)ind[3-c++]=i+1;
        return Combinations.combToInt(ind);
    }
    public static int[] y2CombToRp(int x){
        int[] ind=Combinations.intToComb(x,4,8);
        int[] ret={0,0,0,0,0,0,0,0,9,10,11,12};
        int count=1;
        for(int i=0;i<4;i++)ret[ind[3-i]-1]=count++;
        for(int i=0;i<8;i++)if(ret[i]==0)ret[i]=count++;
        return ret;
    }
}
