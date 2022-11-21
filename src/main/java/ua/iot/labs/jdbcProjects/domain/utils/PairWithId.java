package ua.iot.labs.jdbcProjects.domain.utils;

import lombok.Data;
import ua.iot.labs.jdbcProjects.domain.Album;

@Data
public class PairWithId<T> {
    Album id;
    T data;
}
