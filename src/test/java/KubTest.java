import com.dimotim.kubSolver.*;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.DoubleTables;
import com.dimotim.kubSolver.tables.FullSymTables2x2;
import com.dimotim.kubSolver.tables.SimpleTables;
import com.dimotim.kubSolver.tables.SymTables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.BiFunction;


public class KubTest {
    private final FullSymTables2x2 kub2x2Solver=new FullSymTables2x2();
    private final BiFunction<Kub2x2,Kub2x2,Solution> uzor2x2Solver=KubSolverUtils
            .uzorSolver(kub2x2Solver::solve,new Kub2x2(false)::apply);
    private final KubSolver<?,?> kubSolver=new KubSolver<>(new SymTables(),new SimpleSolver1<>(),new SimpleSolver2<>());
    private final BiFunction<Kub,Kub,Solution> uzorSolver=KubSolverUtils
            .uzorSolver(kubSolver::solve,new Kub(false)::apply);

    @Test
    public void solveKub2x2(){
        Kub2x2 kub2x2=new Kub2x2(true);
        Solution solution=kub2x2Solver.solve(kub2x2);
        Arrays.stream(solution.getHods())
                .forEach(kub2x2::povorot);
        Assertions.assertTrue(kub2x2.isSolved());
    }

    @Test
    public void solveSolvedKub(){
        Kub kub=new Kub(false);
        Solution solution=kubSolver.solve(kub);
        Assertions.assertTrue(kub.apply(solution).isSolved());
    }

    @Test
    public void solveRandomKub(){
        Kub kub=new Kub(true);
        Solution solution=kubSolver.solve(kub);
        Assertions.assertTrue(kub.apply(solution).isSolved());
    }

    @Test
    public void checkSameResults(){
        Kub kub=new Kub(true);
        Solution s1=new KubSolver<>(new SimpleTables(),new SimpleSolver1<>(),new SimpleSolver2<>()).solve(kub);
        Solution s2=new KubSolver<>(new DoubleTables(),new SimpleSolver1<>(),new SimpleSolver2<>()).solve(kub);
        Solution s3=new KubSolver<>(new SymTables(),new SimpleSolver1<>(),new SimpleSolver2<>()).solve(kub);
        Assertions.assertArrayEquals(s1.getHods(),s2.getHods());
        Assertions.assertArrayEquals(s2.getHods(),s3.getHods());
    }

    @Test
    public void checkSolutionInverse(){
        Kub kub=new Kub(true);
        Solution solution=kubSolver.solve(kub);
        Kub duplicate=new Kub(false).apply(solution.inverse());

        Assertions.assertEquals(kub,duplicate);
    }

    @Test
    public void checkSolveUzor(){
        Kub uzor=new Kub(true);
        Kub random=new Kub(true);
        Solution uzorSolution=uzorSolver.apply(random,uzor);

        Assertions.assertEquals(uzor,random.apply(uzorSolution));
    }

    @Test
    public void checkSolveUzorSelf(){
        Kub random=new Kub(true);
        Solution uzorSolution=uzorSolver.apply(random,random);
        Assertions.assertEquals(random,random.apply(uzorSolution));
    }

    @Test
    public void checkSolveUzor2x2(){
        Kub2x2 kub2x2=new Kub2x2(true);
        Kub2x2 uzor=new Kub2x2(true);
        Solution s=uzor2x2Solver.apply(kub2x2,uzor);
        Assertions.assertEquals(uzor,kub2x2.apply(s));
    }
}
