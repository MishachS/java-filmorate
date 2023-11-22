drop table FILM IF EXISTS CASCADE;
drop table RATING IF EXISTS CASCADE;
drop table GENRE IF EXISTS CASCADE;
drop table USERS IF EXISTS CASCADE;
drop table FRIEND IF EXISTS CASCADE;
drop table LIKES IF EXISTS CASCADE;
drop table FILM_GENRE IF EXISTS CASCADE;
create TABLE IF NOT EXISTS RATING (
	RATING_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	RATING_TITLE VARCHAR NOT NULL
);
create TABLE IF NOT EXISTS FILM (
	FILM_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR(255) NOT NULL,
	DESCRIPTION VARCHAR(255) NOT NULL,
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL,
	RATING_ID INTEGER references RATING(RATING_ID) NOT NULL
);
create TABLE IF NOT EXISTS FILM_GENRE (
    FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID, GENRE_ID)
	);
create TABLE IF NOT EXISTS GENRE (
	GENRE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR NOT NULL
);
create TABLE IF NOT EXISTS USERS (
	USER_ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	EMAIL VARCHAR(255) NOT NULL,
	LOGIN VARCHAR(255) NOT NULL,
	NAME VARCHAR(255),
	BIRTHDAY DATE NOT NULL
);
create TABLE IF NOT EXISTS FRIEND (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL,
	FRIENDSHIP_STATUS BOOLEAN,
	CONSTRAINT FRIEND_PK PRIMARY KEY (USER_ID,FRIEND_ID)
);
create TABLE IF NOT EXISTS LIKES (
	FILM_ID INTEGER NOT NULL,
	USERS_ID INTEGER NOT NULL,
	CONSTRAINT LIKE_PK PRIMARY KEY (FILM_ID,USERS_ID)
);