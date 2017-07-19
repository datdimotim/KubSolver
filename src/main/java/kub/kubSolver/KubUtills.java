package kub.kubSolver;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

final class KubFacelet {
    static int[][][] faceletToGrani(int[] facelet) {
        int[][][] grani = new int[6][3][3];
        for (int g = 0; g < 6; g++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    grani[g][i][j] = facelet[j + 3 * i + 9 * g];
                }
            }
        }
        return grani;
    }

    static int[] faceletToUP(int[] facelet){
        int[] u_p=new int[8];
        for (int indPlace = 0; indPlace < 8; indPlace++) {
            int[] colorsKubik = {facelet[KubCubie.ugol_place[indPlace][0] - 1], facelet[KubCubie.ugol_place[indPlace][1] - 1], facelet[KubCubie.ugol_place[indPlace][2] - 1]};
            int ugol_kub = KubCubie.ugol_kub_ind[colorsKubik[0]][colorsKubik[1]][colorsKubik[2]];
            u_p[indPlace] = ugol_kub;
        }
        return u_p;
    }
    static int[] faceletToRP(int[] facelet){
        int[] r_p=new int[12];
        for (int indPlace = 0; indPlace < 12; indPlace++) {
            int[] colorsKubik = {facelet[KubCubie.rebro_place[indPlace][0] - 1], facelet[KubCubie.rebro_place[indPlace][1] - 1]};
            int rebro_kub = KubCubie.rebro_kub_ind[colorsKubik[0]][colorsKubik[1]];
            r_p[indPlace] = rebro_kub;
        }
        return r_p;
    }
    static int[] faceletToUO(int[] facelet){
        int[] u_o=new int[8];
        for (int i = 0; i < 8; i++) {
            for (int orient = 0; orient < 3; orient++) {
                if (facelet[KubCubie.ugol_place[i][orient] - 1] == KubCubie.red_gran || facelet[KubCubie.ugol_place[i][orient] - 1] == KubCubie.orange_gran) {
                    u_o[i] = orient;
                }
            }
        }
        return u_o;
    }
    static int[] faceletToRO(int[] facelet){
        int[] r_o=new int[12];
        for (int i = 0; i < 12; i++) r_o[i] = -1;
        for (int i = 0; i < 12; i++) {
            for (int orient = 0; orient < 2; orient++) {
                if (facelet[KubCubie.rebro_place[i][orient] - 1] == KubCubie.red_gran || facelet[KubCubie.rebro_place[i][orient] - 1] == KubCubie.orange_gran) {
                    r_o[i] = orient;
                }
            }
            if (r_o[i] == -1) {
                if (facelet[KubCubie.rebro_place[i][0] - 1] == KubCubie.yellow_gran || facelet[KubCubie.rebro_place[i][0] - 1] == KubCubie.white_gran) {
                    r_o[i] = 0;
                }
                if (facelet[KubCubie.rebro_place[i][0] - 1] == KubCubie.green_gran || facelet[KubCubie.rebro_place[i][0] - 1] == KubCubie.blue_gran) {
                    r_o[i] = 1;
                }
            }
        }
        return r_o;
    }
}

final class KubKoordinates {
    static int[] z1ToCubie(int z) {
        int[] r_p = new int[12];
        int[] rebro_tmp = new int[12];
        int a1, a2, a3, a4;
        z = z + 1;
        int i;
        for (i = 4; i < 13; i++) {
            if (C(i, 4) >= z) break;
        }
        a1 = i;
        z = z + C(i - 1, 3) - C(i, 4);
        for (i = 3; i < 13; i++) {
            if (C(i, 3) >= z) break;
        }
        a2 = i;
        z = z + C(i - 1, 2) - C(i, 3);
        for (i = 2; i < 13; i++) {
            if (C(i, 2) >= z) break;
        }
        a3 = i;
        z = z + C(i - 1, 1) - C(i, 2);
        for (i = 1; i < 13; i++) {
            if (C(i, 1) >= z) break;
        }
        a4 = i;
        for (i = 0; i < 12; i++) {
            r_p[i] = 0;
        }
        r_p[a1 - 1] = 5;
        r_p[a2 - 1] = 6;
        r_p[a3 - 1] = 7;
        r_p[a4 - 1] = 8;
        for (i = 4; i < 8; i++) {
            rebro_tmp[i] = r_p[i - 4];
        }
        for (i = 0; i < 4; i++) {
            rebro_tmp[i] = r_p[i + 4];
        }
        for (i = 8; i < 12; i++) {
            rebro_tmp[i] = r_p[i];
        }
        for (i = 0; i < 12; i++) {
            r_p[i] = rebro_tmp[i];
        }
        a1 = 0;
        for (i = 0; i < 12; i++) {
            if (r_p[i] == 0) {
                a1 = a1 + 1;
                if (a1 == 5) a1 = a1 + 4;
                r_p[i] = a1;
            }
        }
        return r_p;
    }

