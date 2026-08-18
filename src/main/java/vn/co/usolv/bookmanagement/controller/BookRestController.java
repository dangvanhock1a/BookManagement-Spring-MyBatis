package vn.co.usolv.bookmanagement.controller; 

import vn.co.usolv.bookmanagement.model.Book; 
import vn.co.usolv.bookmanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.co.usolv.bookmanagement.model.PageResult;


@RestController
@RequestMapping("/api/books") // Đường dẫn gốc cho toàn bộ REST API về sách
@Tag(name = "Book REST Controller", description = "Hệ thống REST API độc lập phục vụ kiểm thử Swagger và gọi AJAX/Fetch API")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<PageResult<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        // Truyền thẳng 2 tham số số nguyên vào hàm Service mới sửa
        PageResult<Book> bookPage = bookService.findPage(page, size);
        return ResponseEntity.ok(bookPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết một cuốn sách theo ID (JSON)")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build(); // Trả về HTTP Status 404 Not Found nếu không tìm thấy ID
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping
    @Operation(summary = "Thêm mới một cuốn sách (Nhận dữ liệu JSON)", description = "Truyền vào một chuỗi Object JSON chứa thông tin sách. ID sách không được trùng lặp.")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        // Annotation @RequestBody bắt buộc phải có để Spring ánh xạ dữ liệu JSON từ Swagger gửi lên thành Object Java
        bookService.addBook(book);
        return new ResponseEntity<>("Thêm sách mới thành công qua REST API!", HttpStatus.CREATED); // Trả về HTTP Status 201 Created
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin sách theo ID (Nhận dữ liệu JSON)")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateBook(book);
        return ResponseEntity.ok("Cập nhật thông tin sách thành công qua REST API!");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một cuốn sách khỏi hệ thống theo ID")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Xóa sách thành công qua REST API!");
    }
}
