package kub.kubSolver;

import static kub.kubSolver.BaseFaseSolver.MAX_DEEP;

public class KubSolver{
    private Fase1Solver fase1Solver;
    private Fase2Solver fase2Solver;
    private final SymTables tables;
    private static final int[] hodsFase2=HodTransforms.getP10To18();
    public KubSolver(){
        this.tables=SymTables.readTables();
        //setFase1Solver(new SimpleSolver1());
        //setFase2Solver(new SimpleSolver2());
        setFase1Solver(new SymSimpleSolver1());
        setFase2Solver(new SymSimpleSolver2());
    }
    public KubSolver(Fase1Solver fase1Solver,Fase2Solver fase2Solver){
        this.tables=SymTables.readTables();
        setFase1Solver(fase1Solver);
        setFase2Solver(fase2Solver);
    }
    private void setFase1Solver(Fase1Solver solver){
        fase1Solver=solver;
        if(solver!=null)solver.init(tables);
    }
    private void setFase2Solver(Fase2Solver solver){
        fase2Solver=solver;
        if(solver!=null)solver.init(tables);
    }
    public Solution solve(Kub kub,Solution preSolution,int sym){
        int[][][] grani;
        switch (sym){
            case 1:{
                grani=kub.getGrani();
                break;
            }
            case 2:{
                grani=Symmetry.normalizeColors(Symmetry.symZ(kub.getGrani()));
                break;
            }
            case 3:{
                grani=Symmetry.normalizeColors(Symmetry.symX(kub.getGrani()));
                break;
            }
            default:throw new IllegalArgumentException("sym="+sym+", but 1<=sym<=3");
        }
        Cubie cubie;
        try {
            cubie=new Cubie(grani);
        } catch (InvalidPositionException e) {
            throw new RuntimeException(e);
        }

        int[] fase1solution=null;
        if(preSolution!=null)fase1solution=fase1HodsAdapter(preSolution.getFase1());
        if(fase1solution!=null)incrementHods1(fase1solution);
        else fase1solution=new int[MAX_DEEP +1];

        if(fase1Solver!=null){
            int[] k1=cubie.toKoordinates1();
            fase1Solver.solve(k1[0], k1[1], k1[2],fase1solution);
            for (int p : fase1solution) cubie=cubie.povorot(p);
            k1=cubie.toKoordinates1();
            if(k1[0]!=0||k1[1]!=0||k1[2]!=0)throw new RuntimeException("Kub 1 fase error");
        }

        int[] fase2solution=new int[MAX_DEEP +1];

        if(fase2Solver!=null) {
            int[] k2 = cubie.toKoordinates2();
            fase2Solver.solve(k2[0], k2[1], k2[2], fase2solution);
            for(int i=0;i<fase2solution.length;i++)fase2solution[i]=hodsFase2[fase2solution[i]];
            for (int p : fase2solution) cubie=cubie.povorot(p);
            k2=cubie.toKoordinates2();
            if(k2[0]!=0||k2[1]!=0||k2[2]!=0)throw new RuntimeException("Kub 2 fase error");
        }
        return new Solution(sym, fase1solution, fase2solution);
    }

    private static int[] fase1HodsAdapter(int[] fase1){
        int[] ret=new int[MAX_DEEP+1];
        System.arraycopy(fase1,0,ret,MAX_DEEP+1-fase1.length,fase1.length);
        return ret;
    }

    private static void incrementHods1(int[] hods){
        int sm = hods.length - 1;
        while(true) {
            hods[sm]++;
            if (hods[sm] <= 18) break;
            else {
                hods[sm] = 0;
                sm--;
            }
        }
    }
}
