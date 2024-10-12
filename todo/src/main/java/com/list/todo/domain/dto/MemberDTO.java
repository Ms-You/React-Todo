package com.list.todo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDTO {
    @Getter
    @NoArgsConstructor
    public static class JoinReq {
        private String nickname;
        private String email;
        private String password;
        private String passwordConfirm;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginReq {
        private String email;
        private String password;
    }
}
