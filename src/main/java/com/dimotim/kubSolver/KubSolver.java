package com.dimotim.kubSolver;

import com.dimotim.kubSolver.kernel.*;
import lombok.val;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class KubSolver<KS,Solver1State>{
    private final int tryKol=0;
    private final int maxSolutionLength=23;
    private Fase1Solver<KS,Solver1State> fase1Solver;
    private Fase2Solver<KS> fase2Solver;
    private static final int[] hodsFase2=HodTransforms.getP10To18();
    public KubSolver(Tables<KS> tables,Fase1Solver<KS,Solver1State> fase1Solver,Fase2Solver<KS> fase2Solver){
        this.fase1Solver=fase1Solver;
        this.fase2Solver=fase2Solver;
        this.fase1Solver.init(tables);
        this.fase2Solver.init(tables);
    }

    public Iterator<Function<Integer, Optional<Solution>>> getSolutionSequence(Kub kub, int symmetry){
        return new Iterator<Function<Integer, Optional<Solution>>>() {
            private final Kub symKub=getSymmetryKub(kub,symmetry);
            private final Solver1State solver1State=initFase1(symKub);
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Function<Integer, Optional<Solution>> next() {
                fase1Solver.solve(solver1State);
                int[] phase1=fase1Solver.getResultFromSolveState(solver1State);
                int length=lengthHods(phase1);
                return totalLengthLimit-> {
                    if(totalLengthLimit + 1 - length<0)return Optional.empty();
                    int[] fase2solution = new int[totalLengthLimit + 1 - length];
                    if(finishSolution(symKub,solver1State,fase2solution)){
                        return Optional.of(Solution.fromFases(symmetry,phase1,fase2solution));
                    }
                    return Optional.empty();
                };
            }
        };
    }

    public Solution solve(Kub kub){
        val symmetrySolutionIterators= IntStream.rangeClosed(1,3)
                .mapToObj(s->getSolutionSequence(kub,s))
                .collect(Collectors.toList());

        int tk=0;
        int targetLength = maxSolutionLength;
        Solution solution=null;
        while (true) {
            for(val symmetrySolutionIterator:symmetrySolutionIterators) {
                if (solution != null) {
                    tk++;
                    if (tk > tryKol) return solution;
                }

                Optional<Solution> s = symmetrySolutionIterator.next().apply(targetLength);
                if (s.isPresent()) {
                    solution = s.get();
                    targetLength = s.get().getLength() - 1;
                }
            }
        }
    }

    private boolean finishSolution(Kub kub,Solver1State solver1State,int[] fase2){
        int[] fase1solution= fase1Solver.getResultFromSolveState(solver1State);
        Kub kubFase2=new Kub(kub);
        for(int i:fase1solution)kubFase2.povorot(i);

        int[][][] grani = kubFase2.getGrani();
        int[] k2 = {
                CubieKoordinateConverter.upToX2(GraniCubieConverter.graniToUP(grani)),
                CubieKoordinateConverter.rpToY2(GraniCubieConverter.graniToRP(grani)),
                CubieKoordinateConverter.rpToZ2(GraniCubieConverter.graniToRP(grani))
        };
        if (!fase2Solver.solve(k2[0], k2[1], k2[2], fase2))return false;
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
