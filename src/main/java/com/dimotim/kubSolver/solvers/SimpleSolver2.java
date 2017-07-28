package com.dimotim.kubSolver.solvers;

import com.dimotim.kubSolver.kernel.Fase2Solver;
import com.dimotim.kubSolver.kernel.HodTransforms;
import com.dimotim.kubSolver.kernel.Tables;

public final class SimpleSolver2<KS> implements Fase2Solver<KS> {
    private Tables<KS> tables;
    private static int[] hodsFase2= HodTransforms.getP10To18();
    @Override
    public void init(Tables<KS> tables){
        this.tables=tables;
    }
    @Override
    public boolean solve(int x, int y, int z, int[] hods) {
        if(hods.length==0)return tables.getDepthInState(tables.initKubStateFase1(x,y,z))==0;
        KS[] state=tables.newArrayKubState(hods.length);
        for(int i=0;i<state.length;i++)state[i]=tables.newKubState();
        state[0]= tables.initKubStateFase2(x,y,z);
        if(tables.getDepthInState(state[0])==0)return true;
        int deep=1;
        mega: while(deep<hods.length) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
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
        return tables.getDepthInState(state[state.length-1])==0;
    }
    private static boolean hodPredHod(int hod,int predHod){
        hod=hodsFase2[hod];
        predHod=hodsFase2[predHod];
        if(predHod!=0& hod ==0)return false;
        if(predHod!=0) {
            if ((predHod - 1) / 3==(hod - 1) / 3)return false;
            if ((predHod - 1) / 3==0& (hod - 1) / 3==5)return false;
            if ((predHod - 1) / 3==1&(hod - 1) / 3==4)return false;
            if ((predHod - 1) / 3==2&(hod - 1) / 3==3)return false;
        }
        return true;
    }
}
