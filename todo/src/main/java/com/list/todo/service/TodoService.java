package com.list.todo.service;

import com.list.todo.domain.Todo;
import com.list.todo.domain.dto.TodoDTO;
import com.list.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Transactional
    public List<TodoDTO.TodoResp> create(final TodoDTO.TodoReq todoReq) {
        validate(todoReq);

        Todo todo = Todo.builder()
                .title(todoReq.getTitle())
                .description(todoReq.getDescription())
                .done(todoReq.isDone())
                .build();

        /*
            Member 조회
            member.addTodo(todo);

            if (todoReq.get() == null) {
                log.warn("Unknown User.");
                throw new RuntimeException("Unknown User.");
            }
        */

        todoRepository.save(todo);

        log.info("TodoEntity Id : {} is saved.", todo.getId());

        return todoRepository.findByMember(todo.getMember()).stream()
                .map(TodoDTO.TodoResp::entityToResp)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<TodoDTO.TodoResp> retrieve() {
//        // Member 조회
//        // member로 todo 목록 조회
//
//        return todoList.stream()
//                .map(TodoDTO.TodoResp::entityToResp)
//                .collect(Collectors.toList());
//    }

    @Transactional
    public void update(Long id, TodoDTO.TodoReq todoReq) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 항목이 존재하지 않습니다.")
        );

        todo.update(todoReq);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 항목이 존재하지 않습니다.")
        );

        todoRepository.delete(todo);
    }


    private void validate(final TodoDTO.TodoReq todoReq) {
        if (todoReq == null) {
            log.warn("TodoRequestDTO cannot be null.");
            throw new RuntimeException("TodoRequestDTO cannot be null.");
        }
    }

}
