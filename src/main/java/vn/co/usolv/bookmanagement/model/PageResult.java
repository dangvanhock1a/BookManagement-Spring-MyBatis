package vn.co.usolv.bookmanagement.model;

import java.util.List;

public class PageResult<T> {
    private List<T> content;      // Danh sách dữ liệu (Sách) của trang hiện tại
    private int totalPages;       // Tổng số trang
    private long totalElements;   // Tổng số bản ghi trong DB

    public PageResult(List<T> content, int totalPages, long totalElements) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    // Getter và Setter (Bắt buộc phải có để Swagger và Jackson chuyển sang JSON)
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
}
