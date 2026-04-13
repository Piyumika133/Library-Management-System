package com.libraryms.service;
import com.libraryms.model.*;
import com.libraryms.repository.BorrowRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate; import java.time.temporal.ChronoUnit; import java.util.List;

@Service @Transactional
public class BorrowService {
    private static final int MAX_DAYS=14, MAX_BOOKS=5;
    private static final double FINE_PER_DAY=10.0;
    private final BorrowRepository borrowRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final EmailService emailService;
    public BorrowService(BorrowRepository br, BookService bs, MemberService ms, EmailService es){
        borrowRepository=br; bookService=bs; memberService=ms; emailService=es;
    }
    public BorrowRecord borrowBook(Long memberId, Long bookId){
        Member member = memberService.getMemberById(memberId);
        if(!member.isActive()) throw new RuntimeException("Account is inactive. Contact admin.");
        long active = borrowRepository.countActiveBorrowsByMember(memberId);
        if(active>=MAX_BOOKS) throw new RuntimeException("Borrow limit reached (max "+MAX_BOOKS+" books)");
        borrowRepository.findByMemberIdAndBookIdAndStatus(memberId,bookId,BorrowRecord.Status.BORROWED)
                .ifPresent(r->{ throw new RuntimeException("Member already has this book borrowed"); });
        Book book = bookService.getBookById(bookId);
        bookService.decreaseAvailability(bookId);
        BorrowRecord rec = BorrowRecord.builder().member(member).book(book)
                .borrowDate(LocalDate.now()).dueDate(LocalDate.now().plusDays(MAX_DAYS))
                .status(BorrowRecord.Status.BORROWED).build();
        BorrowRecord saved = borrowRepository.save(rec);
        emailService.sendBorrowConfirmation(member.getEmail(), member.getName(), book.getTitle(), saved.getDueDate().toString());
        return saved;
    }
    public BorrowRecord returnBook(Long borrowId){
        BorrowRecord rec = borrowRepository.findById(borrowId).orElseThrow(()->new RuntimeException("Borrow record not found"));
        if(rec.getStatus()==BorrowRecord.Status.RETURNED) throw new RuntimeException("Book already returned");
        rec.setReturnDate(LocalDate.now()); rec.setStatus(BorrowRecord.Status.RETURNED);
        if(LocalDate.now().isAfter(rec.getDueDate())){
            long days=ChronoUnit.DAYS.between(rec.getDueDate(),LocalDate.now());
            rec.setFineAmount(days*FINE_PER_DAY);
        }
        bookService.increaseAvailability(rec.getBook().getId());
        BorrowRecord saved=borrowRepository.save(rec);
        if(saved.getFineAmount()>0) emailService.sendFineNotification(rec.getMember().getEmail(),rec.getMember().getName(),rec.getBook().getTitle(),saved.getFineAmount());
        return saved;
    }
    public List<BorrowRecord> getAllBorrowRecords(){ return borrowRepository.findAll(); }
    public List<BorrowRecord> getByMember(Long memberId){ return borrowRepository.findByMemberId(memberId); }
    public List<BorrowRecord> getByBook(Long bookId){ return borrowRepository.findByBookId(bookId); }
    public List<BorrowRecord> getOverdue(){ return borrowRepository.findOverdueRecords(LocalDate.now()); }
    @Scheduled(cron="0 0 9 * * *")
    public void processOverdue(){
        borrowRepository.findOverdueRecords(LocalDate.now()).forEach(rec->{
            rec.setStatus(BorrowRecord.Status.OVERDUE); borrowRepository.save(rec);
            long days=ChronoUnit.DAYS.between(rec.getDueDate(),LocalDate.now());
            emailService.sendOverdueReminder(rec.getMember().getEmail(),rec.getMember().getName(),rec.getBook().getTitle(),days,days*FINE_PER_DAY);
        });
    }
}
