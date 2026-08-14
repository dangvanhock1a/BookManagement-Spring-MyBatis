package vn.co.usolv.BookCurd.mapper;
import vn.co.usolv.BookCurd.model.Book;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BookMapper {

    // Lấy danh sách phân trang và sort động (Không lấy cột BOOKCOMMENT)
    @Select("SELECT ID, AUTHOR, TITLE, POINT, LASTUPDATE FROM BOOK " +
            "ORDER BY ${sortBy} ${order} " +
            "LIMIT #{limit} OFFSET #{offset}")
    List<Book> findWithPagination(@Param("sortBy") String sortBy, 
                                  @Param("order") String order, 
                                  @Param("limit") int limit, 
                                  @Param("offset") int offset);

    @Select("SELECT COUNT(*) FROM BOOK ")
    int countAll();

    // Hiển thị chi tiết (Lấy đầy đủ bao gồm cả BOOKCOMMENT)
    @Select("SELECT * FROM BOOK WHERE ID = #{id}")
    Book findById(Long id);

    @Insert("INSERT INTO BOOK (AUTHOR, TITLE, POINT, BOOKCOMMENT, LASTUPDATE) " +
            "VALUES(#{author}, #{title}, #{point}, #{bookcomment}, CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Book book);

    @Update("UPDATE BOOK SET AUTHOR=#{author}, TITLE=#{title}, POINT=#{point}, " +
            "BOOKCOMMENT=#{bookcomment}, LASTUPDATE=CURRENT_TIMESTAMP WHERE ID=#{id}")
    void update(Book book);

    @Delete("DELETE FROM BOOK WHERE ID = #{id}")
    void deleteById(Long id);
}
