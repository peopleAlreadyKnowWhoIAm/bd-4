package ua.iot.labs.jdbcProjects.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {
    Integer id;
    String name;
    List<Integer> relatedGenresId;
}
