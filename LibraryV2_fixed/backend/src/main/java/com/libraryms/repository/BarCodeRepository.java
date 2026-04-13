package com.libraryms.repository;
import com.libraryms.model.BarCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface BarCodeRepository extends JpaRepository<BarCode,Long> {
    Optional<BarCode> findByBookId(Long bookId);
    boolean existsByBookId(Long bookId);
}
