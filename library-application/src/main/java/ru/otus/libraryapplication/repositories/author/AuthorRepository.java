package ru.otus.libraryapplication.repositories.author;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.libraryapplication.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, Long>, AuthorRepositoryCustom {
    Flux<Author> findAll();

    Mono<Author> findById(String Id);

    Mono<Author> findByName(String name);

    Mono<Author> save(Author author);
}