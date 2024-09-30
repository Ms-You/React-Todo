package com.list.todo.domain;

import com.list.todo.domain.dto.TodoDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todo")
@Entity
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String description;
    private boolean done;

    @Builder
    public Todo(Long id, String title, String description, boolean done) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public void updateWriter(Member member) {
        this.member = member;
    }

    public void update(TodoDTO.TodoReq todoReq) {
        this.title = todoReq.getTitle();
        this.description = todoReq.getDescription();
        this.done = todoReq.isDone();
    }
}
