package kub.kubSolver;

import java.util.concurrent.*;

public class ParallelSymmetrySolver{
    private static final ExecutorService executorService= Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread=new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    });
    public static Solution solve(final Kub kub,final KubSolver kubSolver, final long timeLimit){
        try {
            return solvePrivate(kub,kubSolver,timeLimit);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static Solution solvePrivate(final Kub kub,final KubSolver kubSolver, final long timeLimit) throws ExecutionException, InterruptedException {
        long timeStop=System.currentTimeMillis()+timeLimit;
        Future<Solution> f1=executorService.submit(new Nit(kub,kubSolver,1,timeStop));
        Future<Solution> f2=executorService.submit(new Nit(kub,kubSolver,2,timeStop));
        Future<Solution> f3=executorService.submit(new Nit(kub,kubSolver,3,timeStop));
        Solution s1=f1.get();
        Solution s2=f2.get();
        Solution s3=f3.get();
        if(s1.length>s2.length)s1=s2;
        if(s1.length>s3.length)s1=s3;
        return s1;
    }
    private static class Nit implements Callable<Solution> {
        private final Kub kub;
        private final KubSolver kubSolver;
        private final int sym;
        private final long stopTime;
        Nit(Kub kub,KubSolver kubSolver,int sym,long stopTime){
            this.kub=kub;
            this.kubSolver=kubSolver;
            this.sym = sym;
            this.stopTime=stopTime;
        }
        @Override
        public Solution call() throws Exception {
            Solution record=null;
            Solution solution=null;
            do {
                solution=kubSolver.solve(kub,solution,sym);
                if(record==null)record=solution;
                else if(record.length>=solution.length)record=solution;
            }while (System.currentTimeMillis()<stopTime);
            return record;
        }
    }
}
