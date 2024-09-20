package com.list.todo.controller;

import com.list.todo.config.global.ResponseDTO;
import com.list.todo.domain.dto.TodoDTO;
import com.list.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {
    private final TodoService todoService;

    /**
     * 새로운 항목 추가
     * @param todoReq
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO.TodoReq todoReq) {
        try {
            List<TodoDTO.TodoResp> todoRespList = todoService.create(todoReq);

            ResponseDTO<TodoDTO.TodoResp> response = ResponseDTO.<TodoDTO.TodoResp>builder()
                    .data(todoRespList)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<TodoDTO.TodoResp> response = ResponseDTO.<TodoDTO.TodoResp>builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 투두 목록 조회
     * @return
     */
//    @GetMapping
//    public ResponseEntity<?> retrieveTodoList() {
//        List<TodoDTO.TodoResp> todoRespList = todoService.retrieve();
//
//        ResponseDTO<TodoDTO.TodoResp> response = ResponseDTO.<TodoDTO.TodoResp>builder()
//                .data(todoRespList)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }

    /**
     * 항목 수정
     * @param id
     * @param todoReq
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable(name = "id") Long id, @RequestBody TodoDTO.TodoReq todoReq) {
        try {
            todoService.update(id, todoReq);

            ResponseDTO<String> response = ResponseDTO.<String>builder()
                    .message("성공적으로 수정되었습니다.")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            ResponseDTO<String> response = ResponseDTO.<String>builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable(name = "id") Long id) {
        try {
            todoService.delete(id);

            ResponseDTO<String> response = ResponseDTO.<String>builder()
                    .message("성공적으로 삭제되었습니다.")
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            ResponseDTO<String> response = ResponseDTO.<String>builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.ok(response);
        }
    }

}
