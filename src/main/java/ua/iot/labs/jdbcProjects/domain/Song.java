package ua.iot.labs.jdbcProjects.domain;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Song {
    Integer id;
    String name;
    Integer length;
    List<Integer> authors;
    Optional<Integer> album = Optional.empty();
    Integer genre; 
    Double price;
    Integer numberOfDownloads;
}
