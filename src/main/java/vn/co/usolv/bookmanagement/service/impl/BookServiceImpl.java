package vn.co.usolv.bookmanagement.service.impl;

import vn.co.usolv.bookmanagement.model.Book;
import vn.co.usolv.bookmanagement.model.PageResult;
import vn.co.usolv.bookmanagement.repository.BookMapper;
import vn.co.usolv.bookmanagement.service.BookService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lớp triển khai thực tế (Implementation) của BookService.
 * Chứa logic xử lý nghiệp vụ chính của ứng dụng và quản lý các giao dịch (Transactions).
 */
@Service
public class BookServiceImpl implements BookService {

    // Tiêm (Inject) bean tầng dữ liệu thông qua cơ chế Constructor Injection (khuyến nghị của Spring)
    private final BookMapper bookMapper;

    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    /**
     * Thực hiện nghiệp vụ lấy tất cả sách.
     * Ở đây có thể tích hợp thêm các logic như lọc dữ liệu, phân trang nếu cần thiết.
     */
    @Override
    public List<Book> getAllBooks() {
        return bookMapper.selectAllBooks();
    }

    /** Thao tác lấy chi tiết sách thông qua Mapper */
    @Override
    public Book getBookById(Integer id) {
        return bookMapper.selectBookById(id);
    }

    /**
     * Nghiệp vụ thêm sách mới.
     * Thực hiện kiểm tra tính hợp lệ dữ liệu: Không được phép trùng ID có sẵn trong hệ thống.
     * Sử dụng @Transactional để tự động hủy tác vụ (Rollback) nếu quá trình ghi DB phát sinh lỗi hệ thống.
     */
    @Override
    @Transactional
    public void addBook(Book book) {
        // Kiểm tra xem ID người dùng nhập vào đã tồn tại trong Database chưa
        if (bookMapper.selectBookById(book.getId()) != null) {
            throw new IllegalArgumentException("Mã ID sách '" + book.getId() + "' này đã tồn tại trong hệ thống!");
        }
        // Gọi xuống tầng dữ liệu để thực hiện câu lệnh SQL INSERT
        bookMapper.insertBook(book);
    }

    /** Nghiệp vụ cập nhật sách với cơ chế tự đóng/ngắt Transaction an toàn */
    @Override
    @Transactional
    public void updateBook(Book book) {
        bookMapper.updateBook(book);
    }

    /** Nghiệp vụ xóa sách dựa trên mã định danh ID */
    @Override
    @Transactional
    public void deleteBook(Integer id) {
        bookMapper.deleteBookById(id);
    }

    // 🌟 ĐỒNG BỘ CHÍNH XÁC THAM SỐ (int page, int size) VÀ KIỂU TRẢ VỀ PageResult
    @Override
    public PageResult<Book> findPage(int page, int size) {
        // 1. Kích hoạt phân trang của PageHelper (Tính từ trang 1 nên cần + 1)
        com.github.pagehelper.PageHelper.startPage(page + 1, size);

        // 2. Gọi hàm lấy danh sách từ MyBatis
        List<Book> books = bookMapper.selectAllBooks();

        // 3. Sử dụng PageInfo để bóc tách lấy tổng số dòng dữ liệu
        com.github.pagehelper.PageInfo<Book> pageInfo = new com.github.pagehelper.PageInfo<>(books);

        // 4. Trả về đối tượng cấu trúc phân trang tùy biến của bạn
        return new PageResult<>(books, pageInfo.getPages(), pageInfo.getTotal());
    }
}
