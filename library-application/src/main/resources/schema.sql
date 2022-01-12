DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES
(
    ID   BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL CHECK CHARACTER_LENGTH(TRIM(BOTH ' ' FROM NAME)) > 0
        );

DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS
(
    ID   BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL CHECK CHARACTER_LENGTH(TRIM(BOTH ' ' FROM NAME)) > 0
        );

DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS
(
    ID        BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME      VARCHAR(255) NOT NULL CHECK CHARACTER_LENGTH(TRIM(BOTH ' ' FROM NAME)) > 0,
    AUTHOR_ID BIGINT       NOT NULL references AUTHORS (ID) ON DELETE CASCADE,
    GENRE_ID  BIGINT       NOT NULL references GENRES (ID) ON DELETE CASCADE
);