package vn.co.usolv.bookmanagement.controller; // Thay đổi lại đúng package của bạn

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request, Model model) {
        
        String requestURI = request.getRequestURI();

        // TRƯỜNG HỢP 1: Nếu lỗi xảy ra từ hệ thống REST API (Swagger hoặc Fetch API từ client)
        if (requestURI.startsWith("/api/")) {
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("status", HttpStatus.BAD_REQUEST.value());
            errorBody.put("error", "Bad Request");
            errorBody.put("message", ex.getMessage()); // Chuỗi thông báo lỗi "Trùng mã ID!"
            
            return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST); // Trả về cấu trúc JSON + Mã lỗi 400
        }

        // TRƯỜNG HỢP 2: Nếu lỗi xảy ra từ Form nhập liệu Thymeleaf cũ truyền thống
        model.addAttribute("errorMessage", ex.getMessage());
        return "register-form"; // Điều hướng quay lại trang HTML kèm hộp cảnh báo màu đỏ
    }
}
