package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public Film create(Film film) {
        if (film.getId() > 0) {
            String sql = "insert into films(film_id, name, description, release_date, duration, mpa_id) " +
                    "values (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    sql,
                    film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
        } else {
            String sql = "insert into films(name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    sql,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId()
            );
        }
        String sqlSelect = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name " +
                "from films as f left join mpa as m on f.mpa_id = m.mpa_id where f.name = ?";
        Film createdFilm = jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, film.getName());
        if (createdFilm != null) {
            genreDbStorage.setFilmGenres(film.getGenres(), createdFilm.getId());
        }
        return jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, film.getName());
    }

    @Override
    public Film update(Film film) {
        String sqlSelect = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name " +
                "from films as f left join mpa as m on f.mpa_id = m.mpa_id where film_id = ?";
        try {
            jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, film.getId());
            String sql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                    " where film_id = ?";
            jdbcTemplate.update(
                    sql,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            genreDbStorage.updateFilmGenres(film.getGenres(), film.getId());
            return jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, film.getId());
        } catch (DataAccessException dataAccessException) {
            return dataNotFound();
        }
    }

    @Override
    public Film get(long id) {
        String sql = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name " +
                "from films as f left join mpa as m on f.mpa_id = m.mpa_id where film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (DataAccessException dataAccessException) {
            return dataNotFound();
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, " +
                "m.mpa_name from films as f left join mpa as m on f.mpa_id = m.mpa_id";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public Like addLike(long filmId, long userId) {
        return likeDbStorage.addLike(filmId, userId);
    }

    public boolean removeLike(long filmId, long userId) {
        return likeDbStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        String sql = "select f.film_id as film_id, f.name as name, f.description as description, " +
                "f.release_date as release_date, f.duration as duration, f.mpa_id as mpa_id, m.mpa_name as mpa_name " +
                "from films f left join mpa m on f.mpa_id = m.mpa_id " +
                "left join likes l on f.film_id = l.film_id group by f.film_id order by sum(user_id) desc limit(?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int i) throws SQLException {
        return new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                LocalDate.parse(rs.getString("release_date")),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                genreDbStorage.getFilmGenres(rs.getInt("film_id")),
                likeDbStorage.getFilmLikes(rs.getInt("film_id"))
        );
    }

    private Film dataNotFound() {
        log.info(InfoMessage.DATA_NOT_EXIST);
        throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
    }
}