package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("LikeDbStorage")
@Slf4j
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Like addLike(long filmId, long userId) {
        String sql = "insert into likes(film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        String sqlCreate = "select film_id, user_id from likes where film_id = ? and user_id = ?";
        return jdbcTemplate.queryForObject(sqlCreate, this::mapRowToLike, filmId, userId);
    }

    @Override
    public boolean removeLike(long filmId, long userId) {
        String sql = "delete from likes where film_id = ? and user_id = ?";
        try {
            jdbcTemplate.update(sql, filmId, userId);
            return true;
        } catch (DataAccessException dataAccessException) {
            return false;
        }
    }

    @Override
    public Like get(long id) {
        String sql = "select * from likes where like_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToLike, id);
        } catch (DataAccessException exception) {
            return dataNotFound();
        }
    }

    @Override
    public List<Like> getAll() {
        String sql = "select * from likes";
        return jdbcTemplate.query(sql, this::mapRowToLike);
    }

    public Set<Like> getFilmLikes(long filmId) {
        String sql = "select user_id from likes where film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowToLike, filmId));
    }

    private Like mapRowToLike(ResultSet rs, int i) throws SQLException {
        return new Like(rs.getInt("user_id"));
    }

    private Like dataNotFound() {
        log.info(InfoMessage.DATA_NOT_EXIST);
        throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
    }
}