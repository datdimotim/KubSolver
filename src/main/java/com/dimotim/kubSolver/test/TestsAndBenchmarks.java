package com.dimotim.kubSolver.test;

import com.dimotim.kubSolver.Kub;
import com.dimotim.kubSolver.Kub2x2;
import com.dimotim.kubSolver.KubSolver;
import com.dimotim.kubSolver.Solution;
import com.dimotim.kubSolver.kernel.*;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        //SymTables symTables=new SymTables();
        //symTables.proof();
        //SizeOf.sizeof(new SymTables());
        //solveAndView();
        //tablesTest();

        //speedSolve();
        //speedSolve2x2();

        //fase2Benchmark(new SymTables(),new SimpleSolver2());
        //while (true)computeTables();
        //fase1InfiniteBenchmark(new DoubleTables(),new SimpleSolver1());
        ///fase1Benchmark(new SymTables(),new SimpleSolver1());


        int[][] moves = {{18, 6, 9, 16, 3, 6, 1, 12, 17, 12, 6, 9, 6, 8, 2, 5, 11, 7, 4, 18, 13, 11, 16, 2}, {16, 9, 6, 1, 15, 1, 9, 6, 3, 9, 6, 16, 15, 2, 11, 16, 12, 9, 1, 8, 5, 17, 1}, {6, 2, 9, 3, 12, 9, 17, 6, 12, 9, 2, 9, 1, 5, 10, 3, 13, 11, 16, 13, 11, 15, 2}, {9, 18, 6, 2, 15, 12, 6, 18, 12, 9, 6, 2, 6, 2, 11, 17, 7, 2, 14, 1, 5, 16, 8, 4}, {16, 12, 16, 6, 12, 9, 1, 12, 6, 9, 2, 15, 18, 2, 14, 17, 11, 8, 16, 14, 5, 12, 8, 2}, {1, 6, 18, 15, 12, 16, 12, 1, 6, 12, 9, 16, 15, 8, 13, 7, 13, 1, 4, 11, 13, 10}, {2, 9, 15, 16, 15, 6, 17, 6, 16, 12, 17, 6, 5, 3, 10, 15, 9, 18, 2, 6, 11, 2}, {12, 6, 12, 3, 9, 2, 15, 18, 9, 15, 9, 15, 17, 5, 10, 17, 13, 9, 4, 10, 1, 12, 8}, {6, 9, 17, 9, 17, 12, 17, 6, 18, 2, 15, 3, 15, 9, 14, 8, 4, 16, 6, 3, 12, 13, 9, 1}, {9, 6, 17, 12, 9, 17, 15, 12, 6, 9, 16, 1, 15, 2, 5, 12, 16, 15, 18, 7, 1, 11, 5, 2}, {9, 1, 12, 2, 6, 17, 9, 6, 18, 12, 3, 12, 15, 3, 14, 12, 5, 3, 8, 2, 9, 5, 3}, {6, 12, 15, 6, 16, 12, 15, 1, 6, 2, 9, 2, 9, 1, 8, 18, 13, 11, 2, 15, 8, 18}, {6, 9, 17, 12, 6, 17, 12, 18, 9, 15, 2, 15, 1, 11, 5, 11, 1, 8, 15, 17, 13, 11, 5}, {15, 3, 6, 16, 12, 6, 17, 9, 6, 16, 15, 2, 9, 1, 8, 13, 12, 15, 17, 14, 3, 6, 12}, {1, 9, 15, 16, 9, 16, 6, 2, 9, 1, 6, 17, 15, 2, 11, 14, 2, 15, 3, 7, 3, 14, 18, 5}, {12, 2, 12, 2, 9, 15, 16, 9, 1, 15, 9, 6, 9, 2, 11, 13, 16, 10, 2, 8, 2, 11, 7, 1}, {18, 15, 3, 6, 16, 15, 2, 15, 3, 9, 15, 12, 11, 16, 5, 18, 8, 14, 6, 16, 14}, {9, 16, 9, 1, 12, 6, 2, 6, 1, 6, 1, 6, 9, 8, 4, 1, 10, 14, 11, 4, 17, 12, 1}, {6, 3, 15, 12, 2, 9, 3, 6, 2, 15, 9, 16, 6, 2, 14, 18, 8, 16, 15, 10, 5, 9, 17, 2}, {17, 15, 17, 15, 18, 12, 6, 12, 6, 17, 12, 3, 15, 3, 11, 13, 7, 1, 14, 11, 14, 7, 17, 2}, {2, 15, 1, 15, 18, 2, 15, 17, 6, 12, 1, 6, 2, 9, 5, 8, 15, 16, 8, 16, 15, 18, 1}, {1, 9, 6, 1, 9, 6, 3, 12, 15, 1, 9, 1, 11, 16, 14, 4, 1, 8, 16, 13, 4, 3}, {12, 9, 15, 16, 15, 12, 17, 9, 1, 6, 18, 6, 1, 8, 18, 2, 14, 18, 1, 5, 7, 1}, {1, 15, 3, 9, 17, 12, 15, 1, 12, 6, 12, 15, 9, 14, 7, 16, 11, 18, 15, 12, 5, 7, 2}, {3, 12, 6, 18, 12, 17, 15, 17, 6, 1, 15, 17, 15, 9, 8, 4, 3, 4, 12, 15, 5, 11, 8}, {17, 15, 12, 2, 12, 18, 12, 6, 1, 12, 6, 3, 15, 2, 6, 2, 11, 17, 8, 5, 3, 13, 1, 11, 8}, {6, 2, 9, 17, 9, 15, 2, 12, 15, 2, 15, 9, 17, 12, 5, 7, 4, 10, 14, 16, 6, 2, 4, 7}, {6, 16, 12, 6, 12, 15, 2, 12, 18, 9, 16, 9, 6, 2, 14, 5, 2, 15, 7, 2, 4, 18, 4, 3}, {6, 12, 6, 1, 15, 18, 9, 17, 15, 1, 12, 9, 2, 8, 5, 1, 12, 13, 17, 11, 15, 10, 4}, {12, 17, 15, 9, 15, 3, 12, 1, 12, 6, 1, 9, 15, 14, 18, 11, 13, 8, 16, 7, 14, 8}, {15, 1, 9, 2, 6, 17, 6, 12, 2, 9, 2, 9, 2, 11, 5, 10, 7, 18, 5, 16, 10, 5}, {12, 16, 6, 3, 9, 1, 12, 17, 9, 2, 6, 12, 6, 8, 15, 17, 11, 8, 13, 16, 1, 11, 4}, {12, 15, 17, 6, 1, 9, 3, 6, 17, 12, 15, 3, 6, 1, 11, 13, 7, 5, 11, 14, 12, 9, 3}, {9, 18, 9, 17, 9, 3, 6, 1, 6, 3, 12, 2, 9, 1, 12, 11, 5, 12, 3, 9, 2, 7, 17, 4}, {17, 6, 12, 18, 15, 17, 15, 6, 9, 3, 15, 2, 6, 1, 5, 8, 14, 17, 10, 1, 4, 12, 14, 16}, {12, 15, 16, 12, 17, 9, 1, 15, 1, 9, 16, 3, 6, 1, 9, 2, 14, 18, 7, 2, 4, 18, 2, 9, 1}, {9, 18, 6, 12, 17, 3, 12, 2, 12, 6, 17, 12, 6, 2, 6, 14, 12, 16, 10, 3, 7, 5, 3, 15, 1}, {2, 15, 9, 2, 12, 17, 9, 15, 9, 1, 6, 1, 9, 5, 10, 14, 9, 17, 13, 5, 2, 4, 3}, {12, 16, 12, 15, 16, 15, 9, 2, 9, 3, 15, 16, 12, 3, 11, 2, 5, 1, 4, 7, 5, 11, 9, 4}, {15, 6, 12, 6, 1, 12, 16, 12, 6, 16, 1, 9, 1, 6, 14, 2, 14, 7, 13, 10, 3}, {9, 17, 12, 18, 12, 6, 18, 6, 1, 15, 6, 2, 9, 14, 11, 4, 1, 9, 14, 17, 12, 4, 2}, {15, 9, 3, 9, 16, 12, 2, 15, 17, 3, 9, 2, 15, 11, 4, 1, 8, 13, 12, 2, 6, 10, 8}, {6, 9, 16, 9, 17, 12, 3, 15, 16, 15, 16, 15, 2, 6, 14, 10, 5, 8, 14, 7, 13, 16, 10, 1}, {6, 2, 9, 17, 15, 9, 3, 9, 6, 16, 15, 3, 8, 4, 12, 4, 18, 1, 11, 3, 12, 3}, {1, 6, 1, 15, 17, 12, 1, 6, 12, 2, 12, 1, 15, 6, 8, 18, 13, 10, 16, 10, 8, 3, 7, 2}, {12, 1, 9, 18, 9, 17, 9, 2, 12, 16, 15, 6, 12, 2, 9, 5, 7, 2, 12, 9, 14, 17, 14, 2}, {12, 2, 6, 16, 15, 1, 9, 3, 15, 2, 15, 2, 12, 18, 14, 16, 14, 18, 8, 1, 15, 5, 7, 18}, {3, 15, 12, 1, 12, 1, 9, 17, 9, 17, 12, 11, 5, 1, 7, 16, 11, 16, 5, 12, 16}, {6, 17, 9, 6, 2, 6, 9, 17, 15, 1, 6, 1, 14, 12, 6, 1, 8, 17, 8, 13, 2}, {6, 17, 12, 9, 6, 3, 9, 16, 15, 9, 16, 6, 9, 6, 2, 14, 11, 18, 9, 16, 4, 7, 13, 16, 1}, {2, 12, 16, 9, 18, 6, 3, 6, 17, 9, 2, 6, 12, 1, 11, 8, 17, 14, 8, 15, 18, 13, 2}, {12, 17, 6, 16, 15, 16, 3, 6, 2, 9, 15, 12, 2, 8, 5, 18, 12, 1, 7, 17, 7, 3, 5}, {9, 15, 16, 12, 3, 15, 2, 15, 9, 1, 9, 6, 16, 12, 14, 18, 2, 10, 2, 4, 16, 11, 5, 2}, {1, 6, 12, 1, 15, 9, 1, 15, 16, 6, 3, 9, 15, 6, 2, 14, 12, 16, 10, 2, 7, 16, 7, 14, 5}, {18, 12, 9, 2, 6, 9, 1, 9, 18, 2, 15, 2, 6, 14, 3, 10, 4, 8, 17, 12, 13, 12, 8}, {1, 12, 6, 3, 15, 17, 15, 9, 3, 12, 1, 15, 12, 2, 14, 2, 7, 17, 10, 16, 2, 14, 12, 2}, {6, 9, 17, 12, 15, 16, 9, 2, 6, 16, 6, 1, 15, 2, 14, 10, 9, 2, 13, 5, 7, 16, 14, 10}, {3, 6, 2, 15, 9, 1, 15, 12, 1, 12, 6, 12, 17, 2, 14, 12, 2, 10, 7, 2, 14, 7, 3, 12}, {15, 1, 15, 17, 12, 1, 9, 18, 12, 15, 6, 2, 12, 6, 8, 4, 2, 14, 6, 11, 16, 12, 1, 4}, {12, 1, 15, 12, 6, 9, 1, 6, 9, 16, 6, 1, 12, 18, 8, 5, 17, 14, 1, 14, 9, 14, 11}, {12, 15, 17, 15, 1, 6, 2, 15, 17, 9, 17, 12, 2, 8, 14, 5, 11, 16, 14, 18, 2, 8, 2}, {6, 9, 18, 15, 9, 16, 6, 17, 15, 1, 15, 9, 15, 9, 14, 16, 2, 4, 7, 13, 11, 14, 3}, {18, 15, 2, 6, 16, 9, 15, 1, 12, 9, 18, 11, 4, 17, 7, 18, 12, 6, 11, 13, 2}, {1, 9, 17, 9, 1, 15, 12, 9, 16, 9, 1, 12, 6, 3, 14, 11, 18, 11, 18, 7, 2, 13}, {9, 3, 6, 12, 17, 12, 3, 9, 15, 17, 15, 12, 3, 6, 8, 15, 7, 1, 15, 8, 5, 8, 14, 2}, {16, 15, 9, 15, 9, 1, 12, 2, 6, 2, 12, 3, 9, 2, 6, 14, 8, 2, 11, 5, 16, 8, 18, 15, 9}, {18, 12, 1, 15, 16, 9, 6, 9, 6, 16, 15, 12, 16, 12, 5, 12, 7, 5, 18, 12, 15, 16, 13}, {2, 15, 2, 12, 2, 12, 18, 15, 16, 15, 3, 12, 6, 9, 8, 5, 10, 8, 15, 8, 16, 5, 12, 3}, {3, 9, 17, 15, 2, 9, 1, 6, 9, 1, 12, 16, 12, 3, 8, 4, 8, 16, 5, 3, 7, 2}, {18, 12, 17, 6, 2, 15, 9, 2, 12, 15, 2, 12, 6, 11, 16, 5, 7, 2, 4, 16, 14, 7}, {12, 1, 6, 16, 12, 2, 12, 15, 9, 1, 15, 1, 9, 18, 14, 17, 11, 2, 14, 17, 10, 18, 8, 5}, {16, 12, 15, 6, 1, 12, 16, 9, 1, 9, 2, 9, 1, 6, 2, 5, 8, 18, 13, 7, 17, 3, 11, 13, 2}, {12, 2, 9, 3, 6, 1, 9, 2, 9, 2, 9, 3, 12, 6, 2, 8, 17, 14, 2, 9, 17, 10, 7}, {15, 9, 3, 6, 16, 9, 6, 12, 6, 18, 12, 1, 12, 1, 5, 18, 9, 4, 11, 4, 9, 14}, {12, 1, 15, 16, 6, 9, 17, 6, 1, 9, 18, 15, 2, 6, 11, 6, 8, 3, 14, 9, 18, 3}, {15, 1, 15, 6, 9, 17, 9, 16, 6, 12, 1, 15, 9, 11, 6, 16, 10, 7, 2, 8, 4, 17}, {6, 9, 17, 12, 3, 6, 1, 12, 16, 9, 15, 6, 2, 12, 5, 11, 14, 17, 10, 4, 16, 7, 16, 2}, {2, 6, 3, 9, 15, 2, 9, 2, 15, 16, 12, 6, 1, 11, 14, 18, 13, 17, 4, 16, 13, 10}, {12, 3, 15, 6, 12, 2, 12, 17, 15, 17, 9, 18, 9, 6, 8, 16, 3, 5, 1, 13, 9, 2, 8, 2}, {18, 15, 18, 6, 1, 15, 2, 6, 17, 15, 17, 15, 12, 1, 11, 17, 7, 14, 1, 6, 18, 6, 7}, {6, 9, 16, 9, 2, 9, 6, 17, 9, 6, 2, 9, 1, 6, 5, 8, 14, 1, 7, 14, 3, 11, 14}, {16, 6, 3, 6, 16, 9, 6, 3, 15, 16, 15, 3, 5, 17, 15, 9, 13, 10, 6, 16, 10, 7, 2}, {15, 17, 6, 17, 12, 2, 12, 15, 18, 9, 15, 1, 6, 12, 11, 2, 12, 16, 13, 2, 7, 18}, {6, 1, 9, 18, 6, 17, 12, 15, 9, 2, 6, 18, 9, 14, 10, 7, 1, 13, 9, 15, 3, 8, 5}, {16, 6, 3, 9, 17, 9, 16, 12, 15, 12, 9, 16, 12, 15, 3, 11, 18, 8, 2, 11, 5, 7, 6, 17, 12}, {16, 9, 17, 9, 15, 12, 15, 2, 6, 1, 15, 12, 9, 2, 11, 13, 4, 16, 5, 3, 14, 9, 2, 6}, {1, 9, 1, 15, 3, 12, 6, 17, 12, 16, 6, 1, 5, 3, 7, 14, 18, 6, 10, 2, 13}, {6, 18, 15, 6, 17, 6, 9, 17, 15, 9, 16, 1, 12, 11, 16, 5, 8, 2, 12, 14, 9, 14, 6}, {9, 6, 3, 9, 6, 9, 16, 12, 3, 9, 17, 6, 2, 11, 16, 5, 3, 15, 2, 10, 14, 8, 3}, {3, 15, 6, 1, 9, 16, 6, 16, 6, 18, 15, 17, 9, 2, 6, 1, 11, 14, 7, 15, 17, 14, 5, 9, 16, 5}, {6, 16, 15, 3, 15, 9, 2, 9, 6, 12, 6, 1, 12, 6, 8, 17, 15, 1, 8, 17, 5, 1, 14, 5}, {15, 3, 15, 12, 6, 2, 15, 17, 12, 15, 1, 12, 1, 14, 17, 7, 13, 8, 1, 14, 1, 4, 11}, {6, 9, 15, 3, 6, 1, 12, 2, 12, 6, 9, 3, 6, 11, 16, 10, 7, 4, 17, 14, 9, 15, 3}, {1, 9, 6, 2, 6, 9, 6, 12, 18, 12, 17, 15, 6, 8, 4, 1, 8, 6, 11, 15, 18, 3, 6}, {15, 12, 15, 9, 6, 2, 6, 9, 16, 1, 15, 1, 6, 3, 11, 8, 2, 12, 14, 16, 2, 13, 9, 5}, {3, 6, 12, 16, 15, 1, 15, 9, 16, 15, 3, 6, 8, 17, 13, 12, 4, 2, 14, 1, 14, 8}, {15, 16, 6, 3, 15, 12, 16, 15, 16, 6, 17, 6, 1, 6, 9, 8, 15, 16, 10, 9, 13, 12, 14, 8, 5}, {15, 1, 12, 1, 15, 1, 15, 12, 6, 3, 12, 16, 1, 8, 16, 6, 11, 14, 2, 11, 6, 7}, {9, 18, 9, 6, 17, 6, 17, 3, 9, 16, 6, 2, 12, 8, 14, 17, 1, 15, 7, 13, 6, 2, 14}, {9, 1, 12, 15, 3, 6, 16, 9, 1, 9, 6, 1, 15, 12, 1, 14, 11, 9, 2, 10, 7, 5, 18, 3, 4}};

        KubSolver ks = new KubSolver<>(new SimpleTables(), new SimpleSolver1<>(), new SimpleSolver2<>());
        long startTime = System.currentTimeMillis();

        List<Solution> solutionList = Arrays.stream(moves)
                .map(ms -> {
                    Kub kub = new Kub(false);
                    Arrays.stream(ms).forEach(kub::povorot);
                    return ks.solve(kub);
                })
                .collect(Collectors.toList());

        solutionList.forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public static void speedSolve() throws IOException {
        new Benchmark(new Benchmark.Benchmarkable() {
            KubSolver kubSolver = new KubSolver<>(new SimpleTables(), new SimpleSolver1<>(), new SimpleSolver2<>());
            Kub kub = new Kub(false);
            float len = 0;
            int kol = 0;

            @Override
            public void getSolveAndAddNewTask() {
                kub.randomPos();
                //kub.povorot(18);
                kol++;
                len += kubSolver.solve(kub).getLength();
            }

            @Override
            public String resultToStringAndClear() {
                String ret = "" + (kol);
                len = 0;
                kol = 0;
                return ret;
            }
        }).runBenchmark(100000);
    }

    public static void speedSolve2x2() {
        new Benchmark(new Benchmark.Benchmarkable() {
            //KubSolver2x2 solver=new KubSolver2x2();
            //Complex2x2Tables solver=new Complex2x2Tables();
            FullSymTables2x2 solver = FullSymTables2x2.readTables();
            Kub2x2 kub = new Kub2x2(false);
            float len = 0;
            int kol = 0;

            @Override
            public void getSolveAndAddNewTask() {
                kub.randomPos();
                //kub.povorot(18);
                kol++;
                Solution solution = solver.solve(kub);
                len += solution.getLength();

                for (int p : solution.getHods()) kub.povorot(p);
                if (kub.getNumberPos().compareTo(BigDecimal.ZERO) != 0) throw new RuntimeException();
                //System.out.println(kub);
            }

            @Override
            public String resultToStringAndClear() {
                String ret = "" + (len / kol);
                len = 0;
                kol = 0;
                return ret;
            }
        }).runBenchmark(1000);
    }

    public static void fase1InfiniteBenchmark(Tables tables, final Fase1Solver solver) {
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random = new Random();
            private Object task = newTask();
            private int kol = 0;
            private int t = 0;

            @Override
            public void getSolveAndAddNewTask() {
                if (kol > 100) {
                    task = newTask();
                    kol = 0;
                }
                solver.solve(task);
                int[] hods = solver.getResultFromSolveState(task);
                t += hods[30];
                kol++;
            }

            @Override
            public String resultToStringAndClear() {
                task = newTask();
                return "" + t;
            }

            Object newTask() {
                return solver.initSolveState(random.nextInt(2187),
                        random.nextInt(2048),
                        random.nextInt(495));
            }
        }).runBenchmark(1000);
    }

    public static void fase1Benchmark(Tables tables, final Fase1Solver solver) {
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random = new Random();
            private int t = 0;

            @Override
            public void getSolveAndAddNewTask() {
                Object task = newTask();
                solver.solve(task);
                int[] hods = solver.getResultFromSolveState(task);
                t += hods[30];
            }

            @Override
            public String resultToStringAndClear() {
                return "" + t;
            }

            Object newTask() {
                return solver.initSolveState(random.nextInt(2187),
                        random.nextInt(2048),
                        random.nextInt(495));
            }
        }).runBenchmark(1000);
    }

    public static void fase2Benchmark(Tables tables, final Fase2Solver solver) {
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random = new Random();
            private int t = 0;
            private int kol = 0;
            private int solv;

            @Override
            public void getSolveAndAddNewTask() {
                int[] up = CubieKoordinateConverter.x2ToCubie(random.nextInt(40320));
                int[] rp = CubieKoordinateConverter.y2ToCubie(random.nextInt(40320));
                int[] r2 = CubieKoordinateConverter.z2ToCubie(random.nextInt(24));
                int ch1 = Combinations.chetNechetPerestanovka(up) ? 1 : 0;
                int ch2 = Combinations.chetNechetPerestanovka(rp) ? 1 : 0;
                int ch3 = Combinations.chetNechetPerestanovka(r2) ? 1 : 0;

                if ((ch1 + ch2 + ch3) % 2 != 0) {
                    int tmp = r2[11];
                    r2[11] = r2[10];
                    r2[10] = tmp;
                }
                int[] hods = new int[12];

                if (solver.solve(CubieKoordinateConverter.upToX2(up), CubieKoordinateConverter.rpToY2(rp), CubieKoordinateConverter.rpToZ2(r2), hods)) {
                    solv++;
                }
                kol++;
                t += hods[hods.length - 1];
            }

            @Override
            public String resultToStringAndClear() {
                String res = "solved percent=" + (solv * 100f) / kol + "  speed=" + solv + "  " + t;
                kol = 0;
                solv = 0;
                return res;
            }
        }).runBenchmark(1000);
    }

    public static void solveAndView() {
        Kub kub = new Kub(true);
        System.out.println(kub);

        KubSolver kubSolver = new KubSolver<>(new SymTables(), new SimpleSolver1<SymTables.KubState>(), new SimpleSolver2<SymTables.KubState>());
        Solution solution = kubSolver.solve(kub);
        System.out.println(solution);
        for (int p : solution.getHods()) kub.povorot(p);

        System.out.println(kub);
    }

    public static void computeAndTestSymTables() {
        long ts = System.currentTimeMillis();
        SymTables symTables = new SymTables();
        System.out.println("Completed... time=" + (System.currentTimeMillis() - ts) / 1000 + "s");
        symTables.proof();
        System.out.println("Completed... time=" + (System.currentTimeMillis() - ts) / 1000 + "s");
    }

    private static void tablesTest() {
        SymTables symTables = new SymTables();

        SymMoveTable x2Comb = symTables.x2Comb;
        SymMoveTable y = symTables.y2;
        SymDeepTable yX2Comb = symTables.yX2Comb;

        Random random = new Random();

        while (true) {
            {
                int s = 0;
                int count = 0;
                long startTime = System.currentTimeMillis();

                while (true) {
                    s += x2Comb.doMove(random.nextInt(x2Comb.CLASSES * 16), random.nextInt(11));
                    s += y.doMove(random.nextInt(y.CLASSES * 16), random.nextInt(11));
                    count++;
                    if (System.currentTimeMillis() - startTime > 1000) {
                        System.out.println("move count: " + count / 100_000 + " dummy: " + s);
                        break;
                    }
                }
            }

            {
                int s = 0;
                int count = 0;
                long startTime = System.currentTimeMillis();

                while (true) {
                    s += yX2Comb.getDepth(random.nextInt(y.CLASSES * 16), random.nextInt(x2Comb.CLASSES * 16));
                    count++;
                    if (System.currentTimeMillis() - startTime > 1000) {
                        System.out.println("depth count: " + count / 100_000 + " dummy: " + s);
                        break;
                    }
                }
            }
        }

    }
}