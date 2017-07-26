package com.dimotim.kubSolver;

public interface Fase2Solver<KS>{
    void init(Tables<KS> tables);
    boolean solve(int x, int y, int z, int[] hods); // разрешенное число ходов = hods.length-1
                                                    // возвращает true <==> решение найдено
}
