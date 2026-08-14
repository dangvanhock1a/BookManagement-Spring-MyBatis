package vn.co.usolv.BookCurd.controller;

import vn.co.usolv.BookCurd.mapper.BookMapper;
import vn.co.usolv.BookCurd.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class BookController {

    private final BookMapper bookMapper;
    private static final int PAGE_SIZE = 3; 

    public BookController(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    // 1. WELCOME PAGE: Tự động di chuyển tới màn hình hiển thị danh sách
    @GetMapping("/")
    public String welcomePage() {
        return "redirect:/books";
    }

    // 2. MÀN HÌNH HIỂN THỊ DANH SÁCH
    @GetMapping("/books")
    public String listBooks(@RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "ID") String sortBy,
                            @RequestParam(defaultValue = "ASC") String order,
                            Model model) {
        int totalBooks = bookMapper.countAll();
        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        int offset = (page - 1) * PAGE_SIZE;

        if(!List.of("ID", "AUTHOR", "TITLE", "POINT", "LASTUPDATE").contains(sortBy.toUpperCase())) {
            sortBy = "ID";
        }

        List<Book> books = bookMapper.findWithPagination(sortBy, order, PAGE_SIZE, offset);
        
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        return "book-list";
    }

    // 3. MÀN HÌNH HIỂN THỊ CHI TIẾT (Nhận thêm tham số danh sách để phục vụ nút Quay lại)
    @GetMapping("/books/detail/{id}")
    public String viewDetail(@PathVariable Long id,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "ID") String sortBy,
                             @RequestParam(defaultValue = "ASC") String order,
                             Model model) {
        Book book = bookMapper.findById(id);
        model.addAttribute("book", book);
        
        // Đẩy ngược thông số danh sách ra giao diện để làm link cho nút Quay lại, Edit, Add
        model.addAttribute("listPage", page);
        model.addAttribute("listSortBy", sortBy);
        model.addAttribute("listOrder", order);
        return "book-detail";
    }

    // 4. MÀN HÌNH TẠO MỚI (Nhận tham số để nút Back quay về đúng trạng thái danh sách/chi tiết)
    @GetMapping("/books/add")
    public String showAddForm(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "ID") String sortBy,
                              @RequestParam(defaultValue = "ASC") String order,
                              Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("listPage", page);
        model.addAttribute("listSortBy", sortBy);
        model.addAttribute("listOrder", order);
        return "book-add";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam int page,
                           @RequestParam String sortBy,
                           @RequestParam String order,
                           RedirectAttributes redirectAttributes) {
        try {
            bookMapper.insert(book);
            redirectAttributes.addFlashAttribute("successMessage", "Hoàn tất tạo mới");
            // Sau khi tạo mới, di chuyển tới màn hình chi tiết kèm tham số danh sách
            return "redirect:/books/detail/" + book.getId() + "?page=" + page + "&sortBy=" + sortBy + "&order=" + order;
        } catch (Exception e) {
            return "redirect:/books/add?error=duplicate&page=" + page + "&sortBy=" + sortBy + "&order=" + order;
        }
    }

    // 5. MÀN HÌNH EDIT
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @RequestParam int page,
                               @RequestParam String sortBy,
                               @RequestParam String order,
                               Model model) {
        Book book = bookMapper.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("listPage", page);
        model.addAttribute("listSortBy", sortBy);
        model.addAttribute("listOrder", order);
        return "book-edit";
    }

    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book,
                             @RequestParam int page,
                             @RequestParam String sortBy,
                             @RequestParam String order,
                             RedirectAttributes redirectAttributes) {
        bookMapper.update(book);
        redirectAttributes.addFlashAttribute("successMessage", "Hoàn tất update.");
        return "redirect:/books/detail/" + book.getId() + "?page=" + page + "&sortBy=" + sortBy + "&order=" + order;
    }

    // 6. THỰC HIỆN DELETE VÀ ĐIỀU HƯỚNG SANG MÀN HÌNH HOÀN TẤT DELETE
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookMapper.deleteById(id);
        redirectAttributes.addFlashAttribute("deletedId", id);
        return "redirect:/books/delete-success";
    }

    @GetMapping("/books/delete-success")
    public String deleteSuccessPage() {
        return "book-delete-success";
    }
}

