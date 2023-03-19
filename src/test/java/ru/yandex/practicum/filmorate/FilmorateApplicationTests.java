package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql("/schema.sql")
@Sql("/data.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final FilmDbStorage filmDbStorage ;
	private final LikeDbStorage likeDbStorage;
	private final UserDbStorage userDbStorage;
	private Film film;
	private User user;

	@BeforeEach
	public void beforeEach() {
		film = new Film(
				"Первый фильм",
				"Первый фильм для теста",
				Date.valueOf("2010-2-2"),
				60,
				new Mpa(1, "G"),
				Collections.emptySet(),
				Collections.emptySet()
		);
		user = new User(
				1,
				"user@email.ru",
				"login",
				"юзер",
				new Date(1995, 2, 2)
		);
	}

	@AfterEach
	public void afterEach() {
	}

	@Test
	public void createFilm() {
		assertEquals(film, filmDbStorage.create(film));
	}

	@Test
	public void updateFilm() {
		filmDbStorage.create(film);
		Film oldFilm = filmDbStorage.get(1);
		oldFilm.setDescription("Описание обновленного фильма");
		assertEquals(oldFilm, filmDbStorage.update(oldFilm));
	}

	@Test
	public void getFilm() {
		assertEquals(filmDbStorage.create(film), filmDbStorage.get(1));
	}

	@Test
	public void getAllFilms() {
		Film otherFilm = new Film(
				"Еще один фильм",
				"Еще один фильм для теста",
				Date.valueOf("2021-2-2"),
				60,
				new Mpa(1, "G"),
				Collections.emptySet(),
				Collections.emptySet()
		);
		List<Film> films = new ArrayList<>();
		films.add(filmDbStorage.create(film));
		films.add(filmDbStorage.create(otherFilm));
		assertEquals(films, filmDbStorage.getAll());
	}

	@Test
	public void addLike() {
		Like like = new Like(1);
		likeDbStorage.addLike(filmDbStorage.create(film).getId(), userDbStorage.create(user).getId());
		assertEquals(like, likeDbStorage.get(1));
	}

	@Test
	public void removeLike() {
		long filmId = filmDbStorage.create(film).getId();
		long userId = userDbStorage.create(user).getId();
		likeDbStorage.addLike(filmId, userId);
		boolean isRemoveLike = likeDbStorage.removeLike(filmId, userId);
		assertTrue(isRemoveLike);
	}

	@Test
	public void getPopular() {
		filmDbStorage.create(film);
		filmDbStorage.create(
				new Film(
						"Еще один фильм",
						"Еще один фильм для теста",
						Date.valueOf("1988-3-2"),
						60,
						new Mpa(1, "G"),
						Collections.emptySet(),
						Collections.emptySet()));
		filmDbStorage.create(
				new Film(
						"Третий фильм",
						"Еще один фильм для теста",
						Date.valueOf("2010-5-8"),
						20,
						new Mpa(1, "G"),
						Collections.emptySet(),
						Collections.emptySet()
				)
		);
		userDbStorage.create(user);
		userDbStorage.create(
				new User(
						2,
						"user2@email.ru",
						"login2",
						"юзер2",
						new Date(1996, 2, 2)
				)
		);
		likeDbStorage.addLike(1, 1);
		likeDbStorage.addLike(2, 2);
		likeDbStorage.addLike(1, 2);
		List<Film> threePopularFilms = new ArrayList<>();
		threePopularFilms.add(filmDbStorage.get(1));
		threePopularFilms.add(filmDbStorage.get(2));
		threePopularFilms.add(filmDbStorage.get(3));
		assertEquals(threePopularFilms, filmDbStorage.getPopular(3));
	}

	@Test
	public void createUser() {
		assertEquals(user, userDbStorage.create(user));
	}

	@Test
	public void updateUser() {
		userDbStorage.create(user);
		User oldUser = userDbStorage.get(1);
		oldUser.setName("новыйюзер");
		assertEquals(oldUser, userDbStorage.update(oldUser));
	}

	@Test
	public void getUser() {
		assertEquals(userDbStorage.create(user), userDbStorage.get(1));
	}

	@Test
	public void getAllUsers() {
		User otherUser = new User(
				2,
				"user2@email.ru",
				"login",
				"второйюзер",
				new Date(1992, 2, 2)
		);
		List<User> users = new ArrayList<>();
		users.add(userDbStorage.create(user));
		users.add(userDbStorage.create(otherUser));
		assertEquals(users, userDbStorage.getAll());
	}

	@Test
	public void addFriend() {
		userDbStorage.create(user);
		userDbStorage.create(
				new User(
						2,
						"user2@email.ru",
						"login",
						"второйюзер",
						new Date(1992, 2, 2)
				)
		);
		userDbStorage.addFriend(1, 2);
		List<User> friends = new ArrayList<>();
		friends.add(userDbStorage.get(2));
		assertEquals(friends, userDbStorage.getFriends(1));
	}

	@Test
	public void removeFriend() {
		userDbStorage.create(user);
		userDbStorage.create(
				new User(
						2,
						"user2@email.ru",
						"login",
						"второйюзер",
						new Date(1992, 2, 2)
				)
		);
		userDbStorage.addFriend(1, 2);
		List<User> friends = new ArrayList<>();
		userDbStorage.removeFriend(1, 2);
		assertEquals(friends, userDbStorage.getFriends(1));
	}

	@Test
	public void getFriends() {
		userDbStorage.create(user);
		userDbStorage.create(
				new User(
						2,
						"user2@email.ru",
						"login",
						"второйюзер",
						new Date(1992, 2, 2)
				)
		);
		userDbStorage.create(
				new User(
						3,
						"user3@email.ru",
						"login",
						"третийюзер",
						new Date(1994, 2, 2)
				)
		);
		userDbStorage.addFriend(1, 2);
		userDbStorage.addFriend(1, 3);
		List<User> friends = new ArrayList<>();
		friends.add(userDbStorage.get(2));
		friends.add(userDbStorage.get(3));
		assertEquals(friends, userDbStorage.getFriends(1));
	}

	@Test
	public void getCommonFriends() {
		userDbStorage.create(user);
		userDbStorage.create(
				new User(
						2,
						"user2@email.ru",
						"login",
						"второйюзер",
						new Date(1992, 2, 2)
				)
		);
		userDbStorage.create(
				new User(
						3,
						"user3@email.ru",
						"login",
						"третийюзер",
						new Date(1994, 2, 2)
				)
		);
		userDbStorage.addFriend(1, 3);
		userDbStorage.addFriend(2, 3);
		List<User> commonFriends = new ArrayList<>();
		commonFriends.add(userDbStorage.get(3));
		assertEquals(commonFriends, userDbStorage.getCommonFriends(1, 2));
	}
}