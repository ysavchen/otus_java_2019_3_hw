package com.mycompany.executor;

import java.sql.*;

//todo: analyze and put to JdbcTemplateImpl
public class DbUtils {

    private static final String URL = "jdbc:h2:mem:";
    private final Connection connection;

    public static void main(String[] args) throws SQLException {
        DbUtils demo = new DbUtils();
        demo.createTables();
        int id = 1;
        demo.insertRecord(id);
        demo.selectRecord(id);
        demo.close();
    }

    public DbUtils() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
        this.connection.setAutoCommit(false);
    }

    private void createTables() throws SQLException {
        PreparedStatement psUser = connection.prepareStatement(
                "create table User(" +
                        "id bigint(20) NOT NULL auto_increment, " +
                        "name varchar(255), " +
                        "age int(3))");
        PreparedStatement psAccount = connection.prepareStatement(
                "create table Account(" +
                        "no bigint(20) NOT NULL auto_increment, " +
                        "type varchar(255), " +
                        "rest number)");

        try (psUser; psAccount) {
            psUser.executeUpdate();
            psAccount.executeUpdate();
        }
    }

    private void insertRecord(int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            Savepoint savePoint = this.connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setString(2, "NameValue");
            try {
                int rowCount = pst.executeUpdate(); //Блокирующий вызов
                this.connection.commit();
                System.out.println("inserted rowCount:" + rowCount);
            } catch (SQLException ex) {
                this.connection.rollback(savePoint);
                System.out.println(ex.getMessage());
            }
        }
    }

    private void selectRecord(int id) throws SQLException {
        try (PreparedStatement pst = this.connection.prepareStatement("select name from test where id  = ?")) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                System.out.print("name:");
                if (rs.next()) {
                    System.out.println(rs.getString("name"));
                }
            }
        }
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
