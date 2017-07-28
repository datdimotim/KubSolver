package com.dimotim.kubSolver.solvers;

import com.dimotim.kubSolver.kernel.Fase1Solver;
import com.dimotim.kubSolver.kernel.Tables;

public final class SimpleSolver1<KS> implements Fase1Solver<KS,SimpleSolver1.SolveState<KS>> {
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables) {
        this.tables=tables;
    }

    @Override
    public SolveState<KS> initSolveState(int x, int y, int z) {
        return new SolveState<>(tables,x,y,z);
    }

    @Override
    public void getResultFromSolveState(SolveState<KS> state, int[] hods) {
        System.arraycopy(state.hods,0,hods,0,state.hods.length);
    }

    @Override
    public void solve(SolveState<KS> state) {
        while (true) {
            incrementHods1(state.hods);
            int deep = 1;
            mega:
            while (deep <= MAX_DEEP) {
                for (int np = state.hods[deep]; np <= 18; np++) {
                    if (!hodPredHod(np, state.hods[deep - 1])) continue;
                    if (tables.moveAndGetDepthFase1(state.state[deep - 1], state.state[deep], np) <= MAX_DEEP - deep) {
                        state.hods[deep] = np;
                        deep++;
                        continue mega;
                    }
                }
                state.hods[deep] = 1;
                deep--;
                state.hods[deep]++;
            }
            if(validFase1NewSolution(state.hods))return;
        }
    }

    private static boolean hodPredHod(int hod,int predHod){
        if(predHod!=0& hod ==0)return false;
        if(predHod!=0) {
            if ((predHod - 1) / 3==(hod - 1) / 3)return false;
            if ((predHod - 1) / 3==0& (hod - 1) / 3==5)return false;
            if ((predHod - 1) / 3==1&(hod - 1) / 3==4)return false;
            if ((predHod - 1) / 3==2&(hod - 1) / 3==3)return false;
        }
        return true;
    }

    private static boolean validFase1NewSolution(int[] face1NewSolutuion){
        int[] pureFase1hods={0,4,5,7,8,10,11,13,14}; //0 - т.к. решение не может заканчиваться 0
        for(int j:pureFase1hods)if(face1NewSolutuion[face1NewSolutuion.length-1]==j)return true;
        return false;
    }

    private static void incrementHods1(int[] hods){
        hods[hods.length-1]++;
    }

    public static class SolveState<KS>{
        private final int[] hods;
        private final KS[] state;
        private SolveState(Tables<KS> tables,int x, int y,int z) {
            hods=new int[MAX_DEEP+1];
            hods[hods.length-1]=-1;
            state = tables.newArrayKubState(MAX_DEEP + 1);
            for (int i = 0; i < state.length; i++) state[i] = tables.newKubState();
            state[0] = tables.initKubStateFase1(x, y, z);
        }
    }
}

