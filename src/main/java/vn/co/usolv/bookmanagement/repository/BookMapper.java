package vn.co.usolv.bookmanagement.repository;

import vn.co.usolv.bookmanagement.model.Book;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * Tầng truy cập dữ liệu (Data Access Object - DAO) sử dụng MyBatis Framework.
 * Interface này chứa chữ ký các hàm để MyBatis ánh xạ với các câu lệnh SQL tương ứng trong file BookMapper.xml.
 */
@Mapper
public interface BookMapper {

    // Đảm bảo bạn đã có hàm lấy toàn bộ danh sách như thế này
    List<Book> findAll(); 

    /**
     * Lấy toàn bộ danh sách sách từ cơ sở dữ liệu sắp xếp theo ID tăng dần.
     * @return Danh sách chứa các đối tượng Book, hoặc danh sách rỗng nếu không có dữ liệu.
     */
    List<Book> selectAllBooks();

    /**
     * Tìm kiếm một cuốn sách cụ thể dựa vào mã ID.
     * @param id Mã định danh của cuốn sách cần tìm.
     * @return Đối tượng Book nếu tìm thấy, ngược lại trả về null.
     */
    Book selectBookById(Integer id);

    /**
     * Thêm mới một bản ghi sách vào cơ sở dữ liệu.
     * @param book Đối tượng Book chứa thông tin cần lưu (id, author, title, point, bookcomment).
     */
    void insertBook(Book book);

    /**
     * Cập nhật thông tin sửa đổi của một cuốn sách hiện có dựa theo ID.
     * @param book Đối tượng Book chứa các dữ liệu mới cần cập nhật.
     */
    void updateBook(Book book);

    /**
     * Xóa hoàn toàn một cuốn sách khỏi hệ thống dựa vào ID.
     * @param id Mã định danh của cuốn sách cần xóa.
     */
    void deleteBookById(Integer id);


}
