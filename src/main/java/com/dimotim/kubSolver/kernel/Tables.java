package com.dimotim.kubSolver.kernel;

import java.io.*;

public interface Tables<KubState> extends Serializable{
    int X_1_MAX =2187;
    int Y_1_MAX =2048;
    int Z_1_MAX =495;
    int X_2_MAX =40320;
    int Y_2_MAX =40320;
    int Z_2_MAX =24;
    int X_2_COMB =70;
    int X_1_SYM_CLASSES =324;
    int Y_1_SYM_CLASSES =336;
    int Z_1_SYM_CLASSES =81;
    int X_2_SYM_CLASSES =2768;
    int Y_2_SYM_CLASSES =2768;
    int Z_2_SYM_CLASSES =8;
    int X_2_COMB_SYM_CLASSES =14;

    KubState initKubStateFase1(int x,int y,int z);
    KubState initKubStateFase2(int x,int y,int z);
    int moveAndGetDepthFase1(KubState in, KubState out, int np);
    int moveAndGetDepthFase2(KubState in, KubState out, int np);
    int getDepthInState(KubState state);

    KubState newKubState();
    KubState[] newArrayKubState(int length);
}

