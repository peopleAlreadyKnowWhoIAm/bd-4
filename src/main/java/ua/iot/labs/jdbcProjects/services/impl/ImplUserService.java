package ua.iot.labs.jdbcProjects.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ua.iot.labs.jdbcProjects.datasource.PlaylistDao;
import ua.iot.labs.jdbcProjects.datasource.UserDao;
import ua.iot.labs.jdbcProjects.domain.Playlist;
import ua.iot.labs.jdbcProjects.domain.User;
import ua.iot.labs.jdbcProjects.services.UserService;

@Service
@AllArgsConstructor
public class ImplUserService implements UserService {

    final UserDao users;
    final PlaylistDao playlists;

    @Override
    public List<User> findAllUsers() {
        return users.findAll();
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return users.findById(id);
    }

    @Override
    public List<User> findUsersByName(String name) {
        return users.findByName(name);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return users.findUserByEmail(email);
    }

    @Override
    public Integer create(User obj) {
        return users.create(obj);
    }

    @Override
    public int update(User obj) {
        return users.update(obj);
    }

    @Override
    public int deleteUser(Integer id) {
        return users.delete(id);
    }

    @Override
    public List<Playlist> findAllPlaylists() {
        return playlists.findAll();
    }

    @Override
    public Optional<Playlist> findPlaylistsById(Integer id) {
        return playlists.findById(id);
    }

    @Override
    public List<Playlist> findPlaylistsByName(String name) {
        return playlists.findByName(name);
    }

    @Override
    public List<Playlist> findPlaylistByUser(Integer id) {
        return playlists.findByUserId(id);
    }

    @Override
    public Integer create(Playlist obj) {
        return playlists.create(obj);
    }

    @Override
    public int update(Playlist obj) {
        return playlists.update(obj);
    }

    @Override
    public int deletePlaylist(Integer id) {
        return playlists.delete(id);
    }
    
}
