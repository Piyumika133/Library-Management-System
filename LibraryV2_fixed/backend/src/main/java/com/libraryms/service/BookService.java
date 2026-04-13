package com.libraryms.service;
import com.libraryms.model.Book;
import com.libraryms.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @Transactional
public class BookService {
    private final BookRepository bookRepository;
    public BookService(BookRepository br){ this.bookRepository=br; }
    public List<Book> getAllBooks(){ return bookRepository.findAll(); }
    public Book getBookById(Long id){ return bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found: "+id)); }
    public Book getBookByIsbn(String isbn){ return bookRepository.findByIsbn(isbn).orElseThrow(()->new RuntimeException("Book not found: "+isbn)); }
    public Book getBookByBarcode(String barcode){ return bookRepository.findByBarcodeValue(barcode).orElseThrow(()->new RuntimeException("No book found for barcode: "+barcode)); }
    public List<Book> searchBooks(String kw){ return bookRepository.searchBooks(kw); }
    public List<Book> getByCategory(String cat){ return bookRepository.findByCategory(cat); }
    public List<Book> getAvailableBooks(){ return bookRepository.findByAvailableQuantityGreaterThan(0); }
    public List<String> getAllCategories(){ return bookRepository.findAllCategories(); }
    public Book addBook(Book book){
        if(bookRepository.existsByIsbn(book.getIsbn())) throw new RuntimeException("ISBN already exists");
        if(book.getBarcodeValue()==null||book.getBarcodeValue().isEmpty()){
            book.setBarcodeValue("BC-"+String.format("%06d",System.currentTimeMillis()%1000000));
        }
        return bookRepository.save(book);
    }
    public Book updateBook(Long id, Book d){
        Book b=getBookById(id);
        b.setTitle(d.getTitle()); b.setAuthor(d.getAuthor()); b.setCategory(d.getCategory());
        b.setDescription(d.getDescription()); b.setPublishedYear(d.getPublishedYear());
        b.setCoverImageUrl(d.getCoverImageUrl());
        if(d.getBarcodeValue()!=null&&!d.getBarcodeValue().isEmpty()) b.setBarcodeValue(d.getBarcodeValue());
        int diff=d.getQuantity()-b.getQuantity();
        b.setQuantity(d.getQuantity()); b.setAvailableQuantity(Math.max(0,b.getAvailableQuantity()+diff));
        return bookRepository.save(b);
    }
    public void deleteBook(Long id){ bookRepository.delete(getBookById(id)); }
    public void decreaseAvailability(Long bookId){
        Book b=getBookById(bookId);
        if(b.getAvailableQuantity()<=0) throw new RuntimeException("Book not available for borrowing");
        b.setAvailableQuantity(b.getAvailableQuantity()-1); bookRepository.save(b);
    }
    public void increaseAvailability(Long bookId){
        Book b=getBookById(bookId);
        if(b.getAvailableQuantity()>=b.getQuantity()) throw new RuntimeException("All copies already returned");
        b.setAvailableQuantity(b.getAvailableQuantity()+1); bookRepository.save(b);
    }
}
