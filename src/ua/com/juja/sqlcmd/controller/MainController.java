package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.InMemoryDatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCDatabaseManager();

        view.write("Привет юзер!");
        while (true) {
            view.write("Введи пожалуйста имя базы данных, имя пользователя и пароль в формате: database|username|password");
            String string = view.read();
            String[] data = string.split("\\|");
            String databseName = data[0];
            String userName = data[1];
            String password = data[2];
            try {
                manager.connect(databseName, userName, password);
                break;

            } catch (Exception e) {
                String message = e.getMessage();
                if(e.getCause() != null) {
                    message += "" + e.getCause().getMessage();
                }
                view.write("Неудача по причине: " + message);
                view.write("Повтори попытку");
            }

        }
        view.write("Успех!");
    }
}
