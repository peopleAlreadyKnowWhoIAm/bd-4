package ua.iot.labs.jdbcProjects.domain;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Album {
    Integer id;
    String name;
    List<Integer> authors;
    Integer yearOfPublishing;
    Optional<Integer> label = Optional.empty();
    Double price;
    Integer numberOfDownloads;
}
