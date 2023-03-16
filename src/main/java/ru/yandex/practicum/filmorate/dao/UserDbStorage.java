package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        if (user.getId() > 0) {
            String sql = "insert into users(user_id, email, login, name, birthday) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getLogin(),user.getName(), user.getBirthday());
        } else {
            String sql = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),user.getName(), user.getBirthday());
        }
        String sqlSelect = "select * from users where email = ?";
        return jdbcTemplate.queryForObject(sqlSelect, this::mapRowToUser, user.getEmail());
    }

    @Override
    public User update(User user) {
        String sqlSelect = "select * from users where user_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlSelect, this::mapRowToUser, user.getId());
            String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
            return jdbcTemplate.queryForObject(sqlSelect, this::mapRowToUser, user.getId());
        } catch (DataAccessException dataAccessException) {
            return dataNotFound();
        }
    }

    @Override
    public User get(long id) {
        String sql = "select * from users where user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (DataAccessException exception) {
            return dataNotFound();
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    public void addFriend(long userId, long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        String sql = "insert into friends(user_id, friend_id, confirm_friendship) " +
                "values (?, ?, ((select user_id from friends where user_id = ? and friend_id = ?) = ? and" +
                "(select friend_id from friends where user_id = ? and friend_id = ?) = ?) is not null);" +
                "update friends set confirm_friendship = true where user_id = ? and friend_id = ?";
        jdbcTemplate.update(
                    sql, userId, friendId, friendId, userId, friendId, friendId, userId, userId, friendId, userId);
        log.info(InfoMessage.SUCCESS_ADD_FRIEND, userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        if (isConfirmFriendship(userId, friendId)) {
            String sqlUpdate = "update friends set confirm_friendship = false where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlUpdate, friendId, userId);
        }
        log.info(InfoMessage.SUCCESS_DELETE_FRIEND, userId, friendId);
    }

    public List<User> getFriends(long userId) {
        String sql = "select u.user_id, u.email, u.login, u.name, u.birthday from friends as f " +
                "left join users as u on f.friend_id = u.user_id where f.user_id = ?";
        List<User> friends = jdbcTemplate.query(sql, this::mapRowToUser, userId);
        log.info(InfoMessage.SUCCESS_FRIEND_LIST, userId);
        return friends;
    }

    public List<User> getCommonFriends(long userId, long otherUserId) {
        String sql = "select u.user_id, u.email, u.login, u.name, u.birthday from friends as f " +
                "join users u on f.friend_id = u.user_id where f.user_id = ? AND f.friend_id " +
                "in (select friend_id from friends where user_id = ?)";
        List<User> friends = jdbcTemplate.query(sql, this::mapRowToUser, userId, otherUserId);
        log.info(InfoMessage.SUCCESS_COMMON_FRIEND_LIST, userId, otherUserId);
        return friends;
    }

    private boolean isConfirmFriendship(long userId, long friendId) {
        String sql = "select user_id, friend_id from friends where user_id = ? and friend_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, Integer.class, friendId, userId) != null;
        } catch (DataAccessException dataAccessException) {
            return false;
        }
    }

    private User mapRowToUser(ResultSet rs, int i) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday")
        );
    }

    private User dataNotFound() {
        log.info(InfoMessage.DATA_NOT_EXIST);
        throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
    }
}