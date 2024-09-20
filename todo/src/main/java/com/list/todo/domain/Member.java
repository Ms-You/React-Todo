package com.list.todo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();

    @Builder
    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addTodo(Todo todo) {
        todo.updateWriter(this);
        this.todoList.add(todo);
    }
}
