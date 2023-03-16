package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("GenreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(long id) {
        String sql = "select * from genre where genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (DataAccessException exception) {
            return dataNotFound();
        }
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    public Set<Genre> getFilmGenres(long filmId) {
        String sql = "select c.genre_id, g.name from category c left join genre g on c.genre_id = g.genre_id " +
                "where film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowToGenre, filmId));
    }

    public void setFilmGenres(Set<Genre> genres, long filmId) {
        String sql = "insert into category(film_id, genre_id) values (?, ?)";
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sql, filmId, genre.getId());
            }
        }
    }

    public void updateFilmGenres(Set<Genre> genres, long filmId) {
        String sql = "delete from category where film_id = ?;";
        jdbcTemplate.update(sql, filmId);
        setFilmGenres(genres, filmId);
    }

    private Genre mapRowToGenre(ResultSet rs, int i) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getNString("name"));
    }

    private Genre dataNotFound() {
        log.info(InfoMessage.DATA_NOT_EXIST);
        throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
    }
}