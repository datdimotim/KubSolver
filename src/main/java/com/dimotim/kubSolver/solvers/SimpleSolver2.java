package com.dimotim.kubSolver.solvers;

import com.dimotim.kubSolver.kernel.Fase2Solver;
import com.dimotim.kubSolver.kernel.Tables;

import static com.dimotim.kubSolver.kernel.HodTransforms.hodPredHod2Fase;

public final class SimpleSolver2<KS> implements Fase2Solver<KS> {
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables){
        this.tables=tables;
    }
    @Override
    public boolean solve(int x, int y, int z, int[] hods) {
        if(hods.length==0)return tables.isSolved(tables.initKubStateFase1(x,y,z));
        KS[] state=tables.newArrayKubState(hods.length);
        for(int i=0;i<state.length;i++)state[i]=tables.newKubState();
        state[0]= tables.initKubStateFase2(x,y,z);
        if(tables.isSolved(state[0]))return true;
        int deep=1;
        mega: while(deep<hods.length) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod2Fase(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepthFase2(state[deep-1],state[deep],np)<=hods.length-deep-1) {
                    hods[deep] = np;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            if(deep<1)return false;
            hods[deep]++;
        }
        return tables.isSolved(state[state.length-1]);
    }
}
