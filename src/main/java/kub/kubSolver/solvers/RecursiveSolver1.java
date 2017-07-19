package kub.kubSolver.solvers;

import kub.kubSolver.Fase1Solver;
import kub.kubSolver.Tables;

public class RecursiveSolver1<KS> implements Fase1Solver<KS>{
    private Tables<KS> tables;
    @Override
    public void init(Tables<KS> tables) {
        this.tables=tables;
    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        KS[] state=tables.newArrayKubState(MAX_DEEP+1);
        for(int i=0;i<state.length;i++)state[i]=tables.newKubState();
        state[0]=tables.initKubStateFase1(x,y,z);
        for(int deep=1;deep<state.length;deep++)tables.moveAndGetDepthFase1(state[deep-1],state[deep],hods[deep]);
        recursiveSolve(state,hods,1);
    }
    private boolean recursiveSolve(KS[] state,int[] hods, final int deep){
        for(int np = hods[deep];np<=18;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            if (tables.moveAndGetDepthFase1(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
                hods[deep] = np;
                if(deep==MAX_DEEP)return true;
                if(recursiveSolve(state,hods,deep+1))return true;
            }
        }
        hods[deep]=0;
        hods[deep-1]++;
        return false;
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
}

