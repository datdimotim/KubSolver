package kub.kubSolver;

public interface Fase2Solver<KS>{
    int MAX_DEEP=30;
    int[] hodsFase2= HodTransforms.p10To18;
    void init(Tables<KS> tables);
    void solve(int x, int y, int z, int[] hods);
}
