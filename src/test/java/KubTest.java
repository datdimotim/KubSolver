import com.dimotim.kubSolver.Kub;
import com.dimotim.kubSolver.KubSolver;
import com.dimotim.kubSolver.Solution;
import com.dimotim.kubSolver.solvers.SimpleSolver1;
import com.dimotim.kubSolver.solvers.SimpleSolver2;
import com.dimotim.kubSolver.tables.DoubleTables;
import com.dimotim.kubSolver.tables.SimpleTables;
import com.dimotim.kubSolver.tables.SymTables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class KubTest {
    @Test
    public void SymTablesGenerateTest(){
        SymTables symTables=new SymTables();
    }

    @Test
    public void solveSolvedKub(){
        Kub kub=new Kub(false);
        Solution solution=kub.solve();
        checkSolution(kub,solution);
    }

    @Test
    public void solveRandomKub(){
        Kub kub=new Kub(true);
        Solution solution=kub.solve();
        checkSolution(kub,solution);
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

    public void checkSolution(Kub kub, Solution solution){
        for(int hod:solution.getHods()){
            kub.povorot(hod);
        }
        Assertions.assertEquals(kub.getNumberPos(),BigDecimal.ZERO);
    }

    @Test
    public void checkSolutionInverse(){
        Kub kub=new Kub(true);
        Solution solution=kub.solve();
        Kub duplicate=new Kub(false).apply(solution.inverse());

        Assertions.assertEquals(kub,duplicate);
    }

    @Test
    public void checkSolveUzor(){
        Kub uzor=new Kub(true);
        Kub random=new Kub(true);
        Solution uzorSolution=random.solve(uzor);

        Assertions.assertEquals(uzor,random.apply(uzorSolution));
    }
}
