package vn.co.usolv.BookCurd.model;
import java.time.LocalDateTime;

public class Book {
    private Long id;
    private String author;
    private String title;
    private Integer point;
    private String bookcomment;
    private LocalDateTime lastupdate;

    // Tiến hành tạo các hàm Getter và Setter cho các trường trên
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getPoint() { return point; }
    public void setPoint(Integer point) { this.point = point; }
    public String getBookcomment() { return bookcomment; }
    public void setBookcomment(String bookcomment) { this.bookcomment = bookcomment; }
    public LocalDateTime getLastupdate() { return lastupdate; }
    public void setLastupdate(LocalDateTime lastupdate) { this.lastupdate = lastupdate; }
}
