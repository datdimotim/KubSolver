import com.dimotim.kubSolver.Uzors;
import com.dimotim.kubSolver.tables.FullSymTables2x2;
import com.dimotim.kubSolver.tables.SymTables;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ResourcesTest {

    @SneakyThrows
    @Test
    public void testSymTablesResource(){
        SymTables.main(new String[0]);
        SymTables.readTables();
    }

    @SneakyThrows
    @Test
    public void testFull2x2TablesResource(){
        FullSymTables2x2.main(new String[0]);
        FullSymTables2x2.readTables();
    }

    @SneakyThrows
    @Test
    public void uzorResourceSetTest(){
        Assertions.assertTrue(Uzors.getInstance().getUzors().size()>0);
    }
}
