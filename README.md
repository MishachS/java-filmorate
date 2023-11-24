# java-filmorate
Template repository for Filmorate project.
![](C:\Users\sasha\dev\java-filmorate\Untitled.png)


1) получение лайков фильма SELECT f.name, COUNT(l.user_id) FROM film AS f RIGHT OUTER JOIN like AS l ON f.id = l.id


2) получение 10 популярных фильмов SELECT f.name FROM film AS f WHERE f.id IN (SELECT id FROM like GROUP BY id ORDER BY COUNT(users_id DESC) LIMIT 10)


3) получение всех пользователей SELECT user_id, login, name FROM user


4) получение общих друзей SELECT user_id, login, name FROM users AS u RIGHT OUTER JOIN friends AS f ON u.user_id = f.user_id WHERE f.friend_id IN (SELECT friend_id FROM friend GROUP BY user_id WHERE user_id = 'id второго пользователя')
