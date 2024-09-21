package com.list.todo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "todo")
@Entity
public class TodoEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String title;
    private boolean done;

    @Builder
    private TodoEntity(Long id, Long memberId, String title, boolean done) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.done = done;
    }
}
