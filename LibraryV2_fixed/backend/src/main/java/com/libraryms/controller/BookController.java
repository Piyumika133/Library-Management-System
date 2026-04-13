package com.libraryms.controller;
import com.libraryms.model.Book;
import com.libraryms.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/books") @CrossOrigin(origins="*")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bs){ bookService=bs; }
    @GetMapping public ResponseEntity<List<Book>> getAll(){ return ResponseEntity.ok(bookService.getAllBooks()); }
    @GetMapping("/{id}") public ResponseEntity<Book> getById(@PathVariable Long id){ return ResponseEntity.ok(bookService.getBookById(id)); }
    @GetMapping("/isbn/{isbn}") public ResponseEntity<Book> getByIsbn(@PathVariable String isbn){ return ResponseEntity.ok(bookService.getBookByIsbn(isbn)); }
    @GetMapping("/barcode/{barcode}") public ResponseEntity<Book> getByBarcode(@PathVariable String barcode){ return ResponseEntity.ok(bookService.getBookByBarcode(barcode)); }
    @GetMapping("/search") public ResponseEntity<List<Book>> search(@RequestParam String keyword){ return ResponseEntity.ok(bookService.searchBooks(keyword)); }
    @GetMapping("/category/{cat}") public ResponseEntity<List<Book>> byCategory(@PathVariable String cat){ return ResponseEntity.ok(bookService.getByCategory(cat)); }
    @GetMapping("/available") public ResponseEntity<List<Book>> available(){ return ResponseEntity.ok(bookService.getAvailableBooks()); }
    @GetMapping("/categories") public ResponseEntity<List<String>> categories(){ return ResponseEntity.ok(bookService.getAllCategories()); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Book> add(@Valid @RequestBody Book b){ return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(b)); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<Book> update(@PathVariable Long id,@Valid @RequestBody Book b){ return ResponseEntity.ok(bookService.updateBook(id,b)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){ bookService.deleteBook(id); return ResponseEntity.noContent().build(); }
}
