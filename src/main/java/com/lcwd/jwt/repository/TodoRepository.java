package com.lcwd.jwt.repository;

import com.lcwd.jwt.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoList, Long> {

}