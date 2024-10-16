package com.list.todo.domain;

import com.list.todo.domain.dto.ForNotAuthTodoDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForNotAuthTodo {

    private UUID id;
    private String title;
    private String description;
    private boolean done;

    @Builder
    public ForNotAuthTodo(UUID id, String title, String description, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public void update(ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        this.title = todoReq.getTitle();
        this.description = todoReq.getDescription();
        this.done = todoReq.isDone();
    }
}
