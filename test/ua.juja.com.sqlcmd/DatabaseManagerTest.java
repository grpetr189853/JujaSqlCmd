package ua.juja.com.sqlcmd;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public abstract class DatabaseManagerTest {
    private DatabaseManager manager;
    @Before
    public void setUp() {

        manager = getDatabaseManager();
        manager.connect("sqlcmd", "postgres", "postgres");

    }

    protected abstract DatabaseManager getDatabaseManager();

    @Test
    public void testGetAllTablesNames() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[user, customers, candidates, test]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("user");
        //when
        DataSet input = new DataSet();
        input.put("name","Stiven");
        input.put("password","pass");
        input.put("id",13);
        manager.create("user", input);
        //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);
        DataSet user = users[0];

        assertEquals("[name, password, id]",Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass, 13]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("user");
        DataSet input = new DataSet();
        input.put("name","Stiven");
        input.put("password","pass");
        input.put("id",13);
        manager.create("user", input);
        //when
        DataSet newValue = new DataSet();

        newValue.put("password","pass2");
        manager.update("user",13,newValue);

        //then
        DataSet[] users = manager.getTableData("user");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[name, password, id]",Arrays.toString(user.getNames()));
        assertEquals("[Stiven, pass2, 13]", Arrays.toString(user.getValues()));
    }
}
