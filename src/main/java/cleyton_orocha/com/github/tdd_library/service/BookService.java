package cleyton_orocha.com.github.tdd_library.service;

import org.springframework.stereotype.Service;

import cleyton_orocha.com.github.tdd_library.entity.Book;
import cleyton_orocha.com.github.tdd_library.repository.BookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

}
