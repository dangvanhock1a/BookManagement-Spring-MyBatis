package vn.co.usolv.bookmanagement.controller;

import vn.co.usolv.bookmanagement.model.Book;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Bộ bắt lỗi tập trung cho toàn bộ ứng dụng (Spring MVC Interceptor).
 * Class này sẽ tự động "hứng" các ngoại lệ (Exceptions) quăng ra từ tầng Service hoặc Controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Bắt riêng lỗi IllegalArgumentException (Lỗi do trùng ID sách từ tầng Service gửi lên).
     * Thay vì sập ứng dụng, hệ thống sẽ đưa người dùng quay lại trang thêm mới kèm thông báo lỗi.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleDuplicateIdException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        // Lưu thông báo lỗi vào FlashAttributes để truyền an toàn qua lệnh redirect mà không lộ trên URL
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        
        // Khởi tạo lại một đối tượng Book rỗng để form không bị lỗi binding khi quay lại
        redirectAttributes.addFlashAttribute("book", new Book());
        
        // Điều hướng quay trở lại trang đăng ký sách mới
        return "redirect:/books/add";
    }

    /**
     * Bắt tất cả các loại lỗi hệ thống khác không lường trước được (Ví dụ: Mất kết nối Database PostgreSQL).
     * Điều hướng người dùng đến một trang thông báo chung.
     */
    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception ex, Model model) {
        // Đẩy thông báo lỗi chi tiết vào Model để hiển thị (chỉ dùng khi dev, khi production nên ẩn đi)
        model.addAttribute("systemError", "Hệ thống đang bận hoặc xảy ra sự cố kỹ thuật. Chi tiết: " + ex.getMessage());
        return "error/500"; // Trả về tệp giao diện lỗi: templates/error/500.html
    }
}
