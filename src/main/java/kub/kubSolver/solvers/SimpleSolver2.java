package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public class SimpleSolver2 extends Fase2Solver.AbstractSolver2{
    public SimpleSolver2(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[] hods_x_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_y_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_z_tmp=new int[Tables.MAX_DEEP +1];
        hods_x_tmp[0]=x;
        hods_y_tmp[0]=y;
        hods_z_tmp[0]=z;
        int deep=1;
        mega: while(deep<=Tables.MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                int xt = x2Move[np][hods_x_tmp[deep-1]];
                int yt = y2Move[np][hods_y_tmp[deep-1]];
                int zt = z2Move[np][hods_z_tmp[deep-1]];
                if (xz2Deep[xt][zt] <= Tables.MAX_DEEP - deep && yz2Deep[yt][zt] <= Tables.MAX_DEEP - deep) {
                    hods[deep] = np;
                    hods_x_tmp[deep] = xt;
                    hods_y_tmp[deep] = yt;
                    hods_z_tmp[deep] = zt;
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
