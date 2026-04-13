package com.libraryms.repository;
import com.libraryms.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate; import java.util.List; import java.util.Optional;
@Repository
public interface BorrowRepository extends JpaRepository<BorrowRecord,Long> {
    List<BorrowRecord> findByMemberId(Long memberId);
    List<BorrowRecord> findByBookId(Long bookId);
    List<BorrowRecord> findByStatus(BorrowRecord.Status status);
    Optional<BorrowRecord> findByMemberIdAndBookIdAndStatus(Long memberId,Long bookId,BorrowRecord.Status status);
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :today AND br.status = 'BORROWED'")
    List<BorrowRecord> findOverdueRecords(@Param("today") LocalDate today);
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.member.id = :memberId AND br.status = 'BORROWED'")
    long countActiveBorrowsByMember(@Param("memberId") Long memberId);
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate = :due AND br.status = 'BORROWED'")
    List<BorrowRecord> findRecordsDueOn(@Param("due") LocalDate due);
}
