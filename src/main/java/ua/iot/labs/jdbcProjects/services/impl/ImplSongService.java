package ua.iot.labs.jdbcProjects.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ua.iot.labs.jdbcProjects.datasource.AlbumDao;
import ua.iot.labs.jdbcProjects.datasource.AuthorDao;
import ua.iot.labs.jdbcProjects.datasource.GenreDao;
import ua.iot.labs.jdbcProjects.datasource.LabelDao;
import ua.iot.labs.jdbcProjects.datasource.SongDao;
import ua.iot.labs.jdbcProjects.domain.Album;
import ua.iot.labs.jdbcProjects.domain.Author;
import ua.iot.labs.jdbcProjects.domain.Genre;
import ua.iot.labs.jdbcProjects.domain.Label;
import ua.iot.labs.jdbcProjects.domain.Song;
import ua.iot.labs.jdbcProjects.services.SongService;

@Service
@AllArgsConstructor
public class ImplSongService implements SongService {

    final SongDao songs;
    final LabelDao labels;
    final GenreDao genres;
    final AuthorDao authors;
    final AlbumDao albums;

    @Override
    public List<Song> findAllSongs() {
        return songs.findAll();
    }

    @Override
    public Optional<Song> findSongById(Integer id) {
        return songs.findById(id);
    }

    @Override
    public List<Song> findSongByName(String name) {
        return songs.findByName(name);
    }

    @Override
    public List<Song> findSongsByGenreId(Integer id) {
        return songs.findSongsByGenreId(id);
    }

    @Override
    public Integer create(Song obj) {
        return songs.create(obj);
    }

    @Override
    public int update(Song obj) {
        return songs.update(obj);
    }

    @Override
    public int deleteSongById(Integer id) {
        return songs.delete(id);
    }

    @Override
    public List<Label> findAllLabels() {
        return labels.findAll();
    }

    @Override
    public Optional<Label> findLabelById(Integer id) {
        return labels.findById(id);
    }

    @Override
    public List<Label> findLabelByName(String name) {
        return labels.findByName(name);
    }

    @Override
    public Integer create(Label obj) {
        return labels.create(obj);
    }

    @Override
    public int update(Label obj) {
        return labels.update(obj);
    }

    @Override
    public int deleteLabel(Integer id) {
        return labels.delete(id);
    }

    @Override
    public List<Genre> findAllGenres() {
        return genres.findAll();
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        return genres.findById(id);
    }

    @Override
    public List<Genre> findGenreByName(String name) {
        return genres.findByName(name);
    }

    @Override
    public Integer create(Genre obj) {
        return genres.create(obj);
    }

    @Override
    public int update(Genre obj) {
        return genres.update(obj);
    }

    @Override
    public int deleteGenre(Integer id) {
        return genres.delete(id);
    }

    @Override
    public List<Author> findAllAuthors() {
        return authors.findAll();
    }

    @Override
    public Optional<Author> findAuthorById(Integer id) {
        return authors.findById(id);
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        return authors.findByName(name);
    }

    @Override
    public Integer create(Author obj) {
        return authors.create(obj);
    }

    @Override
    public int update(Author obj) {
        return authors.update(obj);
    }

    @Override
    public int deleteAuthor(Integer id) {
        return authors.delete(id);
    }

    @Override
    public List<Album> findAllAlbums() {
        return albums.findAll();
    }

    @Override
    public Optional<Album> findAlbumById(Integer id) {
        return albums.findById(id);
    }

    @Override
    public List<Album> findAlbumsByName(String name) {
        return albums.findByName(name);
    }

    @Override
    public Integer create(Album obj) {
        return albums.create(obj);
    }

    @Override
    public int update(Album obj) {
        return albums.update(obj);
    }

    @Override
    public int deleteAlbum(Integer id) {
        return albums.delete(id);
    }
    
}
