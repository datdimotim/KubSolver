package kub.kubSolver.solvers;

public class SimpleSolver2 extends Fase2Solver.AbstractSolver2{
    public SimpleSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[][] koords_tmp=new int[MAX_DEEP+1][3];
        koords_tmp[0][0]=x;
        koords_tmp[0][1]=y;
        koords_tmp[0][2]=z;
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.tryMoveAndGetDepth2(koords_tmp[deep-1],np)<=MAX_DEEP-deep) {
                    hods[deep] = np;
                    tables.move2(koords_tmp[deep-1],koords_tmp[deep],np);
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
