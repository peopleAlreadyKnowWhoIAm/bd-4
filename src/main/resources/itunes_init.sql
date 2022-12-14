-- MySQL Script generated by MySQL Workbench and changed by me

-- Schema itunes
DROP SCHEMA IF EXISTS itunes;

CREATE SCHEMA itunes;

USE itunes;

-- Table label
CREATE TABLE label (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- Table album
CREATE TABLE album (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  year_of_publishing INT NULL,
  label_id INT NULL
);

-- Table genre
CREATE TABLE genre (
  id INT NOT NULL auto_increment PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- Table song
CREATE TABLE song (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  length INT NOT NULL,
  album_id INT NULL,
  genre_id INT NULL
);

-- Table author
CREATE TABLE author (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL
);

-- Table related_genres
CREATE TABLE related_genres (
  genre_id INT NOT NULL,
  related_genre_id INT NOT NULL,
  PRIMARY KEY (genre_id, related_genre_id)
);

-- Table user
CREATE TABLE user (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL UNIQUE
);

-- Table user_playlist_info
CREATE TABLE user_playlist_info (
  id INT NOT NULL PRIMARY KEY auto_increment,
  user_id INT NOT NULL,
  name VARCHAR(45) NOT NULL
);

-- Table songs_saved_by_user
CREATE TABLE songs_saved_by_user (
  song_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (song_id, user_id)
);

-- Table albums_saved_by_user
CREATE TABLE albums_saved_by_user (
  album_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (album_id, user_id)
);

-- Table user_prefer_genre
CREATE TABLE user_prefer_genre (
  user_id INT NOT NULL,
  genre_id INT NOT NULL,
  PRIMARY KEY (user_id, genre_id)
);

-- Table playlist_has_song
CREATE TABLE playlist_has_song (
  user_playlist_info_id INT NOT NULL,
  song_id INT NOT NULL,
  PRIMARY KEY (user_playlist_info_id, song_id)
);

-- Table song_commercial
CREATE TABLE song_commercial (
  song_id INT NOT NULL PRIMARY KEY,
  price DECIMAL(8, 2) NOT NULL,
  num_of_downloads INT NOT NULL
);

-- Table user_credential
CREATE TABLE user_credential (
  user_id INT NOT NULL PRIMARY KEY,
  PASSWORD VARCHAR(45) NOT NULL
);

-- Table song_has_author
CREATE TABLE song_has_author (
  song_id INT NOT NULL,
  author_id INT NOT NULL,
  PRIMARY KEY (song_id, author_id)
);

-- Table album_has_author
CREATE TABLE album_has_author (
  album_id INT NOT NULL,
  author_id INT NOT NULL,
  PRIMARY KEY (album_id, author_id)
);

-- Table album_commercial
CREATE TABLE album_commercial (
  album_id INT NOT NULL PRIMARY KEY,
  price DECIMAL(8, 2) NOT NULL,
  num_of_downloads INT NOT NULL
);

-- ---------------------------------------------------------------
-- Constraints section
-- label
CREATE INDEX inx_label_name ON label(name);

-- album
ALTER TABLE
  album
ADD
  CONSTRAINT fk_album_label_id FOREIGN KEY (label_id) REFERENCES label(id) ON DELETE
SET
  NULL ON UPDATE CASCADE;

CREATE INDEX inx_album_name ON album(name);

-- album_commercial
ALTER TABLE
  album_commercial
ADD
  CONSTRAINT fk_album_commercial_album_id -- PK
  FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE ON UPDATE CASCADE;

-- genre
CREATE INDEX inx_genre_name ON genre(name);

-- related genres
ALTER TABLE
  related_genres 
ADD
  CONSTRAINT fk_related_genres_genre_id FOREIGN KEY (genre_id) REFERENCES genre(id) ON UPDATE CASCADE ON DELETE CASCADE,
ADD
  CONSTRAINT fk_related_genres_genre2_id FOREIGN KEY (related_genre_id) REFERENCES genre(id) ON UPDATE CASCADE ON DELETE CASCADE;
-- song
ALTER TABLE
  song
ADD
  CONSTRAINT fk_song_album_id FOREIGN KEY (album_id) REFERENCES album(id) ON UPDATE CASCADE ON DELETE
SET
  NULL,
ADD
  CONSTRAINT fk_song_genre_id FOREIGN KEY (genre_id) REFERENCES genre(id) ON UPDATE CASCADE ON DELETE
SET
  NULL;

CREATE INDEX inx_song_name ON song(name);

-- song commercial
ALTER TABLE
  song_commercial
ADD
  CONSTRAINT fk_song_commercial_song_id -- PK
  FOREIGN KEY (song_id) REFERENCES song(id) ON UPDATE CASCADE ON DELETE CASCADE;

-- author
CREATE INDEX inx_author_name ON author(name);

-- user
CREATE INDEX inx_user_name ON user(name);
CREATE INDEX inx_user_email ON user(email);

-- user_playlist_info
ALTER TABLE
  user_playlist_info
ADD
  CONSTRAINT fk_user_playlist_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX inx_user_playlist_info_name ON user_playlist_info(name);

-- songs saved by user
ALTER TABLE
  songs_saved_by_user 
ADD
  CONSTRAINT fk_song_saved_by_user_song_id FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD
  CONSTRAINT fk_song_saved_by_user_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE;


-- albums_saved_by_user
ALTER TABLE
  albums_saved_by_user 
ADD
  CONSTRAINT fk_album_saved_by_user_album_id FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD
  CONSTRAINT fk_album_saved_by_user_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE;


-- user_prefer_genre
ALTER TABLE
  user_prefer_genre 
ADD
  CONSTRAINT fk_user_prefer_genre_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD
  CONSTRAINT fk_user_prefer_genre_genre_id FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE ON UPDATE CASCADE;


-- playlist_has_song
ALTER TABLE
  playlist_has_song 
ADD
  CONSTRAINT fk_user_playlist_has_song_user_playlist_id FOREIGN KEY (user_playlist_info_id) REFERENCES user_playlist_info(id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD
  CONSTRAINT fk_user_playlist_has_song_song_id FOREIGN KEY (song_id) REFERENCES song(id) ON DELETE CASCADE ON UPDATE CASCADE;


-- user_credential
ALTER TABLE
  user_credential
ADD
  CONSTRAINT fk_user_credential_user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE;

-- song_has_author
ALTER TABLE
  song_has_author
ADD
  CONSTRAINT fk_song_has_author_song_id FOREIGN KEY (song_id) REFERENCES song(id) ON UPDATE CASCADE ON DELETE CASCADE,
ADD
  CONSTRAINT fk_song_has_author_author_id FOREIGN KEY (author_id) REFERENCES author(id) ON UPDATE CASCADE ON DELETE CASCADE;

-- album_has_author
ALTER TABLE
  album_has_author
ADD
  CONSTRAINT fk_album_has_author_song_id FOREIGN KEY (album_id) REFERENCES album(id) ON UPDATE CASCADE ON DELETE CASCADE,
ADD
  CONSTRAINT fk_album_has_author_author_id FOREIGN KEY (author_id) REFERENCES author(id) ON UPDATE CASCADE ON DELETE CASCADE;

-- -------------------------------------------------------------------------------------------------------------
-- Data section
-- Data for table label
INSERT INTO
  label (id, name)
VALUES
  (1, 'Columbia Records'),
  (2, 'L-M Records'),
  (3, 'Nice Life'),
  (4, 'EMI American Records'),
  (5, 'Republic Records'),
  (6, 'Parkwood Entertainment'),
  (7, 'Epic'),
  (8, 'Rimas Entertainment'),
  (9, 'Paramount Pictures'),
  (10, 'River House Artists');

-- Data for table album
INSERT INTO
  album (id, name, year_of_publishing, label_id)
VALUES
  (1, 'Harrys House', 2022, 1),
  (2, 'Gemini Rights', 2022, 2),
  (3, 'Special', 2022, 3),
  (4, 'Hounds of Love', 1985, 4),
  (5, 'Queen Radio: Volume 1', 2022, 5),
  (6, 'Twelve Carat Toothpaste', 2022, 5),
  (7, 'Renaissance', 2022, 6),
  (8, 'I Never Liked You', 2022, 7),
  (9, 'Un Verano Sin Ti', 2022, 8),
  (10, 'Top Gun: Maverick', 2022, 9),
  (11, 'Growin Up', 2022, 10);

-- Data for table genre
INSERT INTO
  genre (id, name)
VALUES
  (1, 'Hip-Hop'),
  (2, 'Pop rock'),
  (3, 'R&B'),
  (4, 'Reggaeton'),
  (5, 'Country'),
  (6, 'Rock');

-- Data for table song
INSERT INTO
  song (id, name, length, album_id, genre_id)
VALUES
  (1, 'As It Was', 166, 1, 2),
  (2, 'Bad Habit', 233, 2, 3),
  (3, 'About Damn Time', 191, 3, 1),
  (4, 'Running Up That Hill', 294, 4, 1),
  (5, 'Sunroof', 163, NULL, 1),
  (6, 'Hold Me Closer', 203, NULL, 1),
  (7, 'Super Freaky Girl', 173, 5, 3),
  (8, 'I Like You', 193, 6, 1),
  (9, 'Break My Soul', 240, 7, 1),
  (10, 'Wait For U', 190, 8, 3),
  (11, 'Me Porto Bonito', 179, 9, 4),
  (12, 'Late Night Talking', 178, 1, 2),
  (13, 'You Proof', 158, NULL, 5),
  (14, 'I Aint Worried', 147, 10, 2),
  (15, 'The Kind Of Love We Make', 225, 11, 5);

-- Data for table author
INSERT INTO
  author (id, name)
VALUES
  (1, 'Harry Styles'),
  (2, 'Steve Lacy'),
  (3, 'Lizzo'),
  (4, 'Kate Bush'),
  (5, 'Nicky Youre'),
  (6, 'dazy'),
  (7, 'Elton John'),
  (8, 'Britney Spears'),
  (9, 'Nicki Minaj'),
  (10, 'Post Malone'),
  (11, 'Doja Cat'),
  (12, 'Beyonce'),
  (13, 'Future'),
  (14, 'Tems'),
  (15, 'Bad Bunny'),
  (16, 'Chencho Corleone'),
  (17, 'Morgan Wallen'),
  (18, 'OneRepublic'),
  (19, 'Luke Combs'),
  (20, 'Drake');

-- Data for table related_genres
INSERT INTO
  related_genres (genre_id, related_genre_id)
VALUES
  (1, 4),
  (2, 1),
  (2, 6),
  (3, 1),
  (3, 6);

-- Data for table user
INSERT INTO
  user (id, name, email)
VALUES
  (1, '???????????? ????????', 'emeal1@gmailc.om'),
  (2, '?????????????? ????????????', 'emeal@gmailc.om'),
  (3, '?????????? ??????????', 'emeal3@gmailc.om'),
  (4, '???????????????? ????????', 'emeal4@gmailc.om'),
  (5, '???????? ??????????', 'emeal5@gmailc.om'),
  (6, '?????????????? ??????????????????', 'emeal6@gmailc.om'),
  (7, '?????????? ??????????????????', 'emeal7@gmailc.om'),
  (8, '?????????????????? ??????????', 'emeal8@gmailc.om'),
  (9, '?????????????????????? ??????????????', 'emeal9@gmailc.om'),
  (10, '???????????????????? ????????????', 'emeal10@gmailc.om'),
  (11, '????????????????????	????????????', 'emeal11@gmailc.om'),
  (12, '???????????????? ??????????', 'emeal12@gmailc.om'),
  (13, '?????????????????? ??????????', 'emeal13@gmailc.om'),
  (14, '?????????????? ????????????', 'emeal14@gmailc.om'),
  (15, '???????? ????????', 'emeal15@gmailc.om');

-- Data for table user_playlist_info
INSERT INTO
  user_playlist_info (id, user_id, name)
VALUES
  (1, 2, 'Super muzlo'),
  (2, 5, 'Puper muzlo'),
  (3, 6, 'Super puper muzlo');

-- Data for table songs_saved_by_user
INSERT INTO
  songs_saved_by_user (song_id, user_id)
VALUES
  (1, 1),
  (4, 1),
  (8, 4),
  (1, 4),
  (4, 4),
  (5, 4),
  (2, 5),
  (1, 5),
  (4, 6),
  (2, 8),
  (5, 8),
  (1, 9),
  (2, 11),
  (9, 14),
  (5, 14),
  (1, 14);

-- Data for table albums_saved_by_user
INSERT INTO
  albums_saved_by_user (album_id, user_id)
VALUES
  (1, 1),
  (3, 2),
  (3, 3),
  (5, 4),
  (8, 5),
  (11, 6),
  (11, 7),
  (11, 8),
  (10, 9);

-- Data for table user_prefer_genre
INSERT INTO
  user_prefer_genre (user_id, genre_id)
VALUES
  (1, 1),
  (1, 2),
  (5, 3),
  (6, 2),
  (6, 1),
  (6, 3),
  (7, 5),
  (9, 2),
  (11, 5),
  (15, 5),
  (15, 2),
  (15, 6);

-- Data for table playlist_has_song
INSERT INTO
  playlist_has_song (user_playlist_info_id, song_id)
VALUES
  (1, 1),
  (1, 2),
  (1, 3),
  (1, 5),
  (2, 5),
  (2, 9),
  (2, 8),
  (2, 4),
  (2, 3),
  (3, 9),
  (3, 11),
  (3, 12),
  (3, 15);

-- Data for table song_commercial
INSERT INTO
  song_commercial (song_id, price, num_of_downloads)
VALUES
  (1, 1.00, 13303),
  (2, 1.50, 42424),
  (3, 2.00, 41564),
  (4, 1.00, 56213),
  (5, 2.00, 42612),
  (6, 0.50, 121213),
  (7, 0.80, 42162),
  (8, 0.25, 43641242),
  (9, 0.50, 424132),
  (10, 0.35, 124661),
  (11, 0.40, 4212343),
  (12, 0.21, 213411),
  (13, 0.55, 4214631),
  (14, 0.80, 21466),
  (15, 0.74, 465216);

-- Data for table user_credential
INSERT INTO
  user_credential (user_id, PASSWORD)
VALUES
  (1, 'password'),
  (2, 'password'),
  (3, 'password'),
  (4, 'password'),
  (5, 'password'),
  (6, 'password'),
  (7, 'password'),
  (8, 'password'),
  (9, 'password'),
  (10, 'password'),
  (11, 'password'),
  (12, 'password'),
  (13, 'password'),
  (14, 'password'),
  (15, 'password');

-- Data for table song_has_author
INSERT INTO
  song_has_author (song_id, author_id)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5),
  (5, 6),
  (6, 7),
  (6, 8),
  (7, 9),
  (8, 10),
  (8, 11),
  (9, 12),
  (10, 13),
  (10, 14),
  (11, 15),
  (11, 16),
  (12, 1),
  (13, 17),
  (14, 18),
  (15, 19),
  (10, 20);

-- Data for table album_has_author
INSERT INTO
  album_has_author (album_id, author_id)
VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 9),
  (6, 10),
  (7, 12),
  (8, 13),
  (9, 15),
  (11, 19);

-- Data for table album_commercial
INSERT INTO
  album_commercial (album_id, price, num_of_downloads)
VALUES
  (1, 5, 545424),
  (2, 10, 5845566),
  (3, 7, 4555455),
  (4, 1, 54545678),
  (5, 22, 455665466),
  (6, 10, 545555),
  (7, 12, 565645456),
  (8, 8, 455664222),
  (9, 7, 22655455),
  (10, 11, 545566),
  (11, 13, 54456565);