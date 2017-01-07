package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public class SimpleSolver1 extends Fase1Solver.AbstractSolver1{
    public SimpleSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[] hods_x_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_y_tmp=new int[Tables.MAX_DEEP +1];
        int[] hods_z_tmp=new int[Tables.MAX_DEEP +1];
        hods_x_tmp[0]=x;
        hods_y_tmp[0]=y;
        hods_z_tmp[0]=z;
        for(int deep=1;deep<hods_x_tmp.length;deep++){
            hods_x_tmp[deep]=x1Move[hods[deep]][hods_x_tmp[deep-1]];
            hods_y_tmp[deep]=y1Move[hods[deep]][hods_y_tmp[deep-1]];
            hods_z_tmp[deep]=z1Move[hods[deep]][hods_z_tmp[deep-1]];
        }
        int deep=1;
        mega: while(deep<=Tables.MAX_DEEP) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                int xt = x1Move[np][hods_x_tmp[deep-1]];
                int yt = y1Move[np][hods_y_tmp[deep-1]];
                int zt = z1Move[np][hods_z_tmp[deep-1]];
                if (xy1Deep[xt][yt] <= Tables.MAX_DEEP - deep && xz1Deep[xt][zt] <= Tables.MAX_DEEP - deep && yz1Deep[yt][zt] <= Tables.MAX_DEEP - deep) {
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
