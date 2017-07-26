package com.dimotim.kubSolver;

public class GraniCubieConverter {
    public static final int orange_gran = 5;
    public static final int yellow_gran = 4;
    public static final int blue_gran = 3;
    public static final int green_gran = 2;
    public static final int white_gran = 1;
    public static final int red_gran = 0;
    private static final int[][] rebro_color = {{orange_gran, yellow_gran},
            {orange_gran, blue_gran},
            {orange_gran, white_gran},
            {orange_gran, green_gran},
            {red_gran, yellow_gran},
            {red_gran, blue_gran},
            {red_gran, white_gran},
            {red_gran, green_gran},
            {yellow_gran, green_gran},
            {yellow_gran, blue_gran},
            {white_gran, blue_gran},
            {white_gran, green_gran}};
    private static final int[][] ugol_color = {{orange_gran, green_gran, yellow_gran},
                    {orange_gran, yellow_gran, blue_gran},
                    {orange_gran, blue_gran, white_gran},
                    {orange_gran, white_gran, green_gran},
                    {red_gran, yellow_gran, green_gran},
                    {red_gran, blue_gran, yellow_gran},
                    {red_gran, white_gran, blue_gran},
                    {red_gran, green_gran, white_gran}};
    private static final int[][] ugol_place = {{46, 21, 37},
            {48, 39, 30},
            {54, 28, 12},
            {52, 10, 19},
            {1, 43, 27},
            {3, 36, 45},
            {9, 18, 34},
            {7, 25, 16}};
    private static final int[][] rebro_place = {{47, 38},
            {51, 29},
            {53, 11},
            {49, 20},
            {2, 44},
            {6, 35},
            {8, 17},
            {4, 26},
            {40, 24},
            {42, 33},
            {15, 31},
            {13, 22}};
    private static final int[][][] ugol_kub_ind = new int[6][6][6];

    static {
        for (int i = 0; i < GraniCubieConverter.ugol_color.length; i++) {
            int col1 = GraniCubieConverter.ugol_color[i][0];
            int col2 = GraniCubieConverter.ugol_color[i][1];
            int col3 = GraniCubieConverter.ugol_color[i][2];
            ugol_kub_ind[col1][col2][col3] = i + 1;
            ugol_kub_ind[col1][col3][col2] = i + 1;
            ugol_kub_ind[col2][col1][col3] = i + 1;
            ugol_kub_ind[col2][col3][col1] = i + 1;
            ugol_kub_ind[col3][col1][col2] = i + 1;
            ugol_kub_ind[col3][col2][col1] = i + 1;
        }
    }

    private static final int[][] rebro_kub_ind = new int[6][6];

    static {
        for (int i = 0; i < GraniCubieConverter.rebro_color.length; i++) {
            int col1 = GraniCubieConverter.rebro_color[i][0];
            int col2 = GraniCubieConverter.rebro_color[i][1];
            rebro_kub_ind[col1][col2] = i + 1;
            rebro_kub_ind[col2][col1] = i + 1;
        }
    }

    public static int[][][] cubieToGrani(int[] u_o, int[] u_p, int[] r_o, int[] r_p) {
        int[] facelet = new int[54];
        {
            int[][] colors_poradok_orientation_massiv_tmp = new int[8][3];
            int[][] colors_poradok_orientation_massiv = new int[8][3];// значения--цвета,первый индекс--цвета в порядке ориентации,второй индекс,цвета--в порядке номеров кубиков
            int op;
            for (int i = 0; i < 8; i++) {
                System.arraycopy(ugol_color[u_p[i] - 1], 0, colors_poradok_orientation_massiv[i], 0, 3);
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
                    facelet[ugol_place[i][j] - 1] = colors_poradok_orientation_massiv_tmp[i][j];
                }
            }
        }
        {
            int[][] colors_poradok_orientation_massiv_tmp = new int[12][2];
            int[][] colors_poradok_orientation_massiv = new int[12][2];// значения--цвета,первый индекс--цвета в порядке ориентации,второй индекс,цвета--в порядке номеров кубиков
            int op;
            for (int i = 0; i < 12; i++) {
                System.arraycopy(rebro_color[r_p[i] - 1], 0, colors_poradok_orientation_massiv[i], 0, 2);
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
                    facelet[rebro_place[i][j] - 1] = colors_poradok_orientation_massiv_tmp[i][j];
                }
            }
        }
        return faceletToGrani(facelet);
    }

    public static int[] graniToUP(int[][][] grani){
        int[] facelet=graniToFacelet(grani);
        int[] u_p=new int[8];
        for (int indPlace = 0; indPlace < 8; indPlace++) {
            int[] colorsKubik = {facelet[ugol_place[indPlace][0] - 1], facelet[ugol_place[indPlace][1] - 1], facelet[ugol_place[indPlace][2] - 1]};
            int ugol_kub = ugol_kub_ind[colorsKubik[0]][colorsKubik[1]][colorsKubik[2]];
            u_p[indPlace] = ugol_kub;
        }
        return u_p;
    }

    public static int[] graniToRP(int[][][] grani){
        int[] facelet=graniToFacelet(grani);
        int[] r_p=new int[12];
        for (int indPlace = 0; indPlace < 12; indPlace++) {
            int[] colorsKubik = {facelet[rebro_place[indPlace][0] - 1], facelet[rebro_place[indPlace][1] - 1]};
            int rebro_kub = rebro_kub_ind[colorsKubik[0]][colorsKubik[1]];
            r_p[indPlace] = rebro_kub;
        }
        return r_p;
    }

    public static int[] graniToUO(int[][][] grani){
        int[] facelet=graniToFacelet(grani);
        int[] u_o=new int[8];
        for (int i = 0; i < 8; i++) {
            for (int orient = 0; orient < 3; orient++) {
                if (facelet[ugol_place[i][orient] - 1] == red_gran || facelet[ugol_place[i][orient] - 1] == orange_gran) {
                    u_o[i] = orient;
                }
            }
        }
        return u_o;
    }

    public static int[] graniToRO(int[][][] grani){
        int[] facelet=graniToFacelet(grani);
        int[] r_o=new int[12];
        for (int i = 0; i < 12; i++) r_o[i] = -1;
        for (int i = 0; i < 12; i++) {
            for (int orient = 0; orient < 2; orient++) {
                if (facelet[rebro_place[i][orient] - 1] == red_gran || facelet[rebro_place[i][orient] - 1] == orange_gran) {
                    r_o[i] = orient;
                }
            }
            if (r_o[i] == -1) {
                if (facelet[rebro_place[i][0] - 1] == yellow_gran || facelet[rebro_place[i][0] - 1] == white_gran) {
                    r_o[i] = 0;
                }
                if (facelet[rebro_place[i][0] - 1] == green_gran || facelet[rebro_place[i][0] - 1] == blue_gran) {
                    r_o[i] = 1;
                }
            }
        }
        return r_o;
    }

    private static int[] graniToFacelet(int[][][] grani) {
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

    private static int[][][] faceletToGrani(int[] facelet) {
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
}
