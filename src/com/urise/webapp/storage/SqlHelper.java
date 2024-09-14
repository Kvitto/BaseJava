package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.sql.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPass) {
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public <T> T query(String sql, String[] values, SqlHelperQuery<T> query) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    ps.setString(i + 1, values[i]);
                }
            }
            return query.execute(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException(e.getMessage());
            }
            throw new StorageException(e.getMessage());
        } catch (IOException e) {
            throw new StorageException(e.getMessage());
        }
    }

    public interface SqlHelperQuery<T> {
        T execute(PreparedStatement ps) throws IOException, SQLException;
    }
}
