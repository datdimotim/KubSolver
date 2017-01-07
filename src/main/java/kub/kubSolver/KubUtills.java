package kub.kubSolver;

import kub.kubSolver.utills.Combinations;

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
    private static int[][] symHods=createSymHods();

    private static int[][] getSymHods(){
        int[][] hods=new int[symHods.length][];
        for(int i=0;i<symHods.length;i++)hods[i]=symHods[i].clone();
        return hods;
    }

    private static int[][] createSymHods() {
        int symmetry[][]=new int[48][];
        for(int i=0;i<24;i++){
            int[] rotatedSymmetry= rotatedSymmetry(i);
            symmetry[i*2]=rotatedHods(rotatedSymmetry);
            symmetry[i*2+1]=mirror(symmetry[i*2]);
        }
        return symmetry;
    }

    private static void proofHodsDuplicate(int[][] symHods){
        for(int i=0;i<symHods.length-1;i++)for(int j=i+1;j<symHods.length;j++)if(Arrays.equals(symHods[i],symHods[j]))throw new RuntimeException("равенство"+"  i="+i+"  j="+j);
    }

    private static void proofHodsMatrix(int sym){
        Kub kub=new Kub();
        int[] zaput=new int[200];
        Random random=new Random(System.currentTimeMillis());
        for(int i=0;i<zaput.length;i++)zaput[i]=random.nextInt(19);
        for(int p:zaput)kub.povorot(p);
        KubSolver kubSolver=new KubSolver();
        int[] solution=kubSolver.solve(kub,null,1).getHods();

        for(int i=0;i<zaput.length;i++)zaput[i]=symHods[sym][zaput[i]];
        for(int i=0;i<solution.length;i++)solution[i]=symHods[sym][solution[i]];

        kub=new Kub();
        for (int p : zaput) kub.povorot(p);
        for (int p : solution) kub.povorot(p);
        //System.out.println(kub);
        if(kub.getNumberPos().compareTo(BigDecimal.ZERO)!=0)throw new RuntimeException("proof fail");
    }

    public static void main(String[] args) {
        //for(int[] m:symHods) System.out.println(Arrays.toString(m));
        proofHodsDuplicate(symHods);
        while (true) {
            for(int i=0;i<symHods.length;i++) proofHodsMatrix(i);
            System.out.println("c");
        }
    }

    private static int[] rotatedSymmetry(int n){
        int[][][] grani=new Kub().getGrani();
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
        int[] converter=new int[] {0, 2,  1,  3,      5, 4,  6,  11, 10, 12,
                                      8,  7,  9,      14,13,15,  17, 16, 18};
        int[] m=new int[19];
        for(int i=0;i<19;i++)m[i]=converter[povorots[i]];
        return m;
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