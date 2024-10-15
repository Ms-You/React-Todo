package com.list.todo.controller;

import com.list.todo.config.global.ResponseDTO;
import com.list.todo.config.jwt.TokenDTO;
import com.list.todo.domain.dto.MemberDTO;
import com.list.todo.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity join(@RequestBody MemberDTO.JoinReq joinReq) {
        memberService.join(joinReq);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "회원가입 되었습니다."));
    }

    @PostMapping("/sign-in")
    public ResponseEntity login(@RequestBody MemberDTO.LoginReq loginReq, HttpServletResponse response) {
        TokenDTO tokenDTO = memberService.login(loginReq);

        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDTO.getRefreshToken())
                .maxAge(60 * 60 * 24 * 7)
                .path("/")
                .httpOnly(true)
                .build();

        response.setHeader("Authorization", "Bearer " + tokenDTO.getAccessToken());
        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "로그인 되었습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String accessToken) {
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            memberService.logout(accessToken.substring(7));
        }

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "로그아웃 되었습니다."));
    }

    @PostMapping("/reissue")
    public ResponseEntity reissueToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();

                    TokenDTO tokenDTO = memberService.reissue(refreshToken);

                    ResponseCookie responseCookie = ResponseCookie.from("RefreshToken", tokenDTO.getRefreshToken())
                            .maxAge(60 * 60 * 24 * 7)   // 7일
                            .path("/")
                            .httpOnly(true)
                            .build();

                    response.setHeader("Authorization", "Bearer " + tokenDTO.getAccessToken());
                    response.setHeader("Set-Cookie", responseCookie.toString());

                    return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "토큰이 재발급되었습니다."));
                }
            }
        }

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.FORBIDDEN.value(), "토큰을 재발급 중 문제가 발생했습니다."));
    }

}
