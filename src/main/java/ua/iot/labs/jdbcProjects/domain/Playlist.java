package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Playlist {
    Integer id;
    String name;
    Integer userId;
    List<Integer> songs;
}
