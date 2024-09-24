package com.list.todo.config;

import com.list.todo.domain.Member;
import com.list.todo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().toString());

        return new User(member.getEmail(), member.getPassword(), Collections.singleton(authority));
    }
}
