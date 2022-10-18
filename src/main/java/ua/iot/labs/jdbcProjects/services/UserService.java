package ua.iot.labs.jdbcProjects.services;

import java.util.List;
import java.util.Optional;

import ua.iot.labs.jdbcProjects.domain.Playlist;
import ua.iot.labs.jdbcProjects.domain.User;

public interface UserService {
// User ----------------
    List<User> findAllUsers();

    Optional<User> findUserById(Integer id);

    List<User> findUsersByName(String name);

    Optional<User> findUserByEmail(String email);

    Integer create(User obj);

    int update(User obj);

    int deleteUser(Integer id);


// Playlists ---------------
    List<Playlist> findAllPlaylists();

    Optional<Playlist> findPlaylistsById(Integer id);

    List<Playlist> findPlaylistsByName(String name);

    List<Playlist> findPlaylistByUser(Integer id);

    Integer create(Playlist obj);

    int update(Playlist obj);

    int deletePlaylist(Integer id);
}
