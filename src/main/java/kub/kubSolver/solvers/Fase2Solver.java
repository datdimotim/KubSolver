package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public interface Fase2Solver extends BaseFaseSolver{
    abstract class AbstractSolver2 implements Fase2Solver{
        int[][] x2Move;
        int[][] y2Move;
        int[][] z2Move;
        byte[][] xz2Deep;
        byte[][] yz2Deep;
        private static int[] hodsFase2=Tables.getConvertPovorot();
        @Override
        public void init(Tables tables) {
            x2Move=tables.getX2Move();
            y2Move=tables.getY2Move();
            z2Move=tables.getZ2Move();
            xz2Deep=tables.getXz2Deep();
            yz2Deep=tables.getYz2Deep();
        }
        public static boolean hodPredHod(int hod,int predHod){
            hod=hodsFase2[hod];
            predHod=hodsFase2[predHod];
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
}
