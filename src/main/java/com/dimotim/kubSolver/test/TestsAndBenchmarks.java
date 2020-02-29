package com.dimotim.kubSolver.test;

import com.dimotim.kubSolver.Kub;
import com.dimotim.kubSolver.Kub2x2;
import com.dimotim.kubSolver.KubSolver;
import com.dimotim.kubSolver.Solution;
import com.dimotim.kubSolver.kernel.*;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.FullSymTables2x2;
import com.dimotim.kubSolver.tables.SymTables;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

public class TestsAndBenchmarks {
    public static void main(String[] args) throws IOException {
        //SymTables symTables=new SymTables();
        //symTables.proof();
        //SizeOf.sizeof(new SymTables());
        //solveAndView();
        speedSolve();
        speedSolve2x2();
        //fase2Benchmark(new SymTables(),new SimpleSolver2());
        //while (true)computeTables();
        //fase1InfiniteBenchmark(new DoubleTables(),new SimpleSolver1());
        ///fase1Benchmark(new SymTables(),new SimpleSolver1());

    }

    public static void speedSolve() throws IOException {
        new Benchmark(new Benchmark.Benchmarkable() {
            KubSolver kubSolver= new KubSolver<>(SymTables.readTables(), new SimpleSolver1<SymTables.KubState>(), new SimpleSolver2<SymTables.KubState>());
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

    public static void speedSolve2x2(){
        new Benchmark(new Benchmark.Benchmarkable() {
            //KubSolver2x2 solver=new KubSolver2x2();
            //Complex2x2Tables solver=new Complex2x2Tables();
            FullSymTables2x2 solver=FullSymTables2x2.readTables();
            Kub2x2 kub = new Kub2x2(false);
            float len=0;
            int kol=0;
            @Override
            public void getSolveAndAddNewTask() {
                kub.randomPos();
                //kub.povorot(18);
                kol++;
                Solution solution=solver.solve(kub);
                len+=solution.length;

                for(int p:solution.getHods())kub.povorot(p);
                if(kub.getNumberPos().compareTo(BigDecimal.ZERO)!=0)throw new RuntimeException();
                //System.out.println(kub);
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

    public static void fase1InfiniteBenchmark(Tables tables, final Fase1Solver solver){
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

    public static void fase1Benchmark(Tables tables,final Fase1Solver solver){
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

    public static void fase2Benchmark(Tables tables,final Fase2Solver solver){
        solver.init(tables);
        new Benchmark(new Benchmark.Benchmarkable() {
            private final Random random=new Random();
            private int t=0;
            private int kol=0;
            private int solv;
            @Override
            public void getSolveAndAddNewTask() {
                int[] up= CubieKoordinateConverter.x2ToCubie(random.nextInt(40320));
                int[] rp=CubieKoordinateConverter.y2ToCubie(random.nextInt(40320));
                int[] r2=CubieKoordinateConverter.z2ToCubie(random.nextInt(24));
                int ch1= Combinations.chetNechetPerestanovka(up)?1:0;
                int ch2=Combinations.chetNechetPerestanovka(rp)?1:0;
                int ch3=Combinations.chetNechetPerestanovka(r2)?1:0;

                if((ch1+ch2+ch3)%2!=0){
                    int tmp=r2[11];
                    r2[11]=r2[10];
                    r2[10]=tmp;
                }
                int[] hods=new int[12];

                if(solver.solve(CubieKoordinateConverter.upToX2(up),CubieKoordinateConverter.rpToY2(rp),CubieKoordinateConverter.rpToZ2(r2),hods)){
                 solv++;
                }
                kol++;
                t+=hods[hods.length-1];
            }

            @Override
            public String resultToStringAndClear() {
                String res="solved percent="+(solv*100f)/kol+"  speed="+solv+"  "+t;
                kol=0;
                solv=0;
                return res;
            }
        }).runBenchmark(1000);
    }

    public static void solveAndView(){
        Kub kub=new Kub(true);
        System.out.println(kub);

        KubSolver kubSolver=new KubSolver<>(new SymTables(),new SimpleSolver1<SymTables.KubState>(),new SimpleSolver2<SymTables.KubState>());
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