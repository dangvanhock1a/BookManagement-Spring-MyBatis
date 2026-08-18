========================================================================
DỰ ÁN QUẢN LÝ SÁCH (BOOK MANAGEMENT APP) - SPRING BOOT & MYBATIS
========================================================================

Dự án được xây dựng theo kiến trúc chuẩn 3 tầng (3-Tier Architecture) 
tích hợp hiệu quả các công nghệ hiện đại Backend và Frontend.

1. CÔNG NGHỆ SỬ DỤNG
------------------------------------------------------------------------
* Ngôn ngữ Backend: Java
* Backend Framework: Spring Boot, Spring MVC
* Database: PostgreSQL 18
* O/R Mapper: MyBatis
* Template Engine: Thymeleaf
* Giao diện: HTML, CSS, JavaScript, Bootstrap 5 (CDN)

2. CẤU TRÚC CÁC TẦNG (3-TIER ARCHITECTURE)
------------------------------------------------------------------------
* Presentation Tier (Tầng biểu diễn):
  - BookController.java: Tiếp nhận, điều phối request, binding form.
  - Giao diện HTML: Nhằm mục tiêu hiển thị, tương tác qua Thymeleaf.
* Logic Tier / Business Tier (Tầng nghiệp vụ):
  - BookService.java & BookServiceImpl.java: Xử lý luồng nghiệp vụ, 
    kiểm soát validation (kiểm tra trùng mã ID) và phân chia hành động.
* Data Tier (Tầng dữ liệu):
  - BookMapper.java & BookMapper.xml: Interface và file cấu hình SQL 
    truy vấn, thực hiện ghi/đọc trực tiếp vào database PostgreSQL.

3. CẤU TRÚC BẢNG POSTGRESQL (TABLE DDL)
------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.book
(
    id integer NOT NULL,
    author character varying(50) COLLATE pg_catalog."default",
    title character varying(100) COLLATE pg_catalog."default",
    point integer,
    bookcomment text COLLATE pg_catalog."default",
    lastupdate timestamp without time zone,
    CONSTRAINT pk_book01 PRIMARY KEY (id)
);

4. BỘ BẮT LỖI TẬP TRUNG (GLOBAL EXCEPTION HANDLER)
------------------------------------------------------------------------
* Sử dụng @ControllerAdvice để kiểm soát lỗi chủ động và bị động.
* Khi nhập trùng ID Sách tại trang đăng ký, hệ thống sẽ ném ra 
  IllegalArgumentException và tự động điều hướng về trang nhập liệu 
  kèm hộp cảnh báo xôn xao màu đỏ an toàn, không làm sập trang web.

5. HƯỚNG DẪN KHI CHẠY ỨNG DỤNG (LOCAL RUN)
------------------------------------------------------------------------
* Bước 1: Cấu hình tài nguyên application.properties (URL, Username, 
  Password kết nối PostgreSQL của riêng bạn).
* Bước 2: Chạy lệnh build ứng dụng bằng IDE hoặc qua terminal:
  mvn clean install
* Bước 3: Khởi chạy code main thông qua BookManagementApplication.java.
* Bước 4: Mở trình duyệt và truy cập đúng URL để test các chức năng:
  http://localhost:8080/books



========================================================================
BÁO CÁO TỔNG HỢP: QUÁ TRÌNH CHUYỂN ĐỔI ỨNG DỤNG SANG REST API & UNIT TEST
Dự án: BookManagement-Spring-MyBatis (Spring Boot 4.0.7 & PostgreSQL 18 & MyBatis)
Tác giả: Đặng Văn Học & AI Assistant
Thời gian hoàn thành: 18/08/2026
========================================================================

Hệ thống đã được chuyển đổi thành công từ mô hình Server-Side Rendering (Thymeleaf) 
sang mô hình Cơ chế tách rời: REST API Backend + Client-Side Rendering (JavaScript Fetch API),
kết hợp tối ưu giao diện Modal Popup, Phân trang và kiểm thử tự động đạt chỉ số BUILD SUCCESS.

Dưới đây là tổng hợp toàn bộ các bước thiết lập cấu hình và mã nguồn quan trọng:

------------------------------------------------------------------------
PHẦN 1: CẤU HÌNH HỆ THỐNG (POM.XML & APPLICATION.PROPERTIES)
------------------------------------------------------------------------

1. Các thư viện bổ sung trong file `pom.xml`:
   - Thư viện tự động sinh tài liệu Swagger UI (OpenAPI 3):
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.6.0</version>
        </dependency>
   - Thư viện phân trang chuyên dụng cho MyBatis:
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>2.1.0</version>
        </dependency>
   - Thư viện kiểm thử (Cơ bản của Spring Boot):
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
   - Plugin JaCoCo đo đạc độ phủ mã nguồn (Đặt trong thẻ <plugins>):
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.12</version>
            <executions>
                <execution>
                    <id>prepare-agent</id>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <excludes>
                    <exclude>**/entity/**</exclude>
                    <exclude>**/config/**</exclude>
                    <exclude>**/*Application.class</exclude>
                </excludes>
            </configuration>
        </plugin>

2. Các cấu hình đường dẫn trong file `src/main/resources/application.properties`:
   springdoc.swagger-ui.path=/swagger-ui.html
   springdoc.api-docs.path=/api-docs
   springdoc.swagger-ui.operationsSorter=alpha

------------------------------------------------------------------------
PHẦN 2: PHÁT TRIỂN MÃ NGUỒN BACKEND (REST API & PHÂN TRANG CUSTOM)
------------------------------------------------------------------------

