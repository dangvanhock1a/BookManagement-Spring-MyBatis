package vn.co.usolv.bookmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Đối tượng Thực thể (Entity) đại diện cho bảng public.book trong PostgreSQL.
 * Bao gồm đầy đủ các hàm Getter, Setter, toString, equals và hashCode.
 */
public class Book {

    /** Mã định danh duy nhất của sách (Primary Key trong Database) */
    private Integer id;

    /** Tên tác giả (Ánh xạ từ cột varchar(50)) */
    private String author;

    /** Tiêu đề / Tên sách (Ánh xạ từ cột varchar(100)) */
    private String title;

    /** Điểm số hoặc đánh giá sách (Ánh xạ từ cột integer) */
    private Integer point;

    /** Lời bình luận, nhận xét về cuốn sách (Ánh xạ từ cột text) */
    private String bookcomment;

    /** Thời gian cập nhật dữ liệu cuối cùng (Ánh xạ từ cột timestamp) */
    private LocalDateTime lastupdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getBookcomment() {
        return bookcomment;
    }

    public void setBookcomment(String bookcomment) {
        this.bookcomment = bookcomment;
    }

    public LocalDateTime getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(LocalDateTime lastupdate) {
        this.lastupdate = lastupdate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", point=" + point +
                ", bookcomment='" + bookcomment + '\'' +
                ", lastupdate=" + lastupdate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id)
                && Objects.equals(author, book.author)
                && Objects.equals(title, book.title)
                && Objects.equals(point, book.point)
                && Objects.equals(bookcomment, book.bookcomment)
                && Objects.equals(lastupdate, book.lastupdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, point, bookcomment, lastupdate);
    }
}
