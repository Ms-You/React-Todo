package com.list.todo.service;

import com.list.todo.common.ErrorCode;
import com.list.todo.common.GlobalException;
import com.list.todo.config.jwt.TokenDTO;
import com.list.todo.config.jwt.TokenProvider;
import com.list.todo.domain.Member;
import com.list.todo.domain.RoleType;
import com.list.todo.domain.dto.MemberDTO;
import com.list.todo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void join(MemberDTO.JoinReq joinReq) {
        if (memberRepository.existsByEmail(joinReq.getEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        checkPasswordConfirm(joinReq.getPassword(), joinReq.getPasswordConfirm());

        Member member = Member.builder()
                .nickname(joinReq.getNickname())
                .email(joinReq.getEmail())
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .role(RoleType.ROLE_USER)
                .build();

        memberRepository.save(member);
    }

    private void checkPasswordConfirm(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new GlobalException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
    }

    @Transactional
    public TokenDTO login(MemberDTO.LoginReq loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDTO tokenDTO = tokenProvider.generate(authentication);

        return tokenDTO;
    }

    // 로그아웃, 토큰 재발급 추가
}
