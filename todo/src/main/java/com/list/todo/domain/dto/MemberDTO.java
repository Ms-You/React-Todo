package com.list.todo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDTO {
    @Getter
    @NoArgsConstructor
    public static class JoinReq {
        private String username;
        private String email;
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginReq {
        private String email;
        private String password;
    }
}
