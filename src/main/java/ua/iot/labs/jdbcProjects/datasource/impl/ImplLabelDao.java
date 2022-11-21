package ua.iot.labs.jdbcProjects.datasource.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ua.iot.labs.jdbcProjects.datasource.LabelDao;
import ua.iot.labs.jdbcProjects.domain.Label;

@Service
@RequiredArgsConstructor
public class ImplLabelDao implements LabelDao {

    static final String FIND_ALL = "SELECT * FROM label";
    static final String FIND_BY_ID = "SELECT * FROM label where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM label where name = ?";

    static final String CREATE = "INSERT INTO label(name) VALUES (?)";

    static final String UPDATE = "UPDATE label SET name=? where id = ?";

    static final String DELETE = "DELETE FROM label WHERE id=?";

    final JdbcTemplate jdbc;

    Label mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");

        return new Label(id, (String) data.get("name"));
    }

    @Override
    public List<Label> findAll() {
        List<Label> out;
        try {
            val outData = jdbc.queryForList(FIND_ALL);
            out = new ArrayList<>(outData.size());

            for (Map<String, Object> map : outData) {
                out.add(mapToObject(map));
            }

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            out = new ArrayList<>();
        }

        return out;
    }

    @Override
    public Optional<Label> findById(Integer id) {
        Optional<Label> out;
        try {
            val outData = jdbc.queryForMap(FIND_BY_ID, id);
            out = Optional.ofNullable(mapToObject(outData));
        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            out = Optional.empty();
        }
        return out;
    }

    @Override
    public List<Label> findByName(String name) {
        List<Label> out;
        try {
            val outData = jdbc.queryForList(FIND_BY_NAME, name);
            out = new ArrayList<>(outData.size());

            for (Map<String, Object> map : outData) {
                out.add(mapToObject(map));
            }

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            out = new ArrayList<>();
        }

        return out;
    }

    @Override
    public Integer create(Label obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, obj.getName());
                return ps;
            }, keyHolder);

            val bufId = keyHolder.getKey();
            if (bufId != null) {
                id = bufId.intValue();

            } else {
                val exc = new DataAccessException("Error!! Id from creating is null") {
                };
                throw exc;
            }

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    @Override
    public int update(Label obj) {
        int out = 0;
        try {
            out = jdbc.update(UPDATE, obj.getName(), obj.getId());
        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
        }
        return out;
    }

    @Override
    public int delete(Integer id) {
        int out = 0;
        try {
            out = jdbc.update(DELETE, id);
        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
        }
        return out;
    }

}