    static int[] y1ToCubie(int x) {
        int[] r_o = new int[12];
        for (int i = 11; i > 0; i--) {
            r_o[i] = x - ((x / 2) * 2);
            x = x / 2;
        }
        int s = 0;
        for (int i = 1; i < 12; i++) {
            s = s + r_o[i];
        }
        if (s == s / 2 * 2) r_o[0] = 0;
        else r_o[0] = 1;
        return r_o;
    }

    static int[] x1ToCubie(int x) {
        return Combinations.schetOrientation(x,3,8);
    }

    static int[] x2ToCubie(int x) {
        return Combinations.schetPerestanovka(x,8);
    }

    static int[] y2ToCubie(int x) {
        int[] m = Combinations.schetPerestanovka(x,8);
        int[] r_p=new int[12];
        for (int i = 0; i < 4; i++) {
            if (m[i] > 4) r_p[i] = m[i] + 4;
            else r_p[i] = m[i];
            if (m[i + 4] > 4) r_p[i + 8] = m[i + 4] + 4;
            else r_p[i + 8] = m[i + 4];
            r_p[i + 4] = i + 5;
        }
        return r_p;
    }

    static int[] z2ToCubie(int z) {
        int[] m = Combinations.schetPerestanovka(z,4);
        int[] r_p=new int[12];
        for (int i = 0; i < 4; i++) {
            r_p[i] = i + 1;
            r_p[i + 4] = m[i] + 4;
            r_p[i + 8] = i + 1 + 8;
        }
        return r_p;
    }


    private static int C(int n, int k) {
        int c = 1;
        for (int i = n; i >= n - k + 1; i--) {
            c = c * i;
        }
        for (int i = 1; i <= k; i++) {
            c = c / i;
        }
        return c;
    }
}

final class Symmetry{
    private static final int[] convertSymHalfToFull={0,1,4,5,8,9,12,13};
    private static final int[] convertSymFullToHalf={0,1,-1,-1,2,3,-1,-1,4,5,-1,-1,6,7};
    private static final int[] hods18to10=HodTransforms.p18to10;
    private static final int[] hods10to18=HodTransforms.p10To18;
    static final int[] inverseSymmetry= InitializerInverseSymmetry.getInverseSymmetry(Symmetry.getSymHodsAllSymmetry());
    static final int[] inverseSymmetryHalf=initInverseSymmetryHalf(inverseSymmetry);
    private static final int[][] symHods=getSymHodsAllSymmetry();
    static final int[][] symHodsHalf=initSymHodsHalf(symHods);
    static final int[][] symHods10=initSymHods10(symHods);
    static final int[][] symmetryMul=initSymmetryMul();  // matrix1*matrix2*vector -> matrix*vector
    static final int[][] symmetryMulHalf=initSymmetryMulHalf(symmetryMul);

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