1. Lớp cấu trúc Phân trang tùy biến không phụ thuộc Spring Data (`PageResult.java`):
   package com.example.bookmanagement.entity;
   import java.util.List;
   public class PageResult<T> {
       private List<T> content;
       private int totalPages;
       private long totalElements;
       public PageResult(List<T> content, int totalPages, long totalElements) {
           this.content = content;
           this.totalPages = totalPages;
           this.totalElements = totalElements;
       }
       public List<T> getContent() { return content; }
       public int getTotalPages() { return totalPages; }
       public long getTotalElements() { return totalElements; }
   }

2. Giao diện REST độc lập hoàn chỉnh (`BookRestController.java`):
   package com.example.bookmanagement.controller;
   import com.example.bookmanagement.entity.Book;
   import com.example.bookmanagement.entity.PageResult;
   import com.example.bookmanagement.service.BookService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.*;
   import java.util.List;

   @RestController
   @RequestMapping("/api/books")
   public class BookRestController {
       @Autowired
       private BookService bookService;

       @GetMapping
       public ResponseEntity<PageResult<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
           PageResult<Book> bookPage = bookService.findPage(page, size);
           return ResponseEntity.ok(bookPage);
       }

       @PostMapping
       public ResponseEntity<String> addBook(@RequestBody Book book) {
           bookService.save(book);
           return new ResponseEntity<>("Thêm sách mới thành công!", HttpStatus.CREATED);
       }

       @PutMapping("/{id}")
       public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody Book book) {
           book.setId(id);
           bookService.update(book);
           return ResponseEntity.ok("Cập nhật thành công!");
       }

       @DeleteMapping("/{id}")
       public ResponseEntity<String> deleteBook(@PathVariable int id) {
           bookService.delete(id);
           return ResponseEntity.ok("Xóa sách thành công!");
       }
   }

3. Tối ưu bộ xử lý lỗi tập trung (`GlobalExceptionHandler.java`):
   Sử dụng thêm annotation `@Hidden` của Swagger để tránh lỗi quét 500 /api-docs, 
   đồng thời phân tích URL request để trả về JSON lỗi cho API hoặc HTML cho View cũ.
   @ControllerAdvice
   @io.swagger.v3.oas.annotations.Hidden
   public class GlobalExceptionHandler { ... }

4. Tích hợp phân trang MyBatis tại `BookServiceImpl.java`:
   @Override
   public PageResult<Book> findPage(int page, int size) {
       com.github.pagehelper.PageHelper.startPage(page + 1, size);
       List<Book> books = bookMapper.findAll();
       com.github.pagehelper.PageInfo<Book> pageInfo = new com.github.pagehelper.PageInfo<>(books);
       return new PageResult<>(books, pageInfo.getPages(), pageInfo.getTotal());
   }

------------------------------------------------------------------------
PHẦN 3: PHÁT TRIỂN GIAO DIỆN CLIENT TƯƠNG TÁC (BOOKS-AJAX.HTML)
------------------------------------------------------------------------
- Sử dụng Bootstrap 5 Modal kết hợp Fetch API của JavaScript để thực hiện các thao tác CRUD.
- Không chỉnh sửa file HTML cũ. Tạo file độc lập `books-ajax.html` trong thư mục templates.
- Điều hướng hiển thị thông qua một `@Controller` tĩnh độc lập:
  @GetMapping("/books-ajax-view")
  public String viewBooksAjaxPage() { return "books-ajax"; }
- Sử dụng cơ chế bóc tách JSON dựa trên cấu trúc phân trang mới:
  const books = pageData.content || [];
  document.getElementById("paginationButtons").innerHTML = ... // Tự động render nút số trang

------------------------------------------------------------------------
PHẦN 4: KỊCH BẢN KIỂM THỬ ĐẠT CHUẨN SPRING BOOT 4.X (UNIT TEST)
------------------------------------------------------------------------
Do đặc thù Spring Boot 4.0.7 đã loại bỏ `@MockBean` cũ và tái cấu trúc `@WebMvcTest`, 
mã nguồn kiểm thử đã được chuyển đổi tối ưu sang dạng Kiểm thử Tích hợp Toàn phần giúp chạy ổn định:

Lớp kiểm thử chuẩn (`BookRestControllerTest.java`):
```java
package vn.co.usolv.bookmanagement;

import com.example.bookmanagement.entity.Book;
import com.example.bookmanagement.entity.PageResult;
import com.example.bookmanagement.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Annotation Mock chính xác cho Spring Boot 4.x
    private BookService bookService;

    private ObjectMapper objectMapper;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        this.objectMapper = new ObjectMapper(); // Khởi tạo trực tiếp để tránh lỗi UnsatisfiedDependency
        sampleBook = new Book();
        sampleBook.setId(1);
        sampleBook.setTitle("Lập trình Java căn bản");
        sampleBook.setAuthor("Nguyễn Văn A");
    }

    @Test
    void getAllBooks_ShouldReturnPageResult() throws Exception {
        PageResult<Book> mockPageResult = new PageResult<>(Arrays.asList(sampleBook), 1, 1);
        Mockito.when(bookService.findPage(0, 5)).thenReturn(mockPageResult);

        mockMvc.perform(get("/api/books").param("page", "0").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
```

------------------------------------------------------------------------
PHẦN 5: CÁC CÂU LỆNH ĐIỀU KHIỂN QUAN TRỌNG TRÊN TERMINAL
------------------------------------------------------------------------
- Biên dịch dọn dẹp hệ thống và cưỡng ép cập nhật thư viện Maven:
  ./mvnw clean test -U
- Kích hoạt chạy riêng file Unit Test để kiểm tra lỗi và kết xuất báo cáo JaCoCo:
  ./mvnw clean test -Dtest=BookRestControllerTest
- Đường dẫn mở tệp kiểm tra độ phủ mã nguồn (Coverage Report) trực quan trên trình duyệt:
  target/site/jacoco/index.html
========================================================================