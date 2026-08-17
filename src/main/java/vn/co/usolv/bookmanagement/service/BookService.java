package vn.co.usolv.bookmanagement.service;

import vn.co.usolv.bookmanagement.model.Book;
import java.util.List;

/**
 * Giao diện (Interface) định nghĩa các dịch vụ xử lý nghiệp vụ quản lý sách.
 * Đóng vai trò là cầu nối lỏng lẻo (loose coupling) giữa Tầng điều khiển (Controller) và Tầng dữ liệu (Repository).
 */
public interface BookService {

    /** Lấy danh sách toàn bộ sách */
    List<Book> getAllBooks();

    /** Tìm chi tiết một cuốn sách bằng ID */
    Book getBookById(Integer id);

    /** Xử lý nghiệp vụ thêm sách mới (kiểm tra trùng lặp) */
    void addBook(Book book);

    /** Xử lý nghiệp vụ cập nhật thông tin sách */
    void updateBook(Book book);

    /** Xử lý nghiệp vụ xóa sách */
    void deleteBook(Integer id);
}
