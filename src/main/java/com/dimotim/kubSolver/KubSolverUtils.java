package com.dimotim.kubSolver;

import java.util.function.BiFunction;
import java.util.function.Function;

public class KubSolverUtils {


    /**
     *
     *     id = f * (R * f')
     *  => f'^(-1) = f * R
     *
     *    id - нейтральная перестановка
     *    R - запутывание кубика
     *    f' - сборка узора в начальную позицию
     *    f'^(-1) - узора из начальной позиции
     *
     *
     *  from запутанный кубик
     *  to кубик - узор
     * @param solver сборщик кубика к начальную позицию
     * @param kubMover фукнция возвращающая кубик полученный путем применения заданной последовательности ходов
     *                 к начальной позиции
     * @return последовательность ходов для сборки кубика from в узор to
     */
    public static  <T> BiFunction<T,T,Solution> uzorSolver(Function<T,Solution> solver, Function<Solution, T> kubMover){
        return (from, to)->{
            Solution R=solver.apply(from).inverse();
            Solution fDash=solver.apply(to);
            Solution RfDash=fDash.compose(R);
            T k=kubMover.apply(RfDash);
            return solver.apply(k);
        };
    }
}