    private static int[][] initSymmetryMulHalf(int[][] symmetryMul){
        int[][] symmetryMulHalf=new int[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                symmetryMulHalf[i][j]=convertSymFullToHalf[symmetryMul[convertSymHalfToFull[i]][convertSymHalfToFull[j]]];
            }
        }
        return symmetryMulHalf;
    }

    private static int[][] initSymmetryMul(){
        int[][] symmetryMul=new int[16][16];
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

    static int[][][] normalizeColors(int[][][] graniIn){
        int[][][] grani=new int[6][3][3];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int c=0;c<6;c++){
                        if(graniIn[i][j][k]==graniIn[c][1][1]){
                            grani[i][j][k]=c;
                            break;
                        }
                    }
                }
            }
        }
        return grani;
    }
    static int[][] getSymHodsAllSymmetry(){
        return InitializerSymHods.createSymHods();
    }
    static int[][][] symZ(int[][][] graniIn) { // symmetry n 2
        return KubGrani.povorot(KubGrani.povorot(KubGrani.povorot(graniIn, 19), 4), 14);
    }
    static int[][][] symX(int[][][] graniIn) { // symmetry n 3
        return KubGrani.povorot(KubGrani.povorot(KubGrani.povorot(graniIn, 10), 8), 22);
    }
    static int[][][] symY(int[][][] graniIn) { return KubGrani.povorot(KubGrani.povorot(KubGrani.povorot(graniIn, 16), 2), 25);}
}

