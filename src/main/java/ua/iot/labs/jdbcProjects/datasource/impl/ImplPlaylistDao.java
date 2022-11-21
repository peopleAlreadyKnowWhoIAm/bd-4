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
import ua.iot.labs.jdbcProjects.datasource.PlaylistDao;
import ua.iot.labs.jdbcProjects.domain.Playlist;

@Service
@RequiredArgsConstructor
public class ImplPlaylistDao implements PlaylistDao {

    static final String FIND_ALL = "SELECT * FROM user_playlist_info";
    static final String FIND_BY_ID = "SELECT * FROM user_playlist_info where id = ?";
    static final String FIND_BY_NAME = "SELECT * FROM user_playlist_info where name = ?";
    static final String FIND_BY_USER = "SELECT * FROM user_playlist_info where user_id = ?";
    static final String FIND_SONGS = "SELECT song_id from playlist_has_song where user_playlist_info_id = ?";

    static final String CREATE = "INSERT INTO user_playlist_info(name, user_id) VALUES (?, ?)";
    static final String CREATE_SONGS = "INSERT INTO playlist_has_song(user_playlist_info_id, song_id) VALUES (?, ?)";

    static final String UPDATE = "UPDATE user_playlist_info SET user_id=?, name=? where id = ?";
    static final String DELETE_SONGS = "DELETE from playlist_has_song where user_playlist_info_id = ?";

    static final String DELETE = "DELETE FROM user_playlist_info WHERE id=?";

    final JdbcTemplate jdbc;

    List<Integer> getAllSongsById(Integer id) throws DataAccessException {
        List<Integer> out;
        // out = jdbc.queryForList(FIND_SONGS, id).stream().map((val)->(Integer) val.get("song_id")).toList();
        out=jdbc.queryForList(FIND_SONGS,Integer.class, id);
        // System.out.println(out.toString());
        return out;
    }

    Playlist mapToObject(Map<String, Object> data) throws DataAccessException {
        Integer id = (Integer) data.get("id");
        val songs = getAllSongsById(id);

        return new Playlist(id, (String) data.get("name"), (Integer) data.get("user_id"), songs);
    }

    @Override
    public List<Playlist> findAll() {
        List<Playlist> out;
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
    public Optional<Playlist> findById(Integer id) {
        Optional<Playlist> out;
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
    public List<Playlist> findByName(String name) {
        List<Playlist> out;
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
    public Integer create(Playlist obj) {
        Integer id;
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbc.update((connection) -> {
                PreparedStatement ps = connection
                        .prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, obj.getName());
                ps.setInt(2, obj.getUserId());
                return ps;
            }, keyHolder);

            val bufId = keyHolder.getKey();
            if (bufId != null) {
                id = bufId.intValue();

            } else {
                val exc = new DataAccessException("Error!! Id from creating is null"){};
                throw exc;
            }

            InsertSongs(id, obj.getSongs());

        } catch (DataAccessException e) {
            System.out.println("Error in data access: " + e.getMessage());
            id = -1;
        }
        return id;
    }

    void InsertSongs(Integer id, List<Integer> songs) throws DataAccessException {
        for (Integer integer : songs) {

            jdbc.update(CREATE_SONGS, id, integer);
        }
    }

    @Override
    public int update(Playlist obj) {
        int out = 0;
        try {
            out = jdbc.update(UPDATE, obj.getUserId(), obj.getName(), obj.getId());
            jdbc.update(DELETE_SONGS, obj.getId());
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
    public List<Playlist> findByUserId(Integer userId) {
        List<Playlist> out;
        try {
            val outData = jdbc.queryForList(FIND_BY_USER, userId);
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
