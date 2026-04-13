package com.libraryms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_record")
public class BorrowRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FIX: EAGER so b.member.name and b.book.title are always populated
    // when a BorrowRecord is serialized to JSON for the frontend
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.BORROWED;

    @Column(name = "fine_amount")
    private double fineAmount = 0.0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BorrowRecord() {}

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId()              { return id; }
    public Member getMember()        { return member; }
    public Book getBook()            { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate()    { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Status getStatus()        { return status; }
    public double getFineAmount()    { return fineAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long v)              { id = v; }
    public void setMember(Member v)        { member = v; }
    public void setBook(Book v)            { book = v; }
    public void setBorrowDate(LocalDate v) { borrowDate = v; }
    public void setDueDate(LocalDate v)    { dueDate = v; }
    public void setReturnDate(LocalDate v) { returnDate = v; }
    public void setStatus(Status v)        { status = v; }
    public void setFineAmount(double v)    { fineAmount = v; }
    public void setCreatedAt(LocalDateTime v) { createdAt = v; }
    public void setUpdatedAt(LocalDateTime v) { updatedAt = v; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Member member; private Book book;
        private LocalDate borrowDate, dueDate;
        private Status status = Status.BORROWED;
        private double fineAmount = 0.0;
        public Builder member(Member v)        { member = v; return this; }
        public Builder book(Book v)            { book = v; return this; }
        public Builder borrowDate(LocalDate v) { borrowDate = v; return this; }
        public Builder dueDate(LocalDate v)    { dueDate = v; return this; }
        public Builder status(Status v)        { status = v; return this; }
        public Builder fineAmount(double v)    { fineAmount = v; return this; }
        public BorrowRecord build() {
            BorrowRecord r = new BorrowRecord();
            r.member = member; r.book = book;
            r.borrowDate = borrowDate; r.dueDate = dueDate;
            r.status = status; r.fineAmount = fineAmount;
            return r;
        }
    }

    public enum Status { BORROWED, RETURNED, OVERDUE, LOST }
}
