package com.dimotim.kubSolver;

import static com.dimotim.kubSolver.Combinations.C;

public final class CubieKoordinateConverter {
    public static int[] z1ToCubie(int z) {
        int[] r_p = new int[12];
        int a1, a2, a3, a4;
        z = z + 1;
        int i;
        for (i = 4; i < 13; i++) if (C(i, 4) >= z) break;
        a1 = i;
        z = z + C(i - 1, 3) - C(i, 4);
        for (i = 3; i < 13; i++) if (C(i, 3) >= z) break;
        a2 = i;
        z = z + C(i - 1, 2) - C(i, 3);
        for (i = 2; i < 13; i++) if (C(i, 2) >= z) break;
        a3 = i;
        z = z + C(i - 1, 1) - C(i, 2);
        for (i = 1; i < 13; i++) if (C(i, 1) >= z) break;
        a4 = i;
        r_p[12-a1] = 9;
        r_p[12-a2] = 10;
        r_p[12-a3] = 11;
        r_p[12-a4] = 12;
        int count=1;
        for(i=0;i<12;i++)if(r_p[i]==0)r_p[i]=count++;
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
        return C(a[0], 4) - C(a[0] - 1, 3) +
                C(a[1], 3) - C(a[1] - 1, 2) +
                C(a[2], 2) - C(a[2] - 1, 1) + C(a[3], 1) - 1;
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
}
