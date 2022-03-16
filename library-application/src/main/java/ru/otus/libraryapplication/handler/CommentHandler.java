package ru.otus.libraryapplication.handler;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.libraryapplication.domain.Comment;
import ru.otus.libraryapplication.dto.CommentDto;
import ru.otus.libraryapplication.repositories.book.BookRepository;
import ru.otus.libraryapplication.repositories.comment.CommentRepository;
import ru.otus.libraryapplication.service.string.StringService;
import ru.otus.libraryapplication.util.exeption.ApplicationException;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class CommentHandler {
    private final CommentRepository commentRepository;
    private final StringService stringService;

    public Mono<ServerResponse> delete(ServerRequest request) {
        return Mono.just(request.pathVariable("id"))
                .filter(StringUtils::isNotEmpty)
                .flatMap(id -> commentRepository.deleteById(id)
                        .flatMap(result -> ok().build()));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(Comment.class)
                .flatMap(comment -> {
                    if (stringService.verifyNotBlank(comment.getComment())) {
                        comment.setId(id);

                        return commentRepository.save(comment)
                                .map(CommentDto::toDto)
                                .flatMap(commentDto -> ok().body(fromValue(commentDto)));
                    } else {
                        throw new ApplicationException("Invalid comment");
                    }
                });
    }

    public Mono<ServerResponse> create(ServerRequest request) {

        return request.bodyToMono(Comment.class)
                .map(comment -> {
                    String name = stringService.beautifyStringName(comment.getComment());
                    comment.setComment(stringService.beautifyStringName(name));

                    return comment;
                })
                .flatMap(comment -> {
                    if (stringService.verifyNotBlank(comment.getComment())) {

                        return commentRepository.save(comment)
                                .map(CommentDto::toDto)
                                .flatMap(commentDto -> ok().body(fromValue(commentDto)));
                    } else {
                        throw new ApplicationException("Invalid comment");
                    }
                });
    }
}