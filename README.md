# java-filmorate
Template repository for Filmorate project.
![Diagram for DB](https://github.com/ArtakJava/java-filmorate/blob/main/diagramm-db.png "Diagram")

--Following are some examples for my web application requests
1) For example, we want to get all users:
   SELECT *
   FROM users_email AS ue
   JOIN users AS u ON ue.user_id = u.user_id;

2) For example, we want to get a user with id = 1:
    SELECT *
    FROM users_email AS ue
    JOIN users AS u ON ue.user_id = u.user_id
    WHERE ue.user_id = '1';

3) For example, we want to get friends id for user with id = 1:
   SELECT f.user_id,
   f.friend_id
   FROM users_email AS ue
   JOIN friends AS f ON ue.user_id = f.user_id
   WHERE ue.user_id = '1'
   GROUP BY f.user_id;