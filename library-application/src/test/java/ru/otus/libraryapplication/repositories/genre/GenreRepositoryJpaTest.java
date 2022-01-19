package ru.otus.libraryapplication.repositories.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.libraryapplication.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE1;
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE2;

@DataJpaTest
@DisplayName("DAO для работы с жанрами должно ")
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @DisplayName("возвращать все жанры из БД")
    @Test
    void shouldReturnAllGenres() {
        List<Genre> expectedGenres = List.of(GENRE1, GENRE2);
        List<Genre> actualGenres = genreRepositoryJpa.getAll();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @DisplayName("возвращать по ИД жанр из БД, если он там существует")
    @Test
    void shouldReturnExistGenreById() {
        Genre actualAuthor = genreRepositoryJpa.getById(1);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(GENRE1);
    }

    @DisplayName("возвращать по ИД жанра null из БД, если он там не существует")
    @Test
    void shouldReturnNullForNonExistGenreById() {
        Genre actualGenre = genreRepositoryJpa.getById(3);
        assertThat(actualGenre).isNull();
    }

    @DisplayName("возвращать по названию жанр из БД, если он там существует")
    @Test
    void shouldReturnExistGenreByName() {
        Genre actualGenre = genreRepositoryJpa.getByName("POEM");
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(GENRE1);
    }

    @DisplayName("возвращать по названию жанра null из БД, если он там не существует")
    @Test
    void shouldReturnNullForNonExistGenreByName() {
        Genre actualGenre = genreRepositoryJpa.getByName("DETECTIVE");
        assertThat(actualGenre).isNull();
    }

    @DisplayName("удалять по ИД жанр из БД, если он там существует")
    @Test
    void shouldCorrectDeleteGenreById() {
        genreRepositoryJpa.deleteById(2);
        assertThatCode(() -> genreRepositoryJpa.getById(2)).isNull();
    }

    @DisplayName("не генерировать ошибки при удалении по ИД жанра из БД, если его там не существует")
    @Test
    void shouldDoesNotThrowExceptionsWhereDeleteNonExistGenreById() {
        assertThatCode(() -> genreRepositoryJpa.deleteById(3)).doesNotThrowAnyException();
    }

    @DisplayName("обновлять жанр в БД, если он там существует")
    @Test
    void updateExistingGenre() {
        Genre newGenre = new Genre(2L, "DETECTIVE");
        genreRepositoryJpa.update(newGenre);
        Genre actualGenre = genreRepositoryJpa.getById(2);
        Genre expectedGenre = new Genre(2L, "DETECTIVE");
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("не генерировать ошибки при обновлении жанра в БД, если он там не существует")
    @Test
    void updateNonExistGenre() {
        Genre newGenre = new Genre(3L, "DETECTIVE");
        assertThatCode(() -> genreRepositoryJpa.update(newGenre)).doesNotThrowAnyException();
    }

    @DisplayName("записывать новый жанр в БД")
    @Test
    void createNewGenre() {
        Genre actualGenre = genreRepositoryJpa.create(new Genre(null, "DETECTIVE"));
        Genre expectedGenre = new Genre(3L, "DETECTIVE");
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }
}