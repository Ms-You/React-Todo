package com.list.todo.domain.dto;

import com.list.todo.domain.ForNotAuthTodo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class ForNotAuthTodoDTO {
    @Getter
    @NoArgsConstructor
    public static class ForNotAuthTodoReq {
        private String title;
        private String description;
        private boolean done;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForNotAuthTodoResp {
        private UUID id;
        private String title;
        private String description;
        private boolean done;

        public static ForNotAuthTodoDTO.ForNotAuthTodoResp entityToResp(ForNotAuthTodo forNotAuthTodo) {
            return new ForNotAuthTodoDTO.ForNotAuthTodoResp(forNotAuthTodo.getId(), forNotAuthTodo.getTitle(), forNotAuthTodo.getDescription(), forNotAuthTodo.isDone());
        }
    }
}
