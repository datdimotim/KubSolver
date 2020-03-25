package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.*;

import static com.dimotim.kubSolver.kernel.Fase1Solver.MAX_DEEP;

public final class KubSolver<KS,Solver1State>{
    private final int tryKol=0;
    private final int maxSolutionLength=30;
    private Fase1Solver<KS,Solver1State> fase1Solver;
    private Fase2Solver<KS> fase2Solver;
    private static final int[] hodsFase2=HodTransforms.getP10To18();
    public KubSolver(Tables<KS> tables,Fase1Solver<KS,Solver1State> fase1Solver,Fase2Solver<KS> fase2Solver){
        this.fase1Solver=fase1Solver;
        this.fase2Solver=fase2Solver;
        this.fase1Solver.init(tables);
        this.fase2Solver.init(tables);
    }

    public Solution solve(Kub kub){
        Kub kub1=getSymmetryKub(kub,1);
        Kub kub2=getSymmetryKub(kub,2);
        Kub kub3=getSymmetryKub(kub,3);
        Solver1State solver1State1=initFase1(kub1);
        Solver1State solver1State2=initFase1(kub2);
        Solver1State solver1State3=initFase1(kub3);
        int[] fase1solution=new int[MAX_DEEP+1];
        int[] fase2solution;
        int tk=0;
        int targetLength = maxSolutionLength;
        Solution solution=null;
        while (true) {
            if(solution!=null){
                tk++;
                if(tk>tryKol)return solution;
            }
            fase1Solver.solve(solver1State1);
            fase1Solver.getResultFromSolveState(solver1State1,fase1solution);
            proofFase1(kub1,fase1solution);
            int length=lengthHods(fase1solution);
            if(length>targetLength)return solution;
            fase2solution = new int[targetLength + 1 - length];
            if(finishSolution(kub1,solver1State1,fase2solution)){
                solution=new Solution(1,fase1solution,fase2solution);
                targetLength =solution.length-1;
            }

            if(solution!=null){
                tk++;
                if(tk>tryKol)return solution;
            }
            fase1Solver.solve(solver1State2);
            fase1Solver.getResultFromSolveState(solver1State2,fase1solution);
            length=lengthHods(fase1solution);
            if(length>targetLength)return solution;
            fase2solution = new int[targetLength + 1 - length];
            if(finishSolution(kub2,solver1State2,fase2solution)){
                solution=new Solution(2,fase1solution,fase2solution);
                targetLength =solution.length-1;
            }

            if(solution!=null){
                tk++;
                if(tk>tryKol)return solution;
            }
            fase1Solver.solve(solver1State3);tk++;
            fase1Solver.getResultFromSolveState(solver1State3,fase1solution);
            length=lengthHods(fase1solution);
            if(length>targetLength)return solution;
            fase2solution = new int[targetLength + 1 - length];
            if(finishSolution(kub3,solver1State3,fase2solution)){
                solution=new Solution(3,fase1solution,fase2solution);
                targetLength =solution.length-1;
            }
        }
    }

    private boolean finishSolution(Kub kub,Solver1State solver1State,int[] fase2){
        int[] fase1solution=new int[MAX_DEEP+1];
        fase1Solver.getResultFromSolveState(solver1State,fase1solution);
        Kub kubFase2=new Kub(kub);
        for(int i:fase1solution)kubFase2.povorot(i);

        int[][][] grani = kubFase2.getGrani();
        int[] k2 = {
                CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani)),
                CubieKoordinateConverter.rpToY2(GraniCubieConverter.graniToRP(grani)),
                CubieKoordinateConverter.rpToZ2(GraniCubieConverter.graniToRP(grani))
        };
        if (!fase2Solver.solve(k2[0], k2[1], k2[2], fase2))return false;
        proofFase2(kubFase2, fase2);
        for (int i = 0; i < fase2.length; i++) fase2[i] = hodsFase2[fase2[i]];
        return true;
    }

    private Solver1State initFase1(Kub kub){
        int[][][] grani= kub.getGrani();
        return fase1Solver.initSolveState(CubieKoordinateConverter.uoToX1(GraniCubieConverter.graniToUO(grani)),
                CubieKoordinateConverter.roToY1(GraniCubieConverter.graniToRO(grani)),
                CubieKoordinateConverter.rpToZ1(GraniCubieConverter.graniToRP(grani)));
    }

    private static int lengthHods(int[] hods){
        int len = 0;
        for (int h : hods) if (h != 0) len++;
        return len;
    }

    private static Kub getSymmetryKub(Kub kub,int sym){
        try {
            switch (sym) {
                case 1: {
                    kub=new Kub(kub);
                    break;
                }
                case 2: {
                    kub = new Kub(normalizeColors(Symmetry.symZ(kub.getGrani())));
                    break;
                }
                case 3: {
                    kub = new Kub(normalizeColors(Symmetry.symX(kub.getGrani())));
                    break;
                }
                default:
                    throw new IllegalArgumentException("sym=" + sym + ", but 1<=sym<=3");
            }
        }
        catch (Kub.InvalidPositionException e){
            throw new RuntimeException(e);
        }
        return kub;
    }

    private static void proofFase1(Kub kub, int[] fase1solution){
        kub=new Kub(kub);
        for(int p:fase1solution)kub.povorot(p);
        int[][][] grani = kub.getGrani();
        int []k1 = new int[]{
                CubieKoordinateConverter.uoToX1(GraniCubieConverter.graniToUO(grani)),
                CubieKoordinateConverter.roToY1(GraniCubieConverter.graniToRO(grani)),
                CubieKoordinateConverter.rpToZ1(GraniCubieConverter.graniToRP(grani))
        };
        if (k1[0] != 0 || k1[1] != 0 || k1[2] != 0) throw new RuntimeException("Kub 1 fase error");
    }

    private static void proofFase2(Kub kub,int[] fase2solution){
        kub=new Kub(kub);
        for(int p:fase2solution)kub.povorot(hodsFase2[p]);
        int[][][] grani = kub.getGrani();
        int[]k2 = new int[]{
                CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani)),
                CubieKoordinateConverter.rpToY2(GraniCubieConverter.graniToRP(grani)),
                CubieKoordinateConverter.rpToZ2(GraniCubieConverter.graniToRP(grani))
        };
        if(k2[0]!=0||k2[1]!=0||k2[2]!=0)throw new RuntimeException("Kub 2 fase error");
    }

    public static int[][][] normalizeColors(int[][][] graniIn){
        int[][][] grani=new int[6][3][3];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int c=0;c<6;c++){
                        if(graniIn[i][j][k]==graniIn[c][1][1]){
                            grani[i][j][k]=c;
                            break;
                        }
                    }
                }
            }
        }
        return grani;
    }

}
