package cleyton_orocha.com.github.tdd_library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cleyton_orocha.com.github.tdd_library.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
    
}
