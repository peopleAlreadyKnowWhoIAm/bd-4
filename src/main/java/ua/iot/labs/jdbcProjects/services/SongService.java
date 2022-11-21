package ua.iot.labs.jdbcProjects.services;

import java.util.List;
import java.util.Optional;

import ua.iot.labs.jdbcProjects.domain.Album;
import ua.iot.labs.jdbcProjects.domain.Author;
import ua.iot.labs.jdbcProjects.domain.Genre;
import ua.iot.labs.jdbcProjects.domain.Label;
import ua.iot.labs.jdbcProjects.domain.Song;

public interface SongService {


// Songs -----------------
    List<Song> findAllSongs();

    Optional<Song> findSongById(Integer id);

    List<Song> findSongByName(String name);

    List<Song> findSongsByGenreId(Integer id);

    Integer create(Song obj);

    int update(Song obj);

    int deleteSongById(Integer id);


// Labels -----------------
    List<Label> findAllLabels();

    Optional<Label> findLabelById(Integer id);

    List<Label> findLabelByName(String name);

    Integer create(Label obj);

    int update(Label obj);

    int deleteLabel(Integer id);


// Genres ------------------
    List<Genre> findAllGenres();

    Optional<Genre> findGenreById(Integer id);

    List<Genre> findGenreByName(String name);

    Integer create(Genre obj);

    int update(Genre obj);

    int deleteGenre(Integer id);


// Authors -------------
    List<Author> findAllAuthors();

    Optional<Author> findAuthorById(Integer id);

    List<Author> findAuthorsByName(String name);

    Integer create(Author obj);

    int update(Author obj);

    int deleteAuthor(Integer id);


// Albums ------------------
    List<Album> findAllAlbums();

    Optional<Album> findAlbumById(Integer id);

    List<Album> findAlbumsByName(String name);

    Integer create(Album obj);

    int update(Album obj);

    int deleteAlbum(Integer id);
}
