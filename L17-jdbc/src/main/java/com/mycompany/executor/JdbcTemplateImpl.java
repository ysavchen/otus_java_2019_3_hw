package com.mycompany.executor;

import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcTemplateImpl implements JdbcTemplate {

    private static final String URL = "jdbc:h2:mem:";

    private Connection connection;

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                connection.setAutoCommit(false);
                createTables(connection);
            } catch (SQLException ex) {
                System.out.println("Connection is not created: " + ex.toString());
                throw new ExceptionInInitializerError(ex);
            }
        }
        return connection;
    }

    private void createTables(Connection connection) throws SQLException {
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

    @Override
    public void create(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
        int numIds = 0;
        for (var field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                numIds++;
            }
        }

        if (numIds == 0) {
            throw new NoIdFoundException("Entity does not have a field with @Id - " + object);
        }
        if (numIds > 1) {
            throw new SeveralIdsFoundException("Entity have several fields with @Id - " + object);
        }

        Connection connection = getConnection();
        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        Consumer<PreparedStatement> paramsSetter = pst -> {
            for (int idx = 0; idx < fields.length; idx++) {
                try {
                    setParameter(pst, idx + 1, fields[idx].get(object));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        long userId = executor.insertRecord(
                "insert into " + table + "(" + columns + ")" +
                        " values (?, ?, ?)", paramsSetter);
        System.out.println("created user:" + userId);
        connection.commit();
        connection.close();
    }


    public void setParameter(PreparedStatement pst, int idx, Object object) throws SQLException {
        if (object == null) {
            return;
        }
        if (object.getClass() == String.class) {
            pst.setString(idx, String.valueOf(object));
        }
        if (object.getClass() == Integer.class || object.getClass() == int.class) {
            pst.setInt(idx, (Integer) object);
        }
        if (object.getClass() == Long.class || object.getClass() == long.class) {
            pst.setLong(idx, (Long) object);
        }
        if (object.getClass() == Double.class || object.getClass() == double.class) {
            pst.setDouble(idx, (Double) object);
        }
        if (object.getClass() == Float.class || object.getClass() == float.class) {
            pst.setFloat(idx, (Float) object);
        }
        if (object.getClass() == Byte.class || object.getClass() == byte.class) {
            pst.setByte(idx, (Byte) object);
        }
        if (object.getClass() == Short.class || object.getClass() == short.class) {
            pst.setShort(idx, (Short) object);
        }
        if (object.getClass() == Boolean.class || object.getClass() == boolean.class) {
            pst.setBoolean(idx, (Boolean) object);
        }
    }

    @Override
    public void update(Object objectData) {

    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
//        Optional<User> user = executor.selectRecord("select id, name from user where id  = ?", userId, resultSet -> {
//            try {
//                if (resultSet.next()) {
//                    return new User(resultSet.getLong("id"), resultSet.getString("name"));
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return null;
//        });
//        return null;
//        System.out.println(user);
        return null;
    }

}
