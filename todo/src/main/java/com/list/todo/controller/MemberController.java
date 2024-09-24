package com.list.todo.controller;

import com.list.todo.config.global.ResponseDTO;
import com.list.todo.config.jwt.TokenDTO;
import com.list.todo.domain.dto.MemberDTO;
import com.list.todo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/auth")
    public ResponseEntity join(@RequestBody MemberDTO.JoinReq joinReq) {
        memberService.join(joinReq);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "회원가입 되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberDTO.LoginReq loginReq, HttpServletRequest request, HttpServletResponse response) {
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


}
