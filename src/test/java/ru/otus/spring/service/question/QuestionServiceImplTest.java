package ru.otus.spring.service.question;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.dao.question.QuestionDao;
import ru.otus.spring.dao.question.QuestionDaoInFile;

import static ru.otus.spring.QuestionTestData.QUESTION_LIST;

class QuestionServiceImplTest {
    @Test
    void getAll() {
        QuestionDao questionDaoInFile = Mockito.mock(QuestionDaoInFile.class);
        Mockito.when(questionDaoInFile.getAll()).thenReturn(QUESTION_LIST);
        QuestionService service = new QuestionServiceImpl(questionDaoInFile);
        Assertions.assertIterableEquals(QUESTION_LIST, service.getAll());
    }
}