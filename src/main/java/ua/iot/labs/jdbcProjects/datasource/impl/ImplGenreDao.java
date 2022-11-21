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
import ua.iot.labs.jdbcProjects.datasource.GenreDao;
import ua.iot.labs.jdbcProjects.domain.Genre;

@Service
@RequiredArgsConstructor
public class ImplGenreDao implements GenreDao {

    static final String FIND_ALL = "SELECT * FROM genre";
    static final String FIND_BY_ID = "SELECT * FROM genre where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM genre where name = ?";
    static final String FIND_RELETED = "SELECT related_genre_id from related_genres where genre_id = ?";

    static final String CREATE = "INSERT INTO genre(name) VALUES (?)";
    static final String CREATE_RELETED = "INSERT INTO related_genres(genre_id, related_genre_id) VALUES(?, ?)";

    static final String UPDATE = "UPDATE genre SET name=? where id=?";
    static final String DELETE_RELETED = "DELETE from related_genres where genre_id = ?";

    static final String DELETE = "DELETE FROM genre WHERE id=?";

    final JdbcTemplate jdbc;

    List<Integer> getAllRelatedById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_RELETED,Integer.class, id);
        return out;
    }

    Genre mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");
        val related = getAllRelatedById(id);

        return new Genre(id, (String) data.get("name"), related);
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> out;
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
    public Optional<Genre> findById(Integer id) {
        Optional<Genre> out;
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
    public List<Genre> findByName(String name) {
        List<Genre> out;
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
    public Integer create(Genre obj) {
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

            InsertRelated(id, obj.getRelatedGenresId());

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    void InsertRelated(Integer id, List<Integer> authors) throws DataAccessException {
        for (Integer integer : authors) {

            jdbc.update(CREATE_RELETED, id, integer);
        }
    }

    @Override
    public int update(Genre obj) {
        int out = 0;
        try {
            out = jdbc.update(UPDATE, obj.getName(), obj.getId());
            jdbc.update(DELETE_RELETED, obj.getId());
            InsertRelated(obj.getId(), obj.getRelatedGenresId());
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
