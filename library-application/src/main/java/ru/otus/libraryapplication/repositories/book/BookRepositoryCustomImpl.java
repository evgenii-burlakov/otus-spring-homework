package ru.otus.libraryapplication.repositories.book;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.libraryapplication.domain.Author;
import ru.otus.libraryapplication.domain.Book;
import ru.otus.libraryapplication.domain.Comment;
import ru.otus.libraryapplication.domain.Genre;

import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Data
    private class ArraySizeProjection {
        private int size;
    }

    @Override
    public boolean existByBookAuthorAndGenre(String bookName, Author author, Genre genre) {
        val aggregation = Aggregation.newAggregation(
                match(where("name").is(bookName)),
                match(where("author.id").is(Optional.ofNullable(author).map(Author::getId).orElse(null))),
                match(where("genre.id").is(Optional.ofNullable(genre).map(Genre::getId).orElse(null))),
                count().as("size"));

        val arraySizeProjection =
                mongoTemplate.aggregate(aggregation, Book.class, ArraySizeProjection.class).getUniqueMappedResult();
        return arraySizeProjection != null && arraySizeProjection.getSize() > 0;
    }

    @Override
    public void deleteWithCommentsByBookId(String id) {
        Query query = Query.query(Criteria.where("book.id").is(id));
        mongoTemplate.findAllAndRemove(query, Comment.class);

        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Book.class);
    }
}
