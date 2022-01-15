package ru.otus.libraryapplication.service.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.libraryapplication.dao.author.AuthorDao;
import ru.otus.libraryapplication.dao.book.BookDao;
import ru.otus.libraryapplication.dao.genre.GenreDao;
import ru.otus.libraryapplication.domain.Genre;
import ru.otus.libraryapplication.service.string.StringService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE1;
import static ru.otus.libraryapplication.LibraryUnitTestData.GENRE2;

@SpringBootTest
@DisplayName("Сервис для работы с жанрами должен ")
class GenreServiceImplTest {
    @Autowired
    private GenreServiceImpl genreService;

    @MockBean
    private StringService stringService;

    @MockBean
    private GenreDao genreDao;

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private BookDao bookDao;

    @Test
    @DisplayName("корректно возвращать список всех жанров")
    void shouldCorrectGetAllGenres() {
        List<Genre> expectedGenres = List.of(GENRE1, GENRE2);
        Mockito.when(genreDao.getAll()).thenReturn(expectedGenres);
        List<Genre> actualGenres = genreService.getAll();
        assertThat(actualGenres).usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @Test
    @DisplayName("корректно возвращать жанр по ИД")
    void shouldCorrectGetGenreById() {
        Mockito.when(genreDao.getById(1)).thenReturn(GENRE1);
        Genre actualGenre = genreService.getById(1);
        assertThat(actualGenre).isEqualTo(GENRE1);
    }

    @Test
    @DisplayName("корректно возвращать жанр названию")
    void shouldCorrectGetGenreByName() {
        Mockito.when(genreDao.getByName("POEM")).thenReturn(GENRE1);
        Genre actualGenre = genreService.getByName("POEM");
        assertThat(actualGenre).isEqualTo(GENRE1);
    }

    @Test
    @DisplayName("корректно удалять жанр по ИД")
    void shouldCorrectDeleteGenreAndUnusedAuthorsById() {
        genreService.deleteById(2);
        Mockito.verify(genreDao, Mockito.times(1)).deleteById(2);
    }

    @Test
    @DisplayName("корректно обновлять жанр")
    void shouldCorrectUpdateGenre() {
        Mockito.when(stringService.beautifyStringName("DETECTIVE")).thenReturn("DETECTIVE");
        Mockito.when(stringService.verifyNotBlank("DETECTIVE")).thenReturn(true);
        genreService.update(1, "DETECTIVE");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("DETECTIVE");
        Mockito.verify(genreDao, Mockito.times(1)).update(new Genre(1, "DETECTIVE"));
    }

    @Test
    @DisplayName("не обновлять жанр при не валидном наименовании")
    void shouldNotUpdateBlankGenreName() {
        Mockito.when(stringService.beautifyStringName("  ")).thenReturn("");
        Mockito.when(stringService.verifyNotBlank("")).thenReturn(false);
        genreService.update(1, "  ");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("  ");
        Mockito.verify(stringService, Mockito.times(1)).verifyNotBlank("");
        Mockito.verify(genreDao, Mockito.never()).update(Mockito.any());
    }

    @Test
    @DisplayName("корректно создавать жанр")
    void shouldCorrectCreateGenre() {
        Mockito.when(stringService.beautifyStringName("DETECTIVE")).thenReturn("DETECTIVE");
        Mockito.when(stringService.verifyNotBlank("DETECTIVE")).thenReturn(true);
        genreService.create("DETECTIVE");
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName("DETECTIVE");
        Mockito.verify(genreDao, Mockito.times(1)).create("DETECTIVE");
    }

    @Test
    @DisplayName("не создавать не валидный жанр")
    void shouldNotCreateNullGenre() {
        Mockito.when(stringService.beautifyStringName(null)).thenReturn(null);
        Mockito.when(stringService.verifyNotBlank((String) null)).thenReturn(false);
        genreService.create(null);
        Mockito.verify(stringService, Mockito.times(1)).beautifyStringName(null);
        Mockito.verify(genreDao, Mockito.never()).create(null);
    }
}