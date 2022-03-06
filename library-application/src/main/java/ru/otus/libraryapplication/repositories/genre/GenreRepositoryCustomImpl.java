package ru.otus.libraryapplication.repositories.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.libraryapplication.domain.Book;
import ru.otus.libraryapplication.domain.Genre;

@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public void deleteWithBooksByGenreId(String id) {
        Query query = Query.query(Criteria.where("genre.id").is(id));
        mongoTemplate.remove(query, Book.class);

        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Genre.class);
    }
}
