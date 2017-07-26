package com.dimotim.kubSolver.test;

import com.dimotim.kubSolver.*;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.SymTables;

import java.io.IOException;
import java.util.Random;

import static com.dimotim.kubSolver.KubSolver.Solution;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        solveAndView();
        speedSolve();
        //while (true)computeTables();
        //fase1InfiniteBenchmark(new DoubleTables(),new SimpleSolver1());
        ///fase1Benchmark(new SymTables(),new SimpleSolver1());

    }

    public static void speedSolve() throws IOException {
        new Benchmark(new Benchmark.Benchmarkable() {
            KubSolver kubSolver=new KubSolver<>(new SymTables(),new SimpleSolver1<>(),new SimpleSolver2<>());
            Kub kub = new Kub(false);
            float len=0;
            int kol=0;
            @Override
            public void getSolveAndAddNewTask() {
                kub.randomPos();
                //kub.povorot(18);
                kol++;
                len+=kubSolver.solve(kub).length;
            }

            @Override
            public String resultToStringAndClear() {
                String ret=""+(len/kol);
                len=0;
                kol=0;
                return ret;
            }
        }).runBenchmark(1000);
    }

    public static void fase1InfiniteBenchmark(Tables tables, Fase1Solver solver){
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random=new Random();
            private Object task=newTask();
            private int kol=0;
            private int t=0;
            private int[] hods=new int[31];
            @Override
            public void getSolveAndAddNewTask() {
                if(kol>100){
                    task=newTask();
                    kol=0;
                }
                solver.solve(task);
                solver.getResultFromSolveState(task,hods);
                t+=hods[30];
                kol++;
            }

            @Override
            public String resultToStringAndClear() {
                task=newTask();
                return ""+t;
            }
            Object newTask(){
                return solver.initSolveState(random.nextInt(2187),
                                             random.nextInt(2048),
                                             random.nextInt(495));
            }
        }).runBenchmark(1000);
    }

    public static void fase1Benchmark(Tables tables,Fase1Solver solver){
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random=new Random();
            private int t=0;
            private int[] hods=new int[31];
            @Override
            public void getSolveAndAddNewTask() {
                Object task=newTask();
                solver.solve(task);
                solver.getResultFromSolveState(task,hods);
                t+=hods[30];
            }

            @Override
            public String resultToStringAndClear() {
                return ""+t;
            }
            Object newTask(){
                return solver.initSolveState(random.nextInt(2187),
                        random.nextInt(2048),
                        random.nextInt(495));
            }
        }).runBenchmark(1000);
    }

    public static void solveAndView(){
        Kub kub=new Kub(true);
        System.out.println(kub);

        KubSolver kubSolver=new KubSolver<>(new SymTables(),new SimpleSolver1<>(),new SimpleSolver2<>());
        Solution solution=kubSolver.solve(kub);
        System.out.println(solution);
        for(int p:solution.getHods())kub.povorot(p);

        System.out.println(kub);
    }

    public static void computeAndTestSymTables(){
        long ts=System.currentTimeMillis();
        SymTables symTables =new SymTables();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
        symTables.proof();
        System.out.println("Completed... time="+(System.currentTimeMillis()-ts)/1000+"s");
    }
}

