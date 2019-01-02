package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.CubieKoordinateConverter;
import com.dimotim.kubSolver.kernel.GraniCubieConverter;
import com.dimotim.kubSolver.kernel.HodTransforms;
import com.dimotim.kubSolver.kernel.Tables;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.SimpleTables2x2;

import static com.dimotim.kubSolver.kernel.Fase1Solver.MAX_DEEP;


public class KubSolver2x2 {
    public static void main(String[] args) {
        Kub2x2 kub2x2=new Kub2x2(true);
        System.out.println(kub2x2);
        KubSolver2x2 kubSolver2x2=new KubSolver2x2();
        KubSolver.Solution solution=kubSolver2x2.solve(kub2x2);
        System.out.println(solution);
        for(int p:solution.getHods())kub2x2.povorot(p);
        System.out.println(kub2x2);
    }
    private final int[] hodsFase2= HodTransforms.getP10To18();
    private final Tables<SimpleTables2x2.KubState> tables=new SimpleTables2x2();
    private final SimpleSolver1<SimpleTables2x2.KubState> fase1Solver=new SimpleSolver1<>();
    private final SimpleSolver2<SimpleTables2x2.KubState> fase2Solver=new SimpleSolver2<>();
    public KubSolver.Solution solve(Kub2x2 kub2x2){
        fase1Solver.init(tables);
        fase2Solver.init(tables);
        SimpleSolver1.SolveState<SimpleTables2x2.KubState> state=initFase1(kub2x2);
        fase1Solver.solve(state);
        int[] hods1=new int[31];
        fase1Solver.getResultFromSolveState(state,hods1);
        int[] hods2=new int[31];
        finishSolution(kub2x2,state,hods2);
        return new KubSolver.Solution(1,hods1,hods2);
    }


    private SimpleSolver1.SolveState<SimpleTables2x2.KubState> initFase1(Kub2x2 kub){
        int[][][] grani= kub.getGrani3x3();
        return fase1Solver.initSolveState(CubieKoordinateConverter.uoToX1(GraniCubieConverter.graniToUO(grani)),0,0);
    }


    private void finishSolution(Kub2x2 kub,SimpleSolver1.SolveState<SimpleTables2x2.KubState> solver1State,int[] fase2){
        int[] fase1solution=new int[MAX_DEEP+1];
        fase1Solver.getResultFromSolveState(solver1State,fase1solution);
        Kub2x2 kubFase2=new Kub2x2(kub);
        for(int i:fase1solution)kubFase2.povorot(i);

        int[][][] grani = kubFase2.getGrani3x3();
        int[] k2 = {
                CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani)),
                CubieKoordinateConverter.rpToY2(GraniCubieConverter.graniToRP(grani)),
                CubieKoordinateConverter.rpToZ2(GraniCubieConverter.graniToRP(grani))
        };
        fase2Solver.solve(k2[0], k2[1], k2[2], fase2);
        for (int i = 0; i < fase2.length; i++) fase2[i] = hodsFase2[fase2[i]];
    }
}
