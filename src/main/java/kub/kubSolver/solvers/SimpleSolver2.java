package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

import static kub.kubSolver.Tables.*;

public class SimpleSolver2 extends Fase2Solver.AbstractSolver2{
    public SimpleSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        CoordSet[] state=new CoordSet[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new CoordSet();
        state[0].coord[0]=x; state[0].coord[1]=y; state[0].coord[2]=z;
        tables.initState2(state[0]);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepth2(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
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
}
