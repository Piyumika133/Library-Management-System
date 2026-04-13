package com.libraryms.service;
import com.libraryms.model.Member;
import com.libraryms.repository.BorrowRepository;
import com.libraryms.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; import java.util.Optional;

@Service @Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final BorrowRepository borrowRepository;
    public MemberService(MemberRepository mr, PasswordEncoder pe, EmailService es, BorrowRepository br) {
        this.memberRepository=mr; this.passwordEncoder=pe; this.emailService=es; this.borrowRepository=br;
    }

    public List<Member> getAllMembers(){ return memberRepository.findAll(); }
    public Member getMemberById(Long id){ return memberRepository.findById(id).orElseThrow(()->new RuntimeException("Member not found: "+id)); }
    public Member getMemberByEmail(String email){ return memberRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Member not found: "+email)); }
    public Optional<Member> findByEmail(String email){ return memberRepository.findByEmail(email); }

    public Member registerMember(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())) throw new RuntimeException("Email already in use");
        if(member.getMemberIdNumber()!=null && !member.getMemberIdNumber().isEmpty() &&
           memberRepository.existsByMemberIdNumber(member.getMemberIdNumber()))
            throw new RuntimeException("Member ID already registered");
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        Member saved = memberRepository.save(member);
        emailService.sendWelcomeEmail(saved.getEmail(), saved.getName());
        return saved;
    }

    public Member updateMember(Long id, Member details) {
        Member m = getMemberById(id);
        m.setName(details.getName()); m.setPhone(details.getPhone()); m.setAddress(details.getAddress());
        if(details.getRole()!=null) m.setRole(details.getRole());
        return memberRepository.save(m);
    }

    public void changePassword(Long id, String oldPass, String newPass) {
        if(newPass==null||newPass.trim().length()<6) throw new RuntimeException("Password must be at least 6 characters");
        Member m = getMemberById(id);
        if(!passwordEncoder.matches(oldPass, m.getPassword())) throw new RuntimeException("Incorrect current password");
        m.setPassword(passwordEncoder.encode(newPass));
        memberRepository.save(m);
    }

    public Member verifyId(Long memberId, String nicFrontPath, String nicBackPath) {
        Member m = getMemberById(memberId);
        m.setNicFrontPath(nicFrontPath);
        m.setNicBackPath(nicBackPath);
        return memberRepository.save(m);
    }

    public Member approveId(Long memberId) {
        Member m = getMemberById(memberId);
        m.setIdVerified(true);
        memberRepository.save(m);
        emailService.sendIdVerifiedEmail(m.getEmail(), m.getName());
        return m;
    }

    public Member rejectId(Long memberId) {
        Member m = getMemberById(memberId);
        m.setIdVerified(false);
        m.setNicFrontPath(null);
        m.setNicBackPath(null);
        return memberRepository.save(m);
    }

    public List<Member> getPendingVerification() {
        return memberRepository.findAll().stream()
                .filter(m -> !m.isIdVerified() && (m.getNicFrontPath()!=null || m.getNicBackPath()!=null))
                .toList();
    }

    public Member toggleStatus(Long id) {
        Member m = getMemberById(id);
        m.setActive(!m.isActive());
        return memberRepository.save(m);
    }

    public void deleteMember(Long id) {
        Member m = getMemberById(id);
        long active = borrowRepository.countActiveBorrowsByMember(id);
        if(active>0) throw new RuntimeException("Cannot delete: member has "+active+" active borrow(s)");
        memberRepository.delete(m);
    }

    public List<Member> getActiveMembers(){ return memberRepository.findByActive(true); }
    public List<Member> getMembersByRole(Member.Role role){ return memberRepository.findByRole(role); }
}
