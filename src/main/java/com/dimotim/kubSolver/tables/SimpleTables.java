package com.dimotim.kubSolver.tables;

import com.dimotim.kubSolver.kernel.Tables;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class SimpleTables implements Tables<SimpleTables.KubState> {
    private static final long serialVersionUID = -4965264704365719620L;
    private final MoveTables moveTables=new MoveTables();
    private final byte[] x1Deep=createDeepTable(moveTables.x1Move);
    private final byte[] y1Deep=createDeepTable(moveTables.y1Move);
    private final byte[] z1Deep=createDeepTable(moveTables.z1Move);
    private final byte[] x2Deep=createDeepTable(moveTables.x2Move);
    private final byte[] y2Deep=createDeepTable(moveTables.y2Move);
    private final byte[] z2Deep=createDeepTable(moveTables.z2Move);

    public static void main(String[] args) {
        printChart(new SimpleTables().x2Deep);
    }

    private static void printChart(byte[] deepTable){
        for(int dd=0;true;dd++){
            final int depth=dd;
            long count = IntStream.range(0,deepTable.length)
                    .mapToLong(i->deepTable[i])
                    .filter(d->d==depth)
                    .count();
            if(count==0)return;
            System.out.println(depth + "\t"+ count);
        }
    }

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
        return Math.max(x1Deep[out.x],Math.max(y1Deep[out.y],z1Deep[out.z]));
    }

    @Override
    public int moveAndGetDepthFase2(KubState in, KubState out, int np) {
        out.x=moveTables.x2Move[np][in.x];
        out.y=moveTables.y2Move[np][in.y];
        out.z=moveTables.z2Move[np][in.z];
        return Math.max(x2Deep[out.x],Math.max(y2Deep[out.y],z2Deep[out.z]));
    }

    @Override
    public boolean isSolved(KubState kubState) {
        return Math.max(x2Deep[kubState.x],Math.max(y2Deep[kubState.y],z2Deep[kubState.z]))==0;
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
