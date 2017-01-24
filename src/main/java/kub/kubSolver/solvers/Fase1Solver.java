package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public interface Fase1Solver extends BaseFaseSolver{
    abstract class AbstractSolver1 implements Fase1Solver{
        Tables tables;
        int[][] x1Move;
        int[][] y1Move;
        int[][] z1Move;
        byte[][] xy1Deep;
        byte[][] xz1Deep;
        byte[][] yz1Deep;
        @Override
        public void init(Tables tables) {
            x1Move=tables.getX1Move();
            y1Move=tables.getY1Move();
            z1Move=tables.getZ1Move();
            xy1Deep=tables.getXy1Deep();
            xz1Deep=tables.getXz1Deep();
            yz1Deep=tables.getYz1Deep();
            this.tables=tables;
        }
        public static boolean hodPredHod(int hod,int predHod){
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
