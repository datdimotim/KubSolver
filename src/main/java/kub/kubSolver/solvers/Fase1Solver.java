package kub.kubSolver.solvers;

import kub.kubSolver.Tables;

public interface Fase1Solver extends BaseFaseSolver{
    abstract class AbstractSolver1 implements Fase1Solver{
        protected Tables tables;
        @Override
        public void init(Tables tables) {
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
