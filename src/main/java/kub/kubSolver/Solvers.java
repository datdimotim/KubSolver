package kub.kubSolver;

interface BaseFaseSolver {
    int MAX_DEEP=30;
    SymTables symTables=new SymTables();
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

class SymSimpleSolver1 extends Fase1Solver.AbstractSolver1{
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        SymTables.KubState[] state=new SymTables.KubState[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new SymTables.KubState();
        state[0]=symTables.initKubStateFase1(x,y,z);
        for(int deep=1;deep<state.length;deep++)symTables.moveAndGetDetphFase1(state[deep-1],state[deep],hods[deep]);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=18;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (symTables.moveAndGetDetphFase1(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
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

class SymSimpleSolver2 extends Fase2Solver.AbstractSolver2{
    @Override
    public void solve(int x, int y, int z, int[] hods) {
        SymTables.KubState[] state=new SymTables.KubState[MAX_DEEP+1];
        for(int i=0;i<state.length;i++)state[i]=new SymTables.KubState();
        state[0]=symTables.initKubStateFase2(x,y,z);
        int deep=1;
        mega: while(deep<=MAX_DEEP) {
            for(int np = hods[deep];np<=10;np++) {
                if(!hodPredHod(np,hods[deep-1]))continue;
                if (symTables.moveAndGetDetphFase2(state[deep-1],state[deep],np)<=MAX_DEEP-deep) {
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