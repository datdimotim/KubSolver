package kub.kubSolver.solvers;

import kub.kubSolver.Fase2Solver;
import kub.kubSolver.Tables;

public class SymSimpleSolver2<KS> implements Fase2Solver<KS> {
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables){
        this.tables=tables;
    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        KS[] state=tables.newArrayKubState(MAX_DEEP+1);
        for(int i=0;i<state.length;i++)state[i]=tables.newKubState();
        state[0]= tables.initKubStateFase2(x,y,z);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.moveAndGetDetphFase2(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
                    hods[deep] = np;
                    deep++;
                    continue mega;
                }
            }
            hods[deep]=0;
            deep--;
            hods[deep]++;
        }
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
