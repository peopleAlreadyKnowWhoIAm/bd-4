package ua.iot.labs.jdbcProjects.datasource;

import java.util.List;

import ua.iot.labs.jdbcProjects.domain.Playlist;

public interface PlaylistDao extends BasicDao<Playlist> {
   List<Playlist> findByUserId(Integer userId); 
}
