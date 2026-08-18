package vn.co.usolv.bookmanagement.controller;

import vn.co.usolv.bookmanagement.model.Book;
import vn.co.usolv.bookmanagement.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Tầng điều khiển (Spring MVC Controller) xử lý các HTTP Requests từ trình duyệt người dùng.
 * Điều phối dữ liệu thông qua tầng Service và chọn View Thymeleaf tương ứng để render giao diện HTML.
 */
@Controller
@RequestMapping("/books") // Tất cả các đường dẫn trong controller này đều bắt đầu bằng /books
public class BookController {

    // Giao tiếp trực tiếp với tầng Business Logic (Không gọi trực tiếp xuống Mapper theo quy tắc 3 tầng)
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * 1. GET: /books
     * Hiển thị danh sách toàn bộ sách hiện có.
     * Map dữ liệu vào giao diện: book-list.html
     */
    @GetMapping
    public String listBooks(Model model) {
        // Đẩy danh sách lấy từ Service vào thuộc tính "books" của Spring Model để Thymeleaf đọc dữ liệu dạng vòng lặp
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list"; // Trả về tệp templates/book-list.html
    }

    /**
     * 2. GET: /books/add
     * Hiển thị biểu mẫu (Form) để người dùng điền thông tin thêm sách mới.
     * Map dữ liệu vào giao diện: book-add.html
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Khởi tạo một đối tượng Book rỗng để liên kết form (Form-binding) với thuộc tính th:object phía Frontend
        model.addAttribute("book", new Book());
        return "book-add"; // Trả về tệp templates/book-add.html
    }

    /**
     * POST: /books/add
     * Xử lý nhận dữ liệu gửi lên (submit) từ Form thêm mới sách.
     */
    @PostMapping("/add")
    public String saveBook(@ModelAttribute("book") Book book) {
        // Gửi đối tượng nhận từ form xuống tầng nghiệp vụ xử lý
        bookService.addBook(book);
        return "redirect:/books"; // Điều hướng (Redirect) trình duyệt quay lại trang danh sách chính
    }

    /**
     * 3. GET: /books/detail/{id}
     * Xem thông tin chi tiết của một cuốn sách cụ thể bằng tham số ID trên URL (Path Variable).
     * Map dữ liệu vào giao diện: book-detail.html
     */
    @GetMapping("/detail/{id}")
    public String viewBookDetail(@PathVariable("id") Integer id, Model model) {
        // Lấy thông tin sách theo id và nạp vào model để hiển thị lên trang chi tiết
        model.addAttribute("book", bookService.getBookById(id));
        return "book-detail"; // Trả về tệp templates/book-detail.html
    }

    /**
     * 4. GET: /books/edit/{id}
     * Hiển thị biểu mẫu chỉnh sửa thông tin sách dựa trên dữ liệu cũ tìm được từ ID.
     * Map dữ liệu vào giao diện: book-edit.html
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        // Đổ dữ liệu hiện tại của sách vào form chỉnh sửa để người dùng xem lại trước khi sửa
        model.addAttribute("book", bookService.getBookById(id));
        return "book-edit"; // Trả về tệp templates/book-edit.html
    }

    /**
     * POST: /books/edit
     * Xử lý dữ liệu gửi lên sau khi người dùng sửa đổi thông tin sách từ biểu mẫu.
     */
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute("book") Book book) {
        // Thực hiện cập nhật dữ liệu thông qua Service
        bookService.updateBook(book);
        return "redirect:/books"; // Điều hướng quay lại danh sách
    }

    /**
     * 5. POST: /books/delete/{id}
     * Xử lý hành động xóa sách dựa trên ID khi người dùng click nút Xóa.
     * Chuyển hướng sang giao diện thông báo xóa thành công: book-delete-success.html
     */
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        // Thực hiện lệnh xóa bản ghi ở DB qua Service
        bookService.deleteBook(id);
        return "book-delete-success"; // Trả về trực tiếp tệp templates/book-delete-success.html thay vì redirect
    }

    // Endpoint phục vụ riêng cho việc hiển thị trang HTML AJAX mới của bạn
    @GetMapping("/books-ajax-view")
    public String viewBooksAjaxPage() {
        return "books-ajax"; // Trả về đúng tên file "books-ajax.html" nằm trong thư mục templates
    }

}
