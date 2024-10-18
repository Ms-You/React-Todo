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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

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

        try {
            // 인증 실패 시 AuthenticationException 예외 처리
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenDTO tokenDTO = tokenProvider.generate(authentication);

            redisTemplate.opsForValue().set(
                    authentication.getName(),
                    tokenDTO.getRefreshToken(),
                    tokenProvider.getExpirationTime(tokenDTO.getRefreshToken()),
                    TimeUnit.MILLISECONDS
            );

            return tokenDTO;
        } catch (AuthenticationException e) {
            throw new GlobalException(ErrorCode.ID_OR_PASSWORD_WRONG);
        }
    }

    @Transactional
    public void logout(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // 로그아웃 시 refreshToken 삭제
        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }
    }

    @Transactional
    public TokenDTO reissue(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_VALIDATE);
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        String existsRefreshToken = redisTemplate.opsForValue().get(authentication.getName());

        if (!refreshToken.equals(existsRefreshToken)) {
            throw new GlobalException(ErrorCode.REFRESH_TOKEN_NOT_MATCHED);
        }

        TokenDTO tokenDTO = tokenProvider.generate(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                tokenDTO.getRefreshToken(),
                tokenProvider.getExpirationTime(tokenDTO.getRefreshToken()),
                TimeUnit.MILLISECONDS
        );

        return tokenDTO;
    }
}
