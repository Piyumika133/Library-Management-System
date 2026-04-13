package com.libraryms.service;
import com.libraryms.model.Member;
import com.libraryms.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    public UserDetailsServiceImpl(MemberRepository r){ this.memberRepository=r; }
    @Override @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member m = memberRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        return User.builder()
                .username(m.getEmail()).password(m.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_"+m.getRole().name())))
                .accountLocked(!m.isActive()).disabled(!m.isActive())
                .accountExpired(false).credentialsExpired(false).build();
    }
}
