package ua.juja.com.sqlcmd;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.InMemoryDatabaseManager;

public class InMemoryDatabaseManagerTest extends DatabaseManagerTest{


    @Override
    protected DatabaseManager getDatabaseManager() {
        return new InMemoryDatabaseManager();
    }
}
