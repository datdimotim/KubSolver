import com.dimotim.kubSolver.tables.FullSymTables2x2;
import com.dimotim.kubSolver.tables.SymTables;
import org.junit.jupiter.api.Test;

public class ResourcesTest {

    @Test
    public void testSymTablesResource(){
        SymTables.readTables();
    }

    @Test
    public void testFull2x2TablesResource(){
        FullSymTables2x2.readTables();
    }
}
