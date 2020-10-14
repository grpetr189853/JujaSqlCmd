package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        String database = "sqlcmd";
        String user = "postgres";
        String password = "postgres";

        Connection connection = getConnection(database, user, password);
        //insert statement
        String sql = "INSERT INTO public.user (name,password)" +
                "VALUES ('Stiven','Pupkin')";

        update(connection, sql);
        Statement stmt;
        //select statement
        String sql1 = "SELECT  * FROM public.user WHERE id > 5";
        select(connection, sql1);
        //tables
        String sql4 = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE' ";
        Statement stmt_tables = connection.createStatement();
        ResultSet rs = stmt_tables.executeQuery(sql4);
        String[] tables  = new String[100];
        int index = 0;
        while (rs.next()) {
            tables[index++] = rs.getString("table_name");
        }
        tables = Arrays.copyOf(tables, index + 1 , String[].class);
        rs.close();
        System.out.println(Arrays.toString(tables));
        //delete statement
        String sql2 = "DELETE FROM public.user " +
                "WHERE id > 10 AND id < 100";
        update(connection, sql2);
        //update
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE  public.user SET password = ? WHERE id > 3"
        );
        String pass = "password" + new Random().nextInt();
        ps.setString(1,pass);
        ps.executeUpdate();
        ps.close();
          connection.close();
    }

    private static Connection getConnection(String database, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/" + database, user, password);
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
}
