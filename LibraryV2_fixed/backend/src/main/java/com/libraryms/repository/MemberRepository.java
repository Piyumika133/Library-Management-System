package com.libraryms.repository;
import com.libraryms.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMemberIdNumber(String memberIdNumber);
    List<Member> findByActive(boolean active);
    List<Member> findByRole(Member.Role role);
    List<Member> findByIdVerified(boolean idVerified);
}
