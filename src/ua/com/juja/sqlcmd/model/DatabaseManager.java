package ua.com.juja.sqlcmd.model;

public interface DatabaseManager {

    DataSet[] getTableData(String tableName);

    String[] getTableNames();

    void connect(String database, String userName, String password);

    void clear(String tableName);

    void create(String tableName, DataSet input);

    void update(String tableName, int id, DataSet newValue);
}
