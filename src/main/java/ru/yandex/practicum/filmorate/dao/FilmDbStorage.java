package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.messageManager.InfoMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;
    private Map<Long, Set<Genre>> genresByFilm;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.likeDbStorage = likeDbStorage;
    }

    @Override
    public Film create(Film film) {
        String sql = "insert into films(name, description, release_date, duration, mpa_id) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement prst = connection.prepareStatement(sql, new String[]{"FILM_ID"});
            prst.setString(1, film.getName());
            prst.setString(2, film.getDescription());
            prst.setDate(3, film.getReleaseDate());
            prst.setInt(4, film.getDuration());
            prst.setLong(5, film.getMpa().getId());
            return prst;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        genreDbStorage.setFilmGenres(film.getGenres(), film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlSelect = "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name " +
                "from films as f left join mpa as m on f.mpa_id = m.mpa_id where film_id = ?";
        try {
            long filmId = film.getId();
            jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, filmId);
            String sql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                    " where film_id = ?";
            jdbcTemplate.update(
                    sql,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    filmId
            );
            genreDbStorage.updateFilmGenres(film.getGenres(), filmId);
            genresByFilm.remove(filmId);
            return jdbcTemplate.queryForObject(sqlSelect, this::mapRowToFilm, filmId);
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
        genresByFilm = getGenresByFilm();
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public List<Film> getPopular(int count) {
        String sql = "select f.film_id as film_id, f.name as name, f.description as description, " +
                "f.release_date as release_date, f.duration as duration, f.mpa_id as mpa_id, m.mpa_name as mpa_name " +
                "from films f left join mpa m on f.mpa_id = m.mpa_id " +
                "left join likes l on f.film_id = l.film_id group by f.film_id order by sum(user_id) desc limit(?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }


    private Map<Long, Set<Genre>> getGenresByFilm() {
        String sql = "select * from category c left join genre g on c.genre_id = g.genre_id";
        return jdbcTemplate.query(sql, (ResultSetExtractor<Map<Long, Set<Genre>>>) rs -> {
            HashMap<Long, Set<Genre>> map = new HashMap<>();
            while (rs.next()) {
                long filmId = rs.getLong("film_id");
                if (map.containsKey(filmId)) {
                    map.get(filmId).add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
                }
                Set<Genre> genresTemp = new HashSet<>();
                genresTemp.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
                map.put(filmId, genresTemp);
            }
            return map;
        });
    }

    private Film mapRowToFilm(ResultSet rs, int i) throws SQLException {
        long filmId = rs.getInt("film_id");
        Set<Genre> genres;
        if (genresByFilm.containsKey(filmId)) {
            genres = genresByFilm.get(filmId);
        } else {
            genres = genreDbStorage.getFilmGenres(filmId);
        }
        return new Film(
                filmId,
                rs.getString("name"),
                rs.getString("description"),
                Date.valueOf(rs.getString("release_date")),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                genres,
                likeDbStorage.getFilmLikes(rs.getInt("film_id"))
        );
    }

    private Film dataNotFound() {
        log.info(InfoMessage.DATA_NOT_EXIST);
        throw new NotFoundException(InfoMessage.DATA_NOT_EXIST);
    }
}