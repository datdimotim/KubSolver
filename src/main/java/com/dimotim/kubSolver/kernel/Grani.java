package com.dimotim.kubSolver.kernel;

public final class Grani {
    private static int convertNapr(int n){
        if (n == 1) n = 3;
        else if (n == 2) n = 1;
        else if (n == 3) n = 2;
        return n;
    }
    public static int[][][] povorot(int[][][] graniIn, int np) {
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

}
