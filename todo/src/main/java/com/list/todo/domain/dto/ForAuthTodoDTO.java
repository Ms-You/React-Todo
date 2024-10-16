package com.list.todo.domain.dto;

import com.list.todo.domain.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ForAuthTodoDTO {

    @Getter
    @NoArgsConstructor
    public static class TodoReq {
        private String title;
        private String description;
        private boolean done;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodoResp {
        private Long id;
        private String title;
        private String description;
        private boolean done;

        public static TodoResp entityToResp(Todo todo) {
            return new TodoResp(todo.getId(), todo.getTitle(), todo.getDescription(), todo.isDone());
        }
    }
}
