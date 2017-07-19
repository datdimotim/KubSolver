package kub.kubSolver;

import kub.kubSolver.solvers.SimpleSolver1;
import kub.kubSolver.solvers.SimpleSolver2;

import static kub.kubSolver.Fase1Solver.MAX_DEEP;

public class KubSolver<KS>{
    private Fase1Solver<KS> fase1Solver;
    private Fase2Solver<KS> fase2Solver;
    private static final int[] hodsFase2=HodTransforms.p10To18;
    public KubSolver(Tables<KS> tables){
        fase1Solver= new SimpleSolver1<>();
        fase2Solver= new SimpleSolver2<>();
        fase1Solver.init(tables);
        fase2Solver.init(tables);
    }
    public KubSolver(Tables<KS> tables,Fase1Solver<KS> fase1Solver,Fase2Solver<KS> fase2Solver){
        this.fase1Solver=fase1Solver;
        this.fase2Solver=fase2Solver;
        this.fase1Solver.init(tables);
        this.fase2Solver.init(tables);
    }

    public Solution solve(Kub kub,Solution preSolution,int sym){
        try {
            switch (sym) {
                case 1: {
                    kub=new Kub(kub);
                    break;
                }
                case 2: {
                    kub = new Kub(Symmetry.normalizeColors(Symmetry.symZ(kub.getGrani())));
                    break;
                }
                case 3: {
                    kub = new Kub(Symmetry.normalizeColors(Symmetry.symX(kub.getGrani())));
                    break;
                }
                default:
                    throw new IllegalArgumentException("sym=" + sym + ", but 1<=sym<=3");
            }
        }
        catch (Kub.InvalidPositionException e){
            throw new RuntimeException(e);
        }

        int[] fase1solution=null;
        if(preSolution!=null)fase1solution=fase1HodsAdapter(preSolution.getFase1());
        if(fase1solution!=null)incrementHods1(fase1solution);
        else fase1solution=new int[MAX_DEEP +1];
        {
            int[] facelet = KubGrani.graniToFacelet(kub.getGrani());
            int[] k1 = {
                    KubCubie.uoToX1(KubFacelet.faceletToUO(facelet)),
                    KubCubie.roToY1(KubFacelet.faceletToRO(facelet)),
                    KubCubie.rpToZ1(KubFacelet.faceletToRP(facelet))
            };
            fase1Solver.solve(k1[0], k1[1], k1[2], fase1solution);
            for (int p : fase1solution) kub.povorot(p);
            facelet = KubGrani.graniToFacelet(kub.getGrani());
            k1 = new int[]{
                    KubCubie.uoToX1(KubFacelet.faceletToUO(facelet)),
                    KubCubie.roToY1(KubFacelet.faceletToRO(facelet)),
                    KubCubie.rpToZ1(KubFacelet.faceletToRP(facelet))
            };
            if (k1[0] != 0 || k1[1] != 0 || k1[2] != 0) throw new RuntimeException("Kub 1 fase error");
        }
        int[] fase2solution=new int[MAX_DEEP +1];

        {
            int[] facelet = KubGrani.graniToFacelet(kub.getGrani());
            int[] k2 = {
                    KubCubie.upToX2(KubFacelet.faceletToUP(facelet)),
                    KubCubie.rpToY2(KubFacelet.faceletToRP(facelet)),
                    KubCubie.rpToZ2(KubFacelet.faceletToRP(facelet))
            };
            fase2Solver.solve(k2[0], k2[1], k2[2], fase2solution);
            for(int i=0;i<fase2solution.length;i++)fase2solution[i]=hodsFase2[fase2solution[i]];
            for (int p : fase2solution) kub.povorot(p);
            facelet = KubGrani.graniToFacelet(kub.getGrani());
            k2 = new int[]{
                    KubCubie.upToX2(KubFacelet.faceletToUP(facelet)),
                    KubCubie.rpToY2(KubFacelet.faceletToRP(facelet)),
                    KubCubie.rpToZ2(KubFacelet.faceletToRP(facelet))
            };
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
    public static class Solution {
        private static final int[][] symHods=HodTransforms.getSymHodsFor3Axis();
        private static final String[] hodString=HodTransforms.hodString;
        public final int length;
        private final int[] hods;
        private final int sym;
        private final int[] fase1;

        private Solution(int sym, int[] fase1, int[] fase2) {
            this.sym = sym;
            fase1=nomalize(fase1);
            fase2=nomalize(fase2);
            length =fase1.length+fase2.length;
            hods = new int[length];
            int kol=0;
            for(int hod:fase1)hods[kol++]=hod;
            for(int hod:fase2)hods[kol++]=hod;
            kol=0;
            for (int hod:hods) hods[kol++] = symHods[sym - 1][hod];
            this.fase1=fase1;
        }

        private static int[] nomalize(int [] hods){
            int s = 0;
            for (int hod : hods) if (hod != 0) s++;
            int[] res=new int[s];
            s=0;
            for (int hod : hods) if (hod != 0) res[s++]=hod;
            return res;
        }

        public int[] getHods() {
            return hods.clone();
        }

        private int[] getFase1(){
            return fase1;
        }

        public String toString() {
            StringBuilder out = new StringBuilder();
            for (int hod : hods) out.append(hodString[hod]);
            return out.toString() + length + "f symmerty="+sym;
        }
    }
}
