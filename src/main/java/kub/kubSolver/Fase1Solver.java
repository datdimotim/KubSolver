package kub.kubSolver;

public interface Fase1Solver<KS>{
    int MAX_DEEP=30;
    void init(Tables<KS> tables);
    void solve(int x, int y, int z, int[] hods);
}
