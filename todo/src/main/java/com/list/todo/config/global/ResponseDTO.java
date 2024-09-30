package com.list.todo.config.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private int code;
    private String message;
    private T result;

    public ResponseDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
