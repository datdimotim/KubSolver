package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Tables;

import java.util.Arrays;

public final class SimpleTables2x2 implements Tables<SimpleTables2x2.KubState> {
    private final MoveTables moveTables=new MoveTables();
    private final byte[] x1Deep=createDeepTable(moveTables.x1Move);
    private final byte[] x2Deep=createDeepTable(moveTables.x2Move);

    private byte[] createDeepTable(char[][] move){
        byte[] deep=new byte[move[0].length];
        Arrays.fill(deep,(byte) 20);
        deep[0]=0;
        for(int d=0;d<20;d++){
            for(int p=0;p<deep.length;p++){
                if(deep[p]!=d)continue;
                for (int np=0;np<move.length;np++){
                    if(deep[move[np][p]]>d+1)deep[move[np][p]]=(byte)(d+1);
                }
            }
        }
        return deep;
    }

    @Override
    public KubState initKubStateFase1(int x, int y, int z) {
        KubState kubState=new KubState();
        kubState.x=x;
        return kubState;
    }

    @Override
    public KubState initKubStateFase2(int x, int y, int z) {
        KubState kubState=new KubState();
        kubState.x=x;
        return kubState;
    }

    @Override
    public int moveAndGetDepthFase1(KubState in, KubState out, int np) {
        out.x=moveTables.x1Move[np][in.x];
        return x1Deep[out.x];
    }

    @Override
    public int moveAndGetDepthFase2(KubState in, KubState out, int np) {
        out.x=moveTables.x2Move[np][in.x];
        return x2Deep[out.x];
    }

    @Override
    public int getDepthInState(KubState kubState) {
        return x2Deep[kubState.x];
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
    }
}
