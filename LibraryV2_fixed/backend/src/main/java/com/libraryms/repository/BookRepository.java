package com.libraryms.repository;
import com.libraryms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List; import java.util.Optional;
@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByIsbn(String isbn);
    Optional<Book> findByBarcodeValue(String barcodeValue);
    List<Book> findByCategory(String category);
    List<Book> findByAvailableQuantityGreaterThan(int qty);
    boolean existsByIsbn(String isbn);
    boolean existsByBarcodeValue(String barcodeValue);
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',:kw,'%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%',:kw,'%')) OR LOWER(b.isbn) LIKE LOWER(CONCAT('%',:kw,'%')) OR LOWER(b.barcodeValue) LIKE LOWER(CONCAT('%',:kw,'%'))")
    List<Book> searchBooks(@Param("kw") String keyword);
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.category IS NOT NULL ORDER BY b.category")
    List<String> findAllCategories();
}
