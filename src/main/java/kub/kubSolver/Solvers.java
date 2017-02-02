package kub.kubSolver;

interface BaseFaseSolver {
    int MAX_DEEP=30;
    void init(Tables tables);
    void solve(int x,int y,int z,int[] hods);
}

interface Fase2Solver extends BaseFaseSolver{
    abstract class AbstractSolver2 implements Fase2Solver{
        Tables tables;
        private static int[] hodsFase2= HodTransforms.getP10To18();
        @Override
        public void init(Tables tables) {
            this.tables=tables;
        }
        static boolean hodPredHod(int hod,int predHod){
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

interface Fase1Solver extends BaseFaseSolver{
    abstract class AbstractSolver1 implements Fase1Solver{
        Tables tables;
        @Override
        public void init(Tables tables) {
            this.tables=tables;
        }
        static boolean hodPredHod(int hod,int predHod){
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

class SimpleSolver1 extends Fase1Solver.AbstractSolver1{
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        Tables.CoordSet[] state=new Tables.CoordSet[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new Tables.CoordSet();
        state[0].coord[0]=x; state[0].coord[1]=y; state[0].coord[2]=z;
        tables.initState1(state[0]);
        for(int deep=1;deep<state.length;deep++)tables.moveAndGetDepth1(state[deep-1],state[deep],hods[deep]);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepth1(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
                    hods[deep] = np;
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

class SimpleSolver2 extends Fase2Solver.AbstractSolver2{
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        Tables.CoordSet[] state=new Tables.CoordSet[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new Tables.CoordSet();
        state[0].coord[0]=x; state[0].coord[1]=y; state[0].coord[2]=z;
        tables.initState2(state[0]);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (tables.moveAndGetDepth2(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
                    hods[deep] = np;
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

/*
class RecursiveSolver1 extends Fase1Solver.AbstractSolver1{
    public RecursiveSolver1(){

    }
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        int[][] koords_tmp=new int[MAX_DEEP +1][3];
        koords_tmp[0][0]=x;
        koords_tmp[0][1]=y;
        koords_tmp[0][2]=z;
        fase1Recurcive(koords_tmp,hods, 1);
    }
    private boolean fase1Recurcive(int[][] koords_tmp,int[] hods,int deep){
        for(int np = hods[deep];np<=18;np++) {
            if(!hodPredHod(np,hods[deep-1]))continue;
            if (tables.tryMoveAndGetDepth1(koords_tmp[deep-1],np)> MAX_DEEP - deep) continue;
            hods[deep]=np;
            if(deep==MAX_DEEP)return true;
            else {
                tables.move1(koords_tmp[deep-1],koords_tmp[deep],np);
                if(fase1Recurcive(koords_tmp, hods, deep + 1))return true;
            }
        }
        hods[deep]=0;
        return false;
    }
}

class RecursiveSolver2 extends Fase2Solver.AbstractSolver2{
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
*/