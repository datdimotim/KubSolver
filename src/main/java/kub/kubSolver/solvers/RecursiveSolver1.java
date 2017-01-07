package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public class RecursiveSolver1 extends Fase1Solver.AbstractSolver1{
    public RecursiveSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        fase1Recurcive(x, y, z ,hods, 1);
    }
    private boolean fase1Recurcive(final int x,final int y,final int z,final int[] hods,final int deep){
        for(int np = hods[deep];np<=18;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            int xt=x1Move[np][x];int yt=y1Move[np][y];int zt=z1Move[np][z];
            if ((xy1Deep[xt][yt]> Tables.MAX_DEEP - deep||xz1Deep[xt][zt]>Tables.MAX_DEEP - deep||yz1Deep[yt][zt]>Tables.MAX_DEEP - deep)) continue;
            hods[deep]=np;
            if(deep==Tables.MAX_DEEP)return true;
            else if(fase1Recurcive(xt, yt, zt, hods, deep + 1))return true;
        }
        hods[deep]=0;
        return false;
    }
}
