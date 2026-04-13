package com.libraryms.controller;
import com.libraryms.model.BorrowRecord;
import com.libraryms.model.Book;
import com.libraryms.service.BorrowService;
import com.libraryms.service.BookService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List; import java.util.Map;

@RestController @RequestMapping("/api/borrow") @CrossOrigin(origins="*")
public class BorrowController {
    private final BorrowService borrowService;
    private final BookService bookService;
    public BorrowController(BorrowService bs, BookService bks){ borrowService=bs; bookService=bks; }

    @PostMapping("/borrow")
    public ResponseEntity<?> borrow(@RequestParam Long memberId, @RequestParam Long bookId){
        try{ return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(memberId,bookId)); }
        catch(RuntimeException e){ return ResponseEntity.badRequest().body(Map.of("message",e.getMessage())); }
    }

    // ── Borrow by barcode scan ─────────────────────────────
    @PostMapping("/borrow-by-barcode")
    public ResponseEntity<?> borrowByBarcode(@RequestParam Long memberId, @RequestParam String barcodeValue){
        try{
            Book book = bookService.getBookByBarcode(barcodeValue);
            return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.borrowBook(memberId,book.getId()));
        } catch(RuntimeException e){ return ResponseEntity.badRequest().body(Map.of("message",e.getMessage())); }
    }

    // ── Lookup book by barcode (for scan preview) ──────────
    @GetMapping("/scan/{barcodeValue}")
    public ResponseEntity<?> scanBarcode(@PathVariable String barcodeValue){
        try{ return ResponseEntity.ok(bookService.getBookByBarcode(barcodeValue)); }
        catch(RuntimeException e){ return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Book not found for barcode: "+barcodeValue)); }
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<BorrowRecord> returnBook(@PathVariable Long id){ return ResponseEntity.ok(borrowService.returnBook(id)); }

    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<List<BorrowRecord>> getAll(){ return ResponseEntity.ok(borrowService.getAllBorrowRecords()); }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecord>> getByMember(@PathVariable Long memberId){ return ResponseEntity.ok(borrowService.getByMember(memberId)); }

    @GetMapping("/overdue") @PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
    public ResponseEntity<List<BorrowRecord>> overdue(){ return ResponseEntity.ok(borrowService.getOverdue()); }
}
