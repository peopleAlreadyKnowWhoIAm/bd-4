package ua.iot.labs.jdbcProjects.datasource;

import java.util.Optional;

import ua.iot.labs.jdbcProjects.domain.User;

public interface UserDao extends BasicDao<User>{
    Optional<User> finduserByEmail(String email);
}
