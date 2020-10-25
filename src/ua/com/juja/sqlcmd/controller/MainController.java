package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.InMemoryDatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {

    private View view;
    private final DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }
    public void run () {
        connectToDB();
    }
    private void connectToDB() {
        view.write("Привет юзер!");
        view.write("Введи пожалуйста имя базы данных, имя пользователя и пароль в формате: database|username|password");
        while (true) {
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
