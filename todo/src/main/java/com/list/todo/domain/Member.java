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
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Todo> todoList = new ArrayList<>();

    @Builder
    public Member(Long id, String nickname, String email, String password, RoleType role) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void addTodo(Todo todo) {
        todo.updateWriter(this);
        this.todoList.add(todo);
    }
}
