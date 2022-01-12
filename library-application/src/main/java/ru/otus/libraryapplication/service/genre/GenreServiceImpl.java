package ru.otus.libraryapplication.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.libraryapplication.dao.author.AuthorDao;
import ru.otus.libraryapplication.dao.genre.GenreDao;
import ru.otus.libraryapplication.domain.Genre;
import ru.otus.libraryapplication.service.string.StringService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final StringService stringService;

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public Genre getById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public Genre getByName(String name) {
        return genreDao.getByName(name);
    }

    @Override
    public void deleteById(long id) {
        authorDao.getUniqueAuthorsToGenre(id).forEach(authorDao::deleteById);

        genreDao.deleteById(id);
    }

    @Override
    public void update(long id, String name) {
        String genreName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(genreName)) {
            genreDao.update(id, genreName);
        }
    }

    @Override
    public Long create(String name) {
        String genreName = stringService.beautifyStringName(name);
        if (stringService.verifyNotBlank(genreName)) {
            return genreDao.create(genreName);
        }

        return null;
    }
}