final class KubGrani {
    private static int convertNapr(int n){
        if (n == 1) n = 3;
        else if (n == 2) n = 1;
        else if (n == 3) n = 2;
        return n;
    }
    static int[][][] povorot(int[][][] graniIn, int np) {
        final int povorot = (np - 1) / 3 + 1;
        int napravlenie = (np - 1) - (np - 1) / 3 * 3 + 1;
        final int red_gran = 1, white_gran = 2, blue_gran = 4, green_gran = 3, yellow_gran = 5, orange_gran = 6, white_sr = 7, blue_sr = 8, orange_sr = 9;
        final int[][][] grani_tmp = new int[6][3][3];
        final int[][][] grani = new int[6][3][3];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    grani_tmp[i][j][k] = graniIn[i][j][k];
                    grani[i][j][k] = graniIn[i][j][k];
                }
            }
        }
        switch (povorot) {
            case (red_gran): {
                napravlenie=convertNapr(napravlenie);
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[red_gran - 1][0][i] = grani[red_gran - 1][2 - i][0];
                        grani_tmp[red_gran - 1][1][i] = grani[red_gran - 1][2 - i][1];
                        grani_tmp[red_gran - 1][2][i] = grani[red_gran - 1][2 - i][2];
                        grani_tmp[white_gran - 1][2][i] = grani[blue_gran - 1][2][i];
                        grani_tmp[blue_gran - 1][2][i] = grani[yellow_gran - 1][2][2 - i];
                        grani_tmp[yellow_gran - 1][2][i] = grani[green_gran - 1][2][i];
                        grani_tmp[green_gran - 1][2][i] = grani[white_gran - 1][2][2 - i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (orange_gran): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[orange_gran - 1][0][i] = grani[orange_gran - 1][2 - i][0];
                        grani_tmp[orange_gran - 1][1][i] = grani[orange_gran - 1][2 - i][1];
                        grani_tmp[orange_gran - 1][2][i] = grani[orange_gran - 1][2 - i][2];
                        grani_tmp[white_gran - 1][0][i] = grani[blue_gran - 1][0][i];
                        grani_tmp[blue_gran - 1][0][i] = grani[yellow_gran - 1][0][2 - i];
                        grani_tmp[yellow_gran - 1][0][i] = grani[green_gran - 1][0][i];
                        grani_tmp[green_gran - 1][0][i] = grani[white_gran - 1][0][2 - i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (orange_sr): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[white_gran - 1][1][i] = grani[blue_gran - 1][1][i];
                        grani_tmp[blue_gran - 1][1][i] = grani[yellow_gran - 1][1][2 - i];
                        grani_tmp[yellow_gran - 1][1][i] = grani[green_gran - 1][1][i];
                        grani_tmp[green_gran - 1][1][i] = grani[white_gran - 1][1][2 - i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (white_gran): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[white_gran - 1][0][i] = grani[white_gran - 1][2 - i][0];
                        grani_tmp[white_gran - 1][1][i] = grani[white_gran - 1][2 - i][1];
                        grani_tmp[white_gran - 1][2][i] = grani[white_gran - 1][2 - i][2];
                        grani_tmp[orange_gran - 1][2][i] = grani[green_gran - 1][2 - i][0];
                        grani_tmp[green_gran - 1][i][0] = grani[red_gran - 1][2][i];
                        grani_tmp[red_gran - 1][2][i] = grani[blue_gran - 1][2 - i][0];
                        grani_tmp[blue_gran - 1][i][0] = grani[orange_gran - 1][2][i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (white_sr): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[orange_gran - 1][1][i] = grani[green_gran - 1][2 - i][1];
                        grani_tmp[green_gran - 1][i][1] = grani[red_gran - 1][1][i];
                        grani_tmp[red_gran - 1][1][i] = grani[blue_gran - 1][2 - i][1];
                        grani_tmp[blue_gran - 1][i][1] = grani[orange_gran - 1][1][i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (yellow_gran): {
                napravlenie=convertNapr(napravlenie);
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[yellow_gran - 1][0][i] = grani[yellow_gran - 1][2 - i][0];
                        grani_tmp[yellow_gran - 1][1][i] = grani[yellow_gran - 1][2 - i][1];
                        grani_tmp[yellow_gran - 1][2][i] = grani[yellow_gran - 1][2 - i][2];
                        grani_tmp[orange_gran - 1][0][i] = grani[green_gran - 1][2 - i][2];
                        grani_tmp[green_gran - 1][i][2] = grani[red_gran - 1][0][i];
                        grani_tmp[red_gran - 1][0][i] = grani[blue_gran - 1][2 - i][2];
                        grani_tmp[blue_gran - 1][i][2] = grani[orange_gran - 1][0][i];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (green_gran): {
                napravlenie=convertNapr(napravlenie);
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[green_gran - 1][0][i] = grani[green_gran - 1][2 - i][0];
                        grani_tmp[green_gran - 1][1][i] = grani[green_gran - 1][2 - i][1];
                        grani_tmp[green_gran - 1][2][i] = grani[green_gran - 1][2 - i][2];
                        grani_tmp[orange_gran - 1][i][0] = grani[white_gran - 1][i][0];
                        grani_tmp[white_gran - 1][i][0] = grani[red_gran - 1][2 - i][0];
                        grani_tmp[red_gran - 1][i][0] = grani[yellow_gran - 1][i][0];
                        grani_tmp[yellow_gran - 1][i][0] = grani[orange_gran - 1][2 - i][0];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (blue_gran): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[blue_gran - 1][0][i] = grani[blue_gran - 1][2 - i][0];
                        grani_tmp[blue_gran - 1][1][i] = grani[blue_gran - 1][2 - i][1];
                        grani_tmp[blue_gran - 1][2][i] = grani[blue_gran - 1][2 - i][2];
                        grani_tmp[orange_gran - 1][i][2] = grani[white_gran - 1][i][2];
                        grani_tmp[white_gran - 1][i][2] = grani[red_gran - 1][2 - i][2];
                        grani_tmp[red_gran - 1][i][2] = grani[yellow_gran - 1][i][2];
                        grani_tmp[yellow_gran - 1][i][2] = grani[orange_gran - 1][2 - i][2];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
            case (blue_sr): {
                if (napravlenie == 2) napravlenie = 3;
                else if (napravlenie == 3) napravlenie = 2;
                for (int ind = 0; ind < napravlenie; ind++) {
                    for (int i = 0; i < 3; i++) {
                        grani_tmp[orange_gran - 1][i][1] = grani[white_gran - 1][i][1];
                        grani_tmp[white_gran - 1][i][1] = grani[red_gran - 1][2 - i][1];
                        grani_tmp[red_gran - 1][i][1] = grani[yellow_gran - 1][i][1];
                        grani_tmp[yellow_gran - 1][i][1] = grani[orange_gran - 1][2 - i][1];
                    }
                    for (int ii = 0; ii < 6; ii++) {
                        for (int j = 0; j < 3; j++) {
                            System.arraycopy(grani_tmp[ii][j], 0, grani[ii][j], 0, 3);
                        }
                    }
                }
                break;
            }
        }
        return grani_tmp;
    }

    static int[] graniToFacelet(int[][][] grani) {
        int[] facelet = new int[54];
        for (int g = 0; g < 6; g++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    facelet[j + 3 * i + 9 * g] = grani[g][i][j];
                }
            }
        }
        return facelet;
    }
}

final class KubCubie {
    static final int orange_gran = 5, yellow_gran = 4, blue_gran = 3, green_gran = 2, white_gran = 1, red_gran = 0;

    private static final int[][] ugol_color = {{orange_gran, green_gran, yellow_gran},
            {orange_gran, yellow_gran, blue_gran},
            {orange_gran, blue_gran, white_gran},
            {orange_gran, white_gran, green_gran},
            {red_gran, yellow_gran, green_gran},
            {red_gran, blue_gran, yellow_gran},
            {red_gran, white_gran, blue_gran},
            {red_gran, green_gran, white_gran}};
    static final int[][][] ugol_kub_ind = new int[6][6][6];

    static {
        for (int i = 0; i < ugol_color.length; i++) {
            int col1 = ugol_color[i][0];
            int col2 = ugol_color[i][1];
            int col3 = ugol_color[i][2];
            ugol_kub_ind[col1][col2][col3] = i + 1;
            ugol_kub_ind[col1][col3][col2] = i + 1;
            ugol_kub_ind[col2][col1][col3] = i + 1;
            ugol_kub_ind[col2][col3][col1] = i + 1;
            ugol_kub_ind[col3][col1][col2] = i + 1;
            ugol_kub_ind[col3][col2][col1] = i + 1;
        }
    }

    static final int[][] ugol_place = {{46, 21, 37},
            {48, 39, 30},
            {54, 28, 12},
            {52, 10, 19},
            {1, 43, 27},
            {3, 36, 45},
            {9, 18, 34},
            {7, 25, 16}};
    private static final int[][] rebro_color = {{orange_gran, yellow_gran},
            {orange_gran, blue_gran},
            {orange_gran, white_gran},
            {orange_gran, green_gran},
            {yellow_gran, green_gran},
            {yellow_gran, blue_gran},
            {white_gran, blue_gran},
            {white_gran, green_gran},
            {red_gran, yellow_gran},
            {red_gran, blue_gran},
            {red_gran, white_gran},
            {red_gran, green_gran}};
    static final int[][] rebro_place = {{47, 38},
            {51, 29},
            {53, 11},
            {49, 20},
            {40, 24},
            {42, 33},
            {15, 31},
            {13, 22},
            {2, 44},
            {6, 35},
            {8, 17},
            {4, 26}};
    static final int[][] rebro_kub_ind = new int[6][6];

    static {
        for (int i = 0; i < rebro_color.length; i++) {
            int col1 = rebro_color[i][0];
            int col2 = rebro_color[i][1];
            rebro_kub_ind[col1][col2] = i + 1;
            rebro_kub_ind[col2][col1] = i + 1;
        }
    }
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
            int[] facelet=KubGrani.graniToFacelet(KubGrani.povorot(KubFacelet.faceletToGrani(KubCubie.cubieToFacelet(u_oS,u_pS,r_oS,r_pS)),np));
            int[] u_o=KubFacelet.faceletToUO(facelet);
            int[] u_p=KubFacelet.faceletToUP(facelet);
            int[] r_o=KubFacelet.faceletToRO(facelet);
            int[] r_p=KubFacelet.faceletToRP(facelet);
            rPerest[np]=r_p;
            uPerest[np]=u_p;
            rFlip[np]=r_o;
            uFlip[np]=u_o;
        }
    }

    static void povorotUP(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++)out[i]=in[uPerest[np][i]-1];
    }
    static void povorotRP(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++)out[i]=in[rPerest[np][i]-1];
    }
    static void povorotUO(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++){
            out[i]=in[uPerest[np][i]-1];
            out[i]+=uFlip[np][i];
            if(out[i]>2)out[i]-=3;
        }
    }
    static void povorotRO(int[] in,int[] out,int np){
        for (int i=0;i<out.length;i++){
            out[i]=in[rPerest[np][i]-1];
            out[i]+=rFlip[np][i];
            if(out[i]>1)out[i]-=2;
        }
    }

    static int[] cubieToFacelet(int[] u_o,int[] u_p,int[] r_o,int[] r_p) {
        int[] facelet = new int[54];
        {
            int[][] colors_poradok_orientation_massiv_tmp = new int[8][3];
            int[][] colors_poradok_orientation_massiv = new int[8][3];// значения--цвета,первый индекс--цвета в порядке ориентации,второй индекс,цвета--в порядке номеров кубиков
            int op;
            for (int i = 0; i < 8; i++) {
                System.arraycopy(KubCubie.ugol_color[u_p[i] - 1], 0, colors_poradok_orientation_massiv[i], 0, 3);
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    op = j + 1 + u_o[i];
                    if (op > 3) op = op - 3;
                    colors_poradok_orientation_massiv_tmp[i][op - 1] = colors_poradok_orientation_massiv[i][j];//поворачиваем переставленные кубики
                }
            }
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 3; j++) {
                    facelet[KubCubie.ugol_place[i][j] - 1] = colors_poradok_orientation_massiv_tmp[i][j];
                }
            }
        }
        {
            int[][] colors_poradok_orientation_massiv_tmp = new int[12][2];
            int[][] colors_poradok_orientation_massiv = new int[12][2];// значения--цвета,первый индекс--цвета в порядке ориентации,второй индекс,цвета--в порядке номеров кубиков
            int op;
            for (int i = 0; i < 12; i++) {
                System.arraycopy(KubCubie.rebro_color[r_p[i] - 1], 0, colors_poradok_orientation_massiv[i], 0, 2);
            }
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 2; j++) {
                    op = j + 1 + r_o[i];
                    if (op > 2) op = op - 2;
                    colors_poradok_orientation_massiv_tmp[i][op - 1] = colors_poradok_orientation_massiv[i][j];//поворачиваем переставленные кубики
                }
            }
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 2; j++) {
                    facelet[KubCubie.rebro_place[i][j] - 1] = colors_poradok_orientation_massiv_tmp[i][j];
                }
            }
        }
        return facelet;
    }

    static int uoToX1(int[] u_o){
        return Combinations.schetOrientation(u_o,3);
    }
    static int roToY1(int[] r_o){
        return Combinations.schetOrientation(r_o,2);
    }
    static int rpToZ1(int[] r_p){
        int a1 = 0, a2 = 0, a3 = 0, a4 = 0;
        int[] rebro = new int[12];
        System.arraycopy(r_p, 8, rebro, 8, 4);
        System.arraycopy(r_p, 0, rebro, 4, 4);
        System.arraycopy(r_p, 4, rebro, 0, 4);
        for (int i = 0; i < 12; i++) {
            if (rebro[i] >= 5 & rebro[i] <= 8) rebro[i] = 1;
            else rebro[i] = 0;
        }
        for (int i = 11; i >= 0; i--) {
            if (rebro[i] == 1) {
                a1 = i;
                rebro[i] = 0;
                break;
            }
        }
        for (int i = 11; i >= 0; i--) {
            if (rebro[i] == 1) {
                a2 = i;
                rebro[i] = 0;
                break;
            }
        }
        for (int i = 11; i >= 0; i--) {
            if (rebro[i] == 1) {
                a3 = i;
                rebro[i] = 0;
                break;
            }
        }
        for (int i = 11; i >= 0; i--) {
            if (rebro[i] == 1) {
                a4 = i;
                rebro[i] = 0;
                break;
            }
        }
        a1++;a2++;a3++;a4++;
        return Combinations.C(a1, 4) - Combinations.C(a1 - 1, 3) +
                Combinations.C(a2, 3) - Combinations.C(a2 - 1, 2) +
                Combinations.C(a3, 2) - Combinations.C(a3 - 1, 1) + Combinations.C(a4, 1) - 1;
    }
    static int upToX2(int[] u_p){
        return Combinations.schetPerestanovka(u_p);
    }
    static int rpToY2(int[] r_p){
        int[] m = new int[8];
        for (int i = 0; i < 4; i++) {
            if (r_p[i] > 4) m[i] = r_p[i] - 4;
            else m[i] = r_p[i];
            if (r_p[i + 8] > 4) m[i + 4] = r_p[i + 8] - 4;
            else m[i + 4] = r_p[i + 8];
        }
        return Combinations.schetPerestanovka(m);
    }
    static int rpToZ2(int[] r_p){
        int[] m = new int[4];
        for (int i = 0; i < 4; i++) {
            m[i] = r_p[i + 4] - 4;
        }
        return Combinations.schetPerestanovka(m);
    }
}

