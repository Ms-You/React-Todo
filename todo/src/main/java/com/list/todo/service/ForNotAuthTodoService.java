package com.list.todo.service;

import com.list.todo.domain.ForNotAuthTodo;
import com.list.todo.domain.dto.ForNotAuthTodoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForNotAuthTodoService {
    // 로그인 하지 않은 사용자를 위한 인메모리 데이터 저장소
    private final Map<UUID, ForNotAuthTodo> memoryMap = new ConcurrentHashMap<>();

    public List<ForNotAuthTodoDTO.ForNotAuthTodoResp> create(final ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        validate(todoReq);

        UUID id = UUID.randomUUID();

        ForNotAuthTodo forNotAuthTodo = ForNotAuthTodo.builder()
                .id(id)
                .title(todoReq.getTitle())
                .description(todoReq.getDescription())
                .done(todoReq.isDone())
                .build();

        memoryMap.put(id, forNotAuthTodo);

        return memoryMap.values()
                .stream()
                .map(ForNotAuthTodoDTO.ForNotAuthTodoResp::entityToResp)
                .collect(Collectors.toList());
    }

    public List<ForNotAuthTodoDTO.ForNotAuthTodoResp> retrieve() {
        return memoryMap.values()
                .stream()
                .map(ForNotAuthTodoDTO.ForNotAuthTodoResp::entityToResp)
                .collect(Collectors.toList());
    }

    public void update(UUID id, ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        ForNotAuthTodo forNotAuthTodo = memoryMap.get(id);
        forNotAuthTodo.update(todoReq);

        memoryMap.put(id, forNotAuthTodo);
    }

    public void delete(UUID id) {
        memoryMap.remove(id);
    }

    private void validate(final ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        if (todoReq == null) {
            log.warn("TodoRequestDTO cannot be null.");
            throw new RuntimeException("TodoRequestDTO cannot be null.");
        }
    }


}
