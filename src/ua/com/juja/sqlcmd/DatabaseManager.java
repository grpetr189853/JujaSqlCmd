package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class DatabaseManager {
    private Connection connection;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String database = "sqlcmd";
        String user = "postgres";
        String password = "postgres";

        DatabaseManager manager = new DatabaseManager();

        manager.connect(database, user, password);

        Connection connection = manager.getConnection();
        //insert statement
//        String sql = "INSERT INTO public.user (name,password)" +
//                "VALUES ('Stiven','Pupkin')";
//
//        update(connection, sql);
//        Statement stmt;

        //delete
        manager.clear("user");
        //insert
        DataSet data = new DataSet();
        data.put("id",13);
        data.put("name","Stiven");
        data.put("password","pass");
        manager.create(data);
        //select statement
        String tableName = "user";

        DataSet[] result = manager.getTableData(tableName);

        System.out.println(Arrays.toString(result));
        //
        String sql1 = "SELECT  * FROM public." + tableName + " WHERE id > 5";
        select(connection, sql1);

        //tables
        String[] tables = manager.getTableNames();

        System.out.println(Arrays.toString(tables));
        //delete statement

//        String sql2 = "DELETE FROM public.user " +
//                "WHERE id > 10 AND id < 100";
//        update(connection, sql2);

        manager.clear("user");
        //update

    }

    public DataSet[] getTableData(String tableName) {
        try {
            int size = getSize(tableName);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public. " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();

            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    public String[] getTableNames() {
        try {
            String sql4 = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE' ";
            Statement stmt_tables = connection.createStatement();
            ResultSet rs = stmt_tables.executeQuery(sql4);
            String[] tables  = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("table_name");
            }
            tables = Arrays.copyOf(tables, index  , String[].class);
            rs.close();
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }

    }

    public void connect(String database, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please add JBDC jar to project");
            e.printStackTrace();
        }
        try {
            connection =  DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/" + database, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("Can't get connection for database: %s user: %s ", database, user));
            e.printStackTrace();
            connection =  null;
        }
    }

    private static void select(Connection connection, String sql1) throws SQLException {
        Statement stmt;
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);
        while (rs.next()){
            System.out.println("id:" + rs.getString("id"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
            System.out.println("------------");
        }
        rs.close();
        stmt.close();
    }

    private static void update(Connection connection, String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public void clear(String tableName) {
        try{
            Statement stmt  = connection.createStatement();
            stmt.executeUpdate("DELETE FROM public." + tableName);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(DataSet input) {
        try {
            Statement stmt = connection.createStatement();

            String values = "";


            String tableNames = getNamesFormatted(input, "%s,");

            values = getValuesFormatted(input, values, "'%s',");


            stmt.executeUpdate("INSERT INTO public.user (" + tableNames + ")" + "VALUES (" + values + ")");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String getValuesFormatted(DataSet input, String values, String format) {
        for(Object value  : input.getValues()) {
            values += String.format(format, value);
        }

        values  = values.substring(0, values.length() - 1);
        return values;
    }

    public void update(String tableName, int id, DataSet newValue) {
        try{
            String tableNames = getNamesFormatted(newValue, "%s = ?,");


            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE  public." + tableName + " SET " + tableNames + " WHERE id = ?"
            );

            int index = 1;
            for(Object value: newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private String getNamesFormatted(DataSet newValue, String format) {
        String string = "";


        for(String name  : newValue.getNames()) {
            string += String.format(format, name);
        }

        string  = string.substring(0, string.length() - 1);
        return string;
    }
}
