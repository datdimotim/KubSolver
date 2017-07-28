package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Tables;

import java.util.Arrays;

public final class DoubleTables implements Tables<DoubleTables.KubState> {
    private final MoveTables moveTables=new MoveTables();
    private final byte[][] xy1Deep=createDeepTable(moveTables.x1Move,moveTables.y1Move);
    private final byte[][] xz1Deep=createDeepTable(moveTables.x1Move,moveTables.z1Move);
    private final byte[][] yz1Deep=createDeepTable(moveTables.y1Move,moveTables.z1Move);
    private final byte[][] xz2Deep=createDeepTable(moveTables.x2Move,moveTables.z2Move);
    private final byte[][] yz2Deep=createDeepTable(moveTables.y2Move,moveTables.z2Move);

    private byte[][] createDeepTable(char[][] move1,char[][] move2){
        byte[][] deep=new byte[move1[0].length][move2[0].length];
        for (byte[] b:deep)Arrays.fill(b,(byte) 20);
        deep[0][0]=0;
        for(int d=0;d<20;d++){
            for(int p1=0;p1<deep.length;p1++){
                for(int p2=0;p2<deep[0].length;p2++) {
                    if (deep[p1][p2] != d) continue;
                    for (int np = 0; np < move1.length; np++) {
                        if (deep[move1[np][p1]][move2[np][p2]] > d + 1) deep[move1[np][p1]][move2[np][p2]] = (byte) (d + 1);
                    }
                }
            }
        }
        return deep;
    }

    @Override
    public KubState initKubStateFase1(int x, int y, int z) {
        KubState kubState=new KubState();
        kubState.x=x;
        kubState.y=y;
        kubState.z=z;
        return kubState;
    }

    @Override
    public KubState initKubStateFase2(int x, int y, int z) {
        KubState kubState=new KubState();
        kubState.x=x;
        kubState.y=y;
        kubState.z=z;
        return kubState;
    }

    @Override
    public int moveAndGetDepthFase1(KubState in, KubState out, int np) {
        out.x=moveTables.x1Move[np][in.x];
        out.y=moveTables.y1Move[np][in.y];
        out.z=moveTables.z1Move[np][in.z];
        return Math.max(xy1Deep[out.x][out.y],Math.max(xz1Deep[out.x][out.z],yz1Deep[out.y][out.z]));
    }

    @Override
    public int moveAndGetDepthFase2(KubState in, KubState out, int np) {
        out.x=moveTables.x2Move[np][in.x];
        out.y=moveTables.y2Move[np][in.y];
        out.z=moveTables.z2Move[np][in.z];
        return Math.max(xz2Deep[out.x][out.z],yz2Deep[out.y][out.z]);
    }

    @Override
    public int getDepthInState(KubState kubState) {
        return Math.max(xz2Deep[kubState.x][kubState.z],yz2Deep[kubState.y][kubState.z]);
    }

    @Override
    public KubState newKubState() {
        return new KubState();
    }

    @Override
    public KubState[] newArrayKubState(int length) {
        return new KubState[length];
    }

    public static class KubState{
        private int x;
        private int y;
        private int z;
    }
}
