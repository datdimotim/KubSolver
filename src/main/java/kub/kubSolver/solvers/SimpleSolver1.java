package kub.kubSolver.solvers;

public class SimpleSolver1 extends Fase1Solver.AbstractSolver1{
    public SimpleSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[][] koord_tmp=new int[MAX_DEEP +1][3];
        koord_tmp[0][0]=x;
        koord_tmp[0][1]=y;
        koord_tmp[0][2]=z;
        for(int deep=1;deep<koord_tmp.length;deep++){
            tables.move1(koord_tmp[deep-1],koord_tmp[deep],hods[deep]);
        }
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.tryMoveAndGetDepth1(koord_tmp[deep-1],np)<=MAX_DEEP-deep) {
                    hods[deep] = np;
                    tables.move1(koord_tmp[deep-1],koord_tmp[deep],np);
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
