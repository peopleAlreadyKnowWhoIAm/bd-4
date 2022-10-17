package ua.iot.labs.jdbcProjects.domain;

import lombok.Data;

@Data
public class PairWithId<T> {
    Album id;
    T data;
}
