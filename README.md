DỰ ÁN QUẢN LÝ SÁCH (BOOK MANAGEMENT APP) - SPRING BOOT & MYBATIS

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
