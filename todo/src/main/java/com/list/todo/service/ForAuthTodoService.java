package com.list.todo.service;

import com.list.todo.common.ErrorCode;
import com.list.todo.common.GlobalException;
import com.list.todo.config.jwt.SecurityUtil;
import com.list.todo.domain.Member;
import com.list.todo.domain.Todo;
import com.list.todo.domain.dto.ForAuthTodoDTO;
import com.list.todo.repository.MemberRepository;
import com.list.todo.repository.ForAuthTodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ForAuthTodoService {
    private final ForAuthTodoRepository forAuthTodoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<ForAuthTodoDTO.TodoResp> create(final ForAuthTodoDTO.TodoReq todoReq) {
        validate(todoReq);

        Todo todo = Todo.builder()
                .title(todoReq.getTitle())
                .description(todoReq.getDescription())
                .done(todoReq.isDone())
                .build();

        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMember()).orElseThrow(
                () -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND)
        );

        member.addTodo(todo);

        forAuthTodoRepository.save(todo);

        log.info("TodoEntity Id : {} is saved.", todo.getId());

        return forAuthTodoRepository.findByMember(todo.getMember()).stream()
                .map(ForAuthTodoDTO.TodoResp::entityToResp)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ForAuthTodoDTO.TodoResp> retrieve() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMember()).orElseThrow(
                () -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND)
        );

        return member.getTodoList().stream()
                .map(ForAuthTodoDTO.TodoResp::entityToResp)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long id, ForAuthTodoDTO.TodoReq todoReq) {
        Todo todo = forAuthTodoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 항목이 존재하지 않습니다.")
        );

        todo.update(todoReq);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = forAuthTodoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 항목이 존재하지 않습니다.")
        );

        forAuthTodoRepository.delete(todo);
    }


    private void validate(final ForAuthTodoDTO.TodoReq todoReq) {
        if (todoReq == null) {
            log.warn("TodoRequestDTO cannot be null.");
            throw new RuntimeException("TodoRequestDTO cannot be null.");
        }
    }

}
