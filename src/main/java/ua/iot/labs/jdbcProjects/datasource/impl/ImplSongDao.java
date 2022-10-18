package ua.iot.labs.jdbcProjects.datasource.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
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
import ua.iot.labs.jdbcProjects.datasource.SongDao;
import ua.iot.labs.jdbcProjects.domain.Song;

@Service
@RequiredArgsConstructor
public class ImplSongDao implements SongDao {

    static final String FIND_ALL = "SELECT * FROM song";
    static final String FIND_BY_ID = "SELECT * FROM song where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM song where name = ?";
    static final String FIND_BY_GENRE = "SELECT * FROM song where genre_id = ?";
    static final String FIND_AUTHORS = "SELECT author_id from song_has_author where song_id = ?";
    static final String FIND_COMMERCIAL = "SELECT * from song_commercial where song_id = ?";

    static final String CREATE = "INSERT INTO song(name, length, album_id, genre_id) VALUES (?, ?, ?,?)";
    static final String CREATE_AUTHORS = "INSERT INTO song_has_author(song_id, author_id) VALUES (?, ?)";
    static final String CREATE_COMMERCIAL = "INSERT INTO song_commercial(song_id, price, num_of_downloads) VALUES (?, ?, ?)";

    static final String UPDATE = "UPDATE song SET name=?, length=?, album_id=?, genre_id=? where id = ?";
    static final String UPDATE_COMMERCIAL = "UPDATE song_commercial SET price=?, num_of_downloads=? where song_id = ?";
    static final String DELETE_AUTHORS = "DELETE from song_has_author where song_id = ?";

    static final String DELETE = "DELETE FROM song WHERE id=?";

    final JdbcTemplate jdbc;

    List<Integer> getAllAuthorsById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_AUTHORS, id).stream().map((val) -> (Integer) val.get("author_id")).toList();
        return out;
    }

    Song mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");
        val authors = getAllAuthorsById(id);
        Map<String, Object> commercial;
        System.out.println(id);
        commercial = jdbc.queryForMap(FIND_COMMERCIAL, id);

        return new Song(id, (String) data.get("name"), (Integer) data.get("length"), authors,
                Optional.ofNullable((Integer) data.get("album_id")), (Integer) data.get("genre_id"),
                (BigDecimal) commercial.get("price"),
                (Integer) commercial.get("num_of_downloads"));
    }

    @Override
    public List<Song> findAll() {
        List<Song> out;
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
    public Optional<Song> findById(Integer id) {
        Optional<Song> out;
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
    public List<Song> findByName(String name) {
        List<Song> out;
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
    public Integer create(Song obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {

                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, obj.getName());
                ps.setInt(2, obj.getLength());
                if (obj.getAlbum().isPresent()) {
                    ps.setInt(3, obj.getAlbum().get());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setInt(4, obj.getGenre());
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
            InsertAuthors(id, obj.getAuthors());
            jdbc.update(CREATE_COMMERCIAL, id, obj.getPrice(), obj.getNumberOfDownloads());

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
    public int update(Song obj) {
        int out = 0;
        try {
            jdbc.update((conection) -> {
                PreparedStatement ps = conection.prepareStatement(UPDATE);
                ps.setString(1, obj.getName());
                ps.setInt(2, obj.getLength());
                if (obj.getAlbum().isPresent()) {
                    ps.setInt(3, obj.getAlbum().get());
                } else {
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setInt(4, obj.getGenre());
                ps.setInt(5, obj.getId());
                return ps;
            });
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

    @Override
    public List<Song> findSongsByGenreId(Integer id) {
        List<Song> out;
        try {
            val outData = jdbc.queryForList(FIND_BY_GENRE, id);
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

}
