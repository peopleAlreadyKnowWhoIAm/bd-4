package ua.iot.labs.jdbcProjects.datasource.impl;

import java.sql.PreparedStatement;
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
import ua.iot.labs.jdbcProjects.datasource.AuthorDao;
import ua.iot.labs.jdbcProjects.domain.Author;

@Service
@RequiredArgsConstructor
public class ImplAuthorDao implements AuthorDao {

    static final String FIND_ALL = "SELECT * FROM author";
    static final String FIND_BY_ID = "SELECT * FROM author where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM author where name = ?";

    static final String CREATE = "INSERT INTO author(name) VALUES (?)";

    static final String UPDATE = "UPDATE author SET name=? where id = ?";

    static final String DELETE = "DELETE FROM author WHERE id=?";
    
    final JdbcTemplate jdbc;


    Author mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");

        return new Author(id, (String) data.get("name"));
    }

    @Override
    public List<Author> findAll() {
        List<Author> out;
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
    public Optional<Author> findById(Integer id) {
        Optional<Author> out;
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
    public List<Author> findByName(String name) {
        List<Author> out;
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
    public Integer create(Author obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE);
                ps.setString(1, obj.getName());
                return ps;
            }, keyHolder);

            val bufId = keyHolder.getKey();
            if (bufId != null) {
                id = bufId.intValue();

            } else {
                val exc = new DataAccessException("Error!! Id from creating is null"){};
                throw exc;
            }

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    @Override
    public int update(Author obj) {
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
