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
import ua.iot.labs.jdbcProjects.datasource.UserDao;
import ua.iot.labs.jdbcProjects.domain.User;

@Service
@RequiredArgsConstructor
public class ImplUserDao implements UserDao {

    static final String FIND_ALL = "SELECT * FROM user";
    static final String FIND_BY_ID = "SELECT * FROM user where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM user where name = ?";
    static final String FIND_BY_EMAIL = "SELECT * FROM user where email = ?";
    static final String FIND_SONGS = "SELECT song_id from songs_saved_by_user where user_id = ?";
    static final String FIND_ALBUMS = "SELECT album_id from albums_saved_by_user where user_id = ?";
    static final String FIND_GENRES = "SELECT genre_id from user_prefer_genre where user_id = ?";
    static final String FIND_PLAYLISTS = "SELECT id from user_playlist_info where user_id = ?";

    static final String CREATE = "INSERT INTO user(name, email) VALUES (?, ?)";
    static final String CREATE_SONGS = "INSERT INTO songs_saved_by_user(user_id, song_id) VALUES (?, ?)";
    static final String CREATE_ALBUM = "INSERT INTO albums_saved_by_user(user_id, album_id) VALUES (?, ?)";
    static final String CREATE_GENRES = "INSERT INTO user_prefer_genre(user_id, genre_id) VALUES (?, ?)";

    static final String UPDATE = "UPDATE user SET name=?, email=? where id = ?";
    static final String DELETE_SONGS = "DELETE from songs_saved_by_user where user_id = ?";
    static final String DELETE_ALBUMS = "DELETE from albums_saved_by_user where user_id = ?";
    static final String DELETE_GENRES = "DELETE from user_prefer_genre where user_id = ?";

    static final String DELETE = "DELETE FROM user WHERE id=?";

    final JdbcTemplate jdbc;

    List<Integer> getAllSongsById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_SONGS, Integer.class, id);
        return out;
    }

    List<Integer> getAllAlbumsById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_ALBUMS, Integer.class, id);
        return out;
    }

    List<Integer> getAllGenresById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_GENRES,Integer.class, id);
        return out;
    }

    List<Integer> getAllPlaylistsById(Integer id) throws DataAccessException {
        List<Integer> out;
        out = jdbc.queryForList(FIND_PLAYLISTS,Integer.class, id);
        return out;
    }

    User mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");
        val songs = getAllSongsById(id);
        val albums = getAllAlbumsById(id);
        val genres = getAllGenresById(id);
        val playlists = getAllPlaylistsById(id);

        return new User(id, (String) data.get("name"), (String) data.get("email"), genres, playlists, albums, songs);
    }

    @Override
    public List<User> findAll() {
        List<User> out;
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
    public Optional<User> findById(Integer id) {
        Optional<User> out;
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
    public List<User> findByName(String name) {
        List<User> out;
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
    public Integer create(User obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, obj.getName());
                ps.setString(2, obj.getEmail());
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

            InsertAlbums(id, obj.getAlbums());
            InsertGenres(id, obj.getPreferedGenres());
            InsertSongs(id, obj.getSongs());

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    void InsertGenres(Integer id, List<Integer> genres) throws DataAccessException {
        for (Integer integer : genres) {

            jdbc.update(CREATE_GENRES, id, integer);
        }
    }

    void InsertAlbums(Integer id, List<Integer> albums) throws DataAccessException {
        for (Integer integer : albums) {

            jdbc.update(CREATE_ALBUM, id, integer);
        }
    }

    void InsertSongs(Integer id, List<Integer> songs) throws DataAccessException {
        for (Integer integer : songs) {

            jdbc.update(CREATE_SONGS, id, integer);
        }
    }

    @Override
    public int update(User obj) {
        int out = 0;
        try {
            out = jdbc.update(UPDATE, obj.getName(), obj.getEmail(), obj.getId());
            jdbc.update(DELETE_ALBUMS, obj.getId());
            jdbc.update(DELETE_SONGS, obj.getId());
            jdbc.update(DELETE_GENRES, obj.getId());
            InsertAlbums(obj.getId(), obj.getAlbums());
            InsertGenres(obj.getId(), obj.getPreferedGenres());
            InsertSongs(obj.getId(), obj.getSongs());
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
    public Optional<User> findUserByEmail(String email) {
        Optional<User> out;
        try {
            val outData = jdbc.queryForMap(FIND_BY_EMAIL, email);
            out = Optional.ofNullable(mapToObject(outData));

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            out = Optional.empty();
        }

        return out;
    }

}
