package kub.kubSolver.solvers;

import kub.kubSolver.HodTransforms;
import kub.kubSolver.Tables;

public interface Fase2Solver extends BaseFaseSolver{
    abstract class AbstractSolver2 implements Fase2Solver{
        Tables tables;
        private static int[] hodsFase2= HodTransforms.getP10To18();
        @Override
        public void init(Tables tables) {
            this.tables=tables;
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
