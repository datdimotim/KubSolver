package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public class RecursiveSolver2 extends Fase2Solver.AbstractSolver2{
    public RecursiveSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        fase2Recurcive(x,y,z,hods,1);
    }
    private boolean fase2Recurcive(final int x,final int y,final int z,final int[] hods,final int deep){
        for(int np = hods[deep];np<=10;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            int xt=x2Move[np][x];int yt=y2Move[np][y];int zt=z2Move[np][z];
            if ((xz2Deep[xt][zt]>Tables.MAX_DEEP - deep||yz2Deep[yt][zt]>Tables.MAX_DEEP - deep)) continue;
            hods[deep]=np;
            if(deep==Tables.MAX_DEEP)return true;
            else if(fase2Recurcive(xt, yt, zt, hods, deep + 1))return true;
        }
        hods[deep]=0;
        return false;
    }
}
