package kub.kubSolver.solvers;

public class RecursiveSolver2 extends Fase2Solver.AbstractSolver2{
    public RecursiveSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[][] koords_tmp=new int[MAX_DEEP+1][3];
        koords_tmp[0][0]=x;
        koords_tmp[0][1]=y;
        koords_tmp[0][2]=z;
        fase2Recurcive(koords_tmp,hods,1);
    }
    private boolean fase2Recurcive(int[][] koords_tmp,final int[] hods,final int deep){
        for(int np = hods[deep];np<=10;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            if (tables.tryMoveAndGetDepth2(koords_tmp[deep-1],np)>MAX_DEEP - deep) continue;
            hods[deep]=np;
            if(deep==MAX_DEEP)return true;
            else {
                tables.move2(koords_tmp[deep-1],koords_tmp[deep],np);
                if(fase2Recurcive(koords_tmp, hods, deep + 1))return true;
            }
        }
        hods[deep]=0;
        return false;
    }
}
