package vn.co.usolv.bookmanagement;

import vn.co.usolv.bookmanagement.model.Book;
import vn.co.usolv.bookmanagement.model.PageResult;
import vn.co.usolv.bookmanagement.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private ObjectMapper objectMapper;
    private Book sampleBook1;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper();

        sampleBook1 = new Book();
        sampleBook1.setId(1);
        sampleBook1.setTitle("Lập trình Java căn bản");
        sampleBook1.setAuthor("Nguyễn Văn A");
    }

    @Test
    @DisplayName("GET /api/books - Trả về dữ liệu phân trang JSON thành công")
    void getAllBooks_ShouldReturnPageResult() throws Exception {
        PageResult<Book> mockPageResult = new PageResult<>(List.of(sampleBook1), 1, 1);

        when(bookService.findPage(0, 5)).thenReturn(mockPageResult);

        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Lập trình Java căn bản")));
    }

    @Test
    @DisplayName("GET /api/books - Dùng giá trị mặc định page=0, size=5")
    void getAllBooks_ShouldUseDefaultPagingParams() throws Exception {
        PageResult<Book> mockPageResult = new PageResult<>(List.of(sampleBook1), 1, 1);

        when(bookService.findPage(0, 5)).thenReturn(mockPageResult);

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(bookService).findPage(0, 5);
    }

    @Test
    @DisplayName("GET /api/books/{id} - Trả về chi tiết sách khi tồn tại")
    void getBookById_ShouldReturnBook_WhenExists() throws Exception {
        when(bookService.getBookById(1)).thenReturn(sampleBook1);

        mockMvc.perform(get("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Lập trình Java căn bản")))
                .andExpect(jsonPath("$.author", is("Nguyễn Văn A")));

        verify(bookService).getBookById(1);
    }

    @Test
    @DisplayName("GET /api/books/{id} - Trả về 404 khi không tồn tại")
    void getBookById_ShouldReturnNotFound_WhenMissing() throws Exception {
        when(bookService.getBookById(999)).thenReturn(null);

        mockMvc.perform(get("/api/books/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(bookService).getBookById(999);
    }

    @Test
    @DisplayName("POST /api/books - Thêm sách mới thành công")
    void addBook_ShouldReturnCreatedStatus() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook1)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Thêm sách mới thành công qua REST API!"));
    }

    @Test
    @DisplayName("POST /api/books - Trả về 400 khi trùng ID")
    void addBook_ShouldReturnBadRequest_WhenDuplicateId() throws Exception {
        doThrow(new IllegalArgumentException("Mã ID sách '1' này đã tồn tại trong hệ thống!"))
                .when(bookService).addBook(any(Book.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Mã ID sách '1' này đã tồn tại trong hệ thống!")));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Cập nhật thành công và ưu tiên ID từ path")
    void updateBook_ShouldReturnOk_AndUsePathId() throws Exception {
        Book requestBody = new Book();
        requestBody.setId(999);
        requestBody.setTitle("Refactoring");
        requestBody.setAuthor("Martin Fowler");

        mockMvc.perform(put("/api/books/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cập nhật thông tin sách thành công qua REST API!"));

        verify(bookService).updateBook(argThat(book ->
                book.getId() != null
                        && book.getId() == 10
                        && "Refactoring".equals(book.getTitle())
                        && "Martin Fowler".equals(book.getAuthor())
        ));
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Xóa cuốn sách thành công")
    void deleteBook_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Xóa thành công và trả message")
    void deleteBook_ShouldReturnOkStatus_AndMessage() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Xóa sách thành công qua REST API!"));

        verify(bookService).deleteBook(1);
    }
}