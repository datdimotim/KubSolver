package kub.kubSolver.solvers;

import static kub.kubSolver.Tables.CoordSet;

public class SimpleSolver1 extends Fase1Solver.AbstractSolver1{
    public SimpleSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        CoordSet[] state=new CoordSet[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new CoordSet();
        state[0].coord[0]=x; state[0].coord[1]=y; state[0].coord[2]=z;
        tables.initState1(state[0]);

        for(int deep=1;deep<state.length;deep++){
            tables.moveAndGetDepth1(state[deep-1],state[deep],hods[deep]);
        }
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepth1(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
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
