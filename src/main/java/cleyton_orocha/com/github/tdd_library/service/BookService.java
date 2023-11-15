package cleyton_orocha.com.github.tdd_library.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import cleyton_orocha.com.github.tdd_library.DTO.BookDTO;
import cleyton_orocha.com.github.tdd_library.entity.Book;
import cleyton_orocha.com.github.tdd_library.repository.BookRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookDTO save(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Book bookSaved = bookRepository.save(book);
        BookDTO bookDTOResult = modelMapper.map(bookSaved, BookDTO.class);
        return bookDTOResult;
    }

}
