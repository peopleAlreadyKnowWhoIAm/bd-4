package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
   Integer id;
   String name;
   String email;
   List<Integer> prefered_genres; 
   List<Integer> playlists;
   List<Integer> albums;
   List<Integer> songs;
}
