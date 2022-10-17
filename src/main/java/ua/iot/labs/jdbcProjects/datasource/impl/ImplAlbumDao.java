package ua.iot.labs.jdbcProjects.datasource.impl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import ua.iot.labs.jdbcProjects.datasource.AlbumDao;
import ua.iot.labs.jdbcProjects.domain.Album;

@Service
@RequiredArgsConstructor
public class ImplAlbumDao implements AlbumDao {

    static final String FIND_ALL = "SELECT * FROM album";
    static final String FIND_BY_ID = "SELECT * FROM album where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM album where name = ?";

    static final String CREATE = "INSERT INTO album(name, year_of_publishing,label_id) VALUES (?, ?, ?)";
    static final String CREATE_AUTHORS = "INSERT INTO album_has_author(album_id, author_id) VALUES (?, ?)";
    static final String CREATE_COMMERCIAL = "INSERT INTO album_commercial(album_id, price, num_of_downloads) VALUES (?, ?, ?)";

    static final String UPDATE = "UPDATE album SET name=?, year_of_publishing=?, label_id=? where id = ?";
    static final String UPDATE_COMMERCIAL = "UPDATE album_commercial SET price=?, num_of_downloads=? where album_id = ?";
    static final String DELETE_AUTHORS = "DELETE from album_has_authors where album_id = ?";

    static final String DELETE = "DELETE FROM album WHERE id=?";
    static final String FIND_AUTHORS = "SELECT author_id from album_has_author where album_id = ?";
    static final String FIND_COMMERCIAL = "SELECT * from album_commecial where album_id = ?";

    
    final JdbcTemplate jdbc;

    List<Integer> getAllAuthorsById(Integer id) {
        List<Integer> out;
        try {
            out = jdbc.query(FIND_AUTHORS, BeanPropertyRowMapper.newInstance(Integer.class), id);
        } catch (Exception e) {
            out = new ArrayList<>();
        }
        return out;
    }

    Album mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");
        val authors = getAllAuthorsById(id);
        Map<String, Object> commercial;
            commercial = jdbc.queryForMap(FIND_COMMERCIAL, id);


        return new Album(id, (String) data.get("name"), authors,  (Integer) data.get("year_of_publishing"),
                Optional.ofNullable((Integer) data.get("label_id")), (Double) commercial.get("price"),
                (Integer) commercial.get("num_of_downloads"));
    }

    @Override
    public List<Album> findAll() {
        List<Album> out;
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
    public Optional<Album> findById(Integer id) {
        Optional<Album> out;
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
    public List<Album> findByName(String name) {
        List<Album> out;
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
    public Integer create(Album obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE);
                ps.setString(1, obj.getName());
                ps.setInt(2, obj.getYearOfPublishing());
                ps.setInt(3, obj.getLabel().get());
                return ps;
            }, keyHolder);

            val bufId = keyHolder.getKey();
            if (bufId != null) {
                id = bufId.intValue();

            } else {
                val exc = new DataAccessException("Error!! Id from creating is null"){};
                throw exc;
            }

            InsertAuthors(id, obj.getAuthors());
            jdbc.queryForMap(CREATE_COMMERCIAL, id, obj.getPrice(), obj.getNumberOfDownloads());

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    void InsertAuthors(Integer id, List<Integer> authors) throws DataAccessException {
        for (Integer integer : authors) {

            jdbc.update(CREATE_AUTHORS, id, integer);
        }
    }

    @Override
    public int update(Album obj) {
        int out = 0;
        try {
            out = jdbc.update(UPDATE, obj.getName(), obj.getYearOfPublishing(), obj.getLabel().get(), obj.getId());
            jdbc.update(UPDATE_COMMERCIAL, obj.getPrice(), obj.getNumberOfDownloads(), obj.getId());
            jdbc.update(DELETE_AUTHORS, obj.getId());
            InsertAuthors(obj.getId(), obj.getAuthors());
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
