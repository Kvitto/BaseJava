package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import com.urise.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = sqlHelper.execute(
                "SELECT * FROM resume r WHERE r.uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                });
        addContacts(resume);
        addSections(resume);
        return resume;
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() != 1) {
                    throw new NotExistStorageException(r.getUuid());
                }
            }
            deleteContact(conn, r);
            deleteSection(conn, r);
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> map = new LinkedHashMap<>();
        sqlHelper.execute(
                "SELECT * FROM resume r ORDER BY full_name, uuid", ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        map.put(rs.getString("uuid"), new Resume(rs.getString("uuid"), rs.getString("full_name")));
                    }
                    return null;
                });
        sqlHelper.execute(
                "SELECT * FROM contact", ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        map.get(rs.getString("resume_uuid"))
                                .addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }
                    return null;
                });
        map.values().forEach(this::addSections);
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        });
    }

    private void insertContact(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                switch (e.getKey().ordinal()) {
                    case 0, 1 -> ps.setString(3, ((TextSection) e.getValue()).getContent());
                    case 2, 3 -> ps.setString(3, listToString(((ListSection) e.getValue()).getItems()));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    private void deleteContact(Connection conn, Resume r) {
        sqlHelper.execute("DELETE FROM contact WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void deleteSection(Connection conn, Resume r) {
        sqlHelper.execute("DELETE FROM section WHERE resume_uuid=?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
    }

    private void addContacts(Resume r) {
        sqlHelper.execute(
                "SELECT * FROM contact c WHERE c.resume_uuid =?",
                ps -> {
                    String uuid = r.getUuid();
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    do {
                        addContact(rs, r);
                    } while (rs.next());
                    return null;
                });
    }

    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSections(Resume resume) {
        sqlHelper.execute(
                "SELECT * FROM section WHERE resume_uuid =?",
                ps -> {
                    ps.setString(1, resume.getUuid());
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        SectionType type = SectionType.valueOf(rs.getString("type"));
                        Section section = switch (type) {
                            case PERSONAL, OBJECTIVE -> new TextSection(rs.getString("value"));
                            case ACHIEVEMENT, QUALIFICATION ->
                                    new ListSection(List.of(rs.getString("value").split("\n")));
                            case EXPERIENCE, EDUCATION -> new CompanySection(new ArrayList<>());
                        };
                        resume.getSections().put(type, section);
                    }
                    return null;
                });
    }

}