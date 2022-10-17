package ua.iot.labs.jdbcProjects.datasource;

import java.util.List;

import ua.iot.labs.jdbcProjects.domain.Song;

public interface SongDao extends BasicDao<Song>{
    List<Song> getSongsByGenreId(Integer id);
}
