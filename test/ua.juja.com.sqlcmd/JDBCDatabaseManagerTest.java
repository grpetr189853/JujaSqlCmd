package ua.juja.com.sqlcmd;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest{

    @Override
    protected DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager();
    }
}