class HodTransforms {
    static final int NUM_HODS_1=19;
    static final int NUM_HODS_2=11;
    static final int[] p10To18 =new int[]{0,1,2,3,6,9,12,15,16,17,18};
    static final int[] p18to10=new int[]{0,1,2,3,-1,-1,4,-1,-1,5,-1,-1,6,-1,-1,7,8,9,10};
    static final String[] hodString = {"",
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

    static int[][] getSymHodsFor3Axis() {
        int[][] ret=new int[symHodsFor3Axis.length][];
        for(int i = 0; i< symHodsFor3Axis.length; i++)ret[i]= symHodsFor3Axis[i].clone();
        return ret;
    }
}

final class Combinations{
    static int schetOrientation(int[] m,int max){
        int x = 0;
        for (int i = m.length-1; i > 0; i--)x +=m[i]*pow(max,(m.length-1-i));
        return x;
    }
    static int[] schetOrientation(int ch,int max,int length){
        int[] m = new int[length];
        for (int i = length-1; i > 0; i--) {
            m[i] = ch - ((ch / max) * max);
            ch = ch / max;
        }
        int s = 0;
        for (int i = 1; i < length; i++) s = s + m[i];
        m[0]=max-s+s/max*max;
        if(m[0]==max)m[0]=0;
        return m;
    }
    static int schetPerestanovka(int[] m){
        int ch = 0;
        int n = m.length;
        int[] poradok = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            int sum = 0;
            for (int aM : m) {
                if (aM > i + 1) sum = sum + 1;
                if (aM == i + 1) {
                    poradok[i] = sum;
                    break;
                }
            }
        }
        int factorial = 1;
        for (int i = n - 2; i > -1; i--) {
            factorial = factorial * (n - 1 - i);
            ch = ch + poradok[i] * factorial;
        }
        return ch;
    }
    static int[] schetPerestanovka(int ch,int length){
        int[] m = new int[length];
        int[] poradok = new int[length - 1];
        for (int i = 0; i < length; i++) {
            m[i] = length;
        }
        for (int i = 0; i < length - 1; i++) {
            poradok[length - 1 - i - 1] = ch - ch / (i + 1 + 1) * (i + 1 + 1);
            ch = ch / (i + 1 + 1);
        }
        for (int i = 0; i < length - 1; i++) {
            int sum = 0;
            for (int j = 0; j < length; j++) {
                if (m[j] > i + 1) sum = sum + 1;
                if (sum == poradok[i] + 1) {
                    m[j] = i + 1;
                    break;
                }
            }
        }
        return m;
    }
    private static int pow(int osn,int stepen){
        int ret=1;
        for(int i=0;i<stepen;i++)ret*=osn;
        return ret;
    }

    static int C(int n, int k) {
        int c = 1;
        for (int i = n; i >= n - k + 1; i--) {
            c = c * i;
        }
        for (int i = 1; i <= k; i++) {
            c = c / i;
        }
        return c;
    }

    static boolean chetNechetPerestanovka(int[] mIn) {
        int[] m = new int[mIn.length];
        System.arraycopy(mIn, 0, m, 0, mIn.length);
        boolean perest = false;
        main:
        for (int i = 0; i < m.length; i++) {
            if (m[i] != i + 1) {
                for (int j = i + 1; j < m.length; j++) {
                    if (m[j] == i + 1) {
                        perestPair(m, i, j);
                        perest = !perest;
                        continue main;
                    }
                }
                throw new RuntimeException("Perestanovka Error");
            }
        }
        return perest;
    }

    private static void perestPair(int[] m, int i, int j) {
        int tmp = m[i];
        m[i] = m[j];
        m[j] = tmp;
    }
}