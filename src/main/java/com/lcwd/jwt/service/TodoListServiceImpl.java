package com.lcwd.jwt.service;

import java.util.List;

import com.lcwd.jwt.entity.TodoList;
import com.lcwd.jwt.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoListServiceImpl {

    private final TodoRepository todoRepository;

    public TodoListServiceImpl(TodoRepository todoRepository) {
        this.todoRepository= todoRepository;
    }

    public TodoList saveTodoList(TodoList todoList) {

        return this.todoRepository.save(todoList);
    }

    public List<TodoList> getAllTodoList(){
        return this.todoRepository.findAll();
    }
}
