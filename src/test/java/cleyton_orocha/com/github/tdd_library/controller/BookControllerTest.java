package cleyton_orocha.com.github.tdd_library.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cleyton_orocha.com.github.tdd_library.DTO.BookDTO;
import cleyton_orocha.com.github.tdd_library.entity.Book;
import cleyton_orocha.com.github.tdd_library.exception.BusinessException;
import cleyton_orocha.com.github.tdd_library.service.BookService;

@WebMvcTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @MockBean
    BookService bookService;

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("")
    public void createBookTest() throws Exception {
        // Cenário
        BookDTO dto = createBookDto();

        Book savedBook = Book.builder()
                .id(1L)
                .title("Titulo")
                .author("Autor")
                .isbn("12345678")
                .build();

        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        // Validação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                // .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar um erro de validaçao quando nao houver dados suficientes para a criaçao do livro")
    public void createInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar cadastrar um livro com isbn já utilizado por outro")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO bookDTO = createBookDto();
        String json = new ObjectMapper().writeValueAsString(bookDTO);
        String errorMessage = "Isbn já cadastrado.";
        BDDMockito.given(bookService.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(errorMessage));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(errorMessage));
    }

    private BookDTO createBookDto() {
        return BookDTO.builder()
                .title("Titulo")
                .author("Autor")
                .isbn("12345678")
                .build();
    }
}
