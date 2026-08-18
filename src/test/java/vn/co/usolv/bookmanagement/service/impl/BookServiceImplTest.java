package vn.co.usolv.bookmanagement.service.impl;

import vn.co.usolv.bookmanagement.model.Book;
import vn.co.usolv.bookmanagement.model.PageResult;
import vn.co.usolv.bookmanagement.repository.BookMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(1);
        sampleBook.setTitle("Clean Code");
        sampleBook.setAuthor("Robert C. Martin");
        sampleBook.setPoint(10);
        sampleBook.setBookcomment("A must-read.");
    }

    @Test
    @DisplayName("getAllBooks: Trả về danh sách sách từ mapper")
    void getAllBooks_ShouldReturnBooksFromMapper() {
        List<Book> expected = List.of(sampleBook);
        when(bookMapper.selectAllBooks()).thenReturn(expected);

        List<Book> actual = bookService.getAllBooks();

        assertIterableEquals(expected, actual);
        verify(bookMapper, times(1)).selectAllBooks();
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("getBookById: Trả về đúng sách theo ID")
    void getBookById_ShouldReturnBookFromMapper() {
        when(bookMapper.selectBookById(1)).thenReturn(sampleBook);

        Book actual = bookService.getBookById(1);

        assertSame(sampleBook, actual);
        verify(bookMapper, times(1)).selectBookById(1);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("addBook: Ném lỗi khi ID đã tồn tại và không gọi insert")
    void addBook_ShouldThrowExceptionWhenBookIdAlreadyExists() {
        when(bookMapper.selectBookById(sampleBook.getId())).thenReturn(sampleBook);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bookService.addBook(sampleBook));

        assertEquals("Mã ID sách '1' này đã tồn tại trong hệ thống!", ex.getMessage());
        verify(bookMapper, times(1)).selectBookById(sampleBook.getId());
        verify(bookMapper, never()).insertBook(sampleBook);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("addBook: Thêm mới thành công khi ID chưa tồn tại")
    void addBook_ShouldInsertWhenBookIdDoesNotExist() {
        when(bookMapper.selectBookById(sampleBook.getId())).thenReturn(null);

        bookService.addBook(sampleBook);

        verify(bookMapper, times(1)).selectBookById(sampleBook.getId());
        verify(bookMapper, times(1)).insertBook(sampleBook);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("updateBook: Ủy quyền cập nhật xuống mapper")
    void updateBook_ShouldDelegateToMapper() {
        bookService.updateBook(sampleBook);

        verify(bookMapper, times(1)).updateBook(sampleBook);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("deleteBook: Ủy quyền xóa xuống mapper")
    void deleteBook_ShouldDelegateToMapper() {
        bookService.deleteBook(1);

        verify(bookMapper, times(1)).deleteBookById(1);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("findPage: Trả về PageResult đúng dữ liệu và thông tin phân trang")
    void findPage_ShouldReturnPageResultWithMappedValues() {
        List<Book> books = List.of(sampleBook);
        when(bookMapper.selectAllBooks()).thenReturn(books);

        PageResult<Book> result = bookService.findPage(0, 5);

        assertNotNull(result);
        assertIterableEquals(books, result.getContent());
        assertEquals(1, result.getTotalPages());
        assertEquals(1L, result.getTotalElements());
        verify(bookMapper, times(1)).selectAllBooks();
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("findPage: Trả về phân trang rỗng khi mapper không có dữ liệu")
    void findPage_ShouldReturnEmptyPageWhenNoBooks() {
        when(bookMapper.selectAllBooks()).thenReturn(Collections.emptyList());

        PageResult<Book> result = bookService.findPage(0, 5);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        verify(bookMapper, times(1)).selectAllBooks();
        verifyNoMoreInteractions(bookMapper);
    }
}
