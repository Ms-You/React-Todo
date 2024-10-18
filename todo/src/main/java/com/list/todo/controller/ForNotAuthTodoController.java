package com.list.todo.controller;

import com.list.todo.config.global.ResponseDTO;
import com.list.todo.domain.dto.ForNotAuthTodoDTO;
import com.list.todo.service.ForNotAuthTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class ForNotAuthTodoController {
    private final ForNotAuthTodoService forNotAuthTodoService;

    /**
     * 새로운 항목 추가
     * @param todoReq
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        List<ForNotAuthTodoDTO.ForNotAuthTodoResp> todoRespList = forNotAuthTodoService.create(todoReq);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "항목이 추가되었습니다.", todoRespList));
    }

    /**
     * 투두 목록 조회
     * @return
     */
    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        List<ForNotAuthTodoDTO.ForNotAuthTodoResp> todoRespList = forNotAuthTodoService.retrieve();

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "투두 리스트를 조회합니다.", todoRespList));
    }

    /**
     * 항목 수정
     * @param id
     * @param todoReq
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable(name = "id") UUID id, @RequestBody ForNotAuthTodoDTO.ForNotAuthTodoReq todoReq) {
        forNotAuthTodoService.update(id, todoReq);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "성공적으로 수정되었습니다."));
    }

    /**
     * 항목 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable(name = "id") UUID id) {
        forNotAuthTodoService.delete(id);

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "성공적으로 삭제되었습니다."));
    }

}